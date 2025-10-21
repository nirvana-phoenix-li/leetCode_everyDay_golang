import os
import sys
from pyspark.sql import SparkSession
from pyspark import SparkContext, SparkConf, HiveContext
import pyspark
from anti_cheat_common.redis_init import redisConnect

app_name = 'mr2_redis'
conf = SparkConf().setAppName(app_name).setMaster("yarn")
conf.set("spark.yarn.queue", "root.yhtech.sc_algorithm")
conf.set("spark.driver.memory", '2G')      
conf.set("spark.executor.memory", '4G')  
conf.set("spark.port.maxRetries", 50)
conf.set("spark.dynamicAllocation.enabled", True)      
conf.set("spark.dynamicAllocation.minExecutors", 5)   
conf.set("spark.dynamicAllocation.maxExecutors", 15)   
conf.set("spark.executor.cores", 3)
conf.set("spark.sql.crossJoin.enabled", True)
conf.set("spark.rpc.message.maxSize", 1024)
conf.set("spark.driver.maxResultSize", "4g")

sc = SparkContext(conf=conf)
hc = HiveContext(sc)
spark = SparkSession(sc)

sdt = '${sdt}'  
mon = '${mon}'
ENV = "online_anticheat"  
redis_prefix="RISK_MR2:"
ratio = 0.03

VALID_TIME=3600 * 24 * 2

r = redisConnect(ENV)
def push_data_to_redis(sql):

    print(spark.sql(sql).head(5))

    def partition2redis(iterator):
        startRedis = redisConnect(ENV)
        with startRedis.r.pipeline() as ctx:
            c = 0
            for redis_key, redis_value in iterator:
                ctx.setex(redis_key, VALID_TIME, str(redis_value))
                c += 1
                if c % 500 == 0:
                    ctx.execute()
            ctx.execute()

    spark.sql(sql).rdd.map(lambda x: (x.redis_key, x.redis_value)).partitionBy(5).foreachPartition(partition2redis)
   
    
# 保证推的是全量，能够实时过滤数据
# 必须先推1再推2  
sql1 = f"""
        select concat('{redis_prefix}', a.member_id) as redis_key
                ,case when multi_shop_cnt > 4 and fresh_ratio < {ratio} and out_of_line_ratio < {ratio} then  round(85+rand()*15,2) else 0 end as redis_value
        from data_mining.data_mining_anticheat_multi_region_user_v2 a 
        inner join (
                select member_id
                    ,max(fresh_ratio) as fresh_ratio -- 必须取最大,去除多关联噪声
                    ,max(out_of_line_ratio) as out_of_line_ratio
                from data_mining.data_mining_anticheat_white_full_period 
                where month = '{mon}'
                group by member_id
        )b
        on a.member_id = b.member_id
        and a.sdt = {sdt}
   """
  
   
sql2 = f"""
        select
            concat('{redis_prefix}', member_id) as redis_key
            , round(85+rand()*15,2) as redis_value
        from 
        ( 
        select member_id
        from data_mining.data_mining_anticheat_crowd_sourcing_user
        where sdt = {sdt} and cnt > 6
        and mobile not in ('18062119848')
        
        union  
        select member_id
        from data_mining.data_mining_anticheat_crowd_sourcing_user_v2
        where sdt = {sdt} and cnt > 4
        
        union
        select a.member_id
        from data_mining.data_mining_anticheat_multi_region_user a
        inner join data_mining.data_mining_anticheat_white_full_period b 
        on a.mobile = b.mobile
        and a.member_id = b.member_id
        and b.month = '{mon}'
        and a.sdt = '{sdt}' 
        and a.score >= 85
        and b.fresh_ratio < 0.1
        )a 

    """
        
 # union
# select member_id
# from data_mining.data_mining_anticheat_multi_region_user
# where sdt = {sdt} and score >= 85
    
push_data_to_redis(sql1)
print("push shop_data to redis successful!")
push_data_to_redis(sql2)
print("push addr_data to redis successful!")

from pyspark import SQLContext, HiveContext
from pyspark import SparkContext, SparkConf
from pyspark.sql import SparkSession
import os, time
from datetime import datetime, timedelta

app_name = "mr_v2"
conf = SparkConf().setAppName(app_name).setMaster("yarn")
conf.set("spark.yarn.queue", "root.yhtech.algorithm")
conf.set("spark.driver.memory", '2G')
conf.set("spark.executor.memory", '4G')
conf.set("deploy-mode", 'cluster')
conf.set("spark.port.maxRetries", 500)
conf.set("spark.dynamicAllocation.enabled", True)
conf.set("spark.dynamicAllocation.minExecutors", 10)
conf.set("spark.dynamicAllocation.maxExecutors", 15)
conf.set("spark.executor.cores", 3)
conf.set("spark.sql.crossJoin.enabled", True)
conf.set("spark.rpc.message.maxSize", 1024)
conf.set("spark.driver.maxResultSize", "4g")
conf.set("spark.storage.memoryFraction", 0.4)
conf.set("spark.shuffle.memoryFraction", 0.4)
conf.set("spark.default.parallelism", 80)
sc = SparkContext(conf=conf)
hc = HiveContext(sc)
spark = SparkSession(sc)

# 定义要过滤的老店编，防止在同一时间窗口下，对同一个门店的不同id重复计数
filter_shop_list = """
    '9163',
    '9047',
    '9039',
    '9139',
    '9144',
    '9439',
    '95C8',
    '9549',
    '9440',
    '9046',
    '9010',
    '9017',
    '9296',
    '9893',
    '90S7',
    '9350',
    '9531',
    '9559',
    '9165',
    '9199',
    '9129',
    '9018',
    '9116',
    '9064',
    '9013',
    '9011',
    '9025',
    '9029',
    '9438',
    '9038',
    '90V3',
    '9496',
    '9049',
    '9250',
    '9285',
    '90U7',   
    '9466',
    '9671',
    '9365',
    '90L6',
    '9494',
    '9284',
    '9254',
    '9219',
    '9812',
    '9678',
    '9359',
    '9148',
    '9084',
    '9576',
    '9085',
    '9360',
    '9MGA',
    '9149',  
    '9256' 
"""
# 2023.06.28 9MGA回切到9494
# 2023.08.30 9256切换到9MGJ
# 90U7截止是福建， 9494截止是成都，共5家;后面都是重庆,9284到9360；福建为1016代码切换，成都重庆1106
def run(start_sdt, end_sdt):
    spark.sql(f"""
        with tab as (
            select member_id
                   ,mobile 
            from dim.dim_member_info
            where sdt = {end_sdt} 
        ),


        mid_tab as (
            select  member_id
                    ,shop_id
                    ,root_order_id
            from
            dwd.dwd_sale_pay_order_item_1d_di   b1
            inner join data_mining.data_mining_anticheat_sku_goods b2
            on regexp_replace(b1.src_goods_id, '[a-zA-Z]-', '')  = b2.dim_goods_id     
            and  b1.sdt >= {start_sdt}
            and  b1.sdt <= {end_sdt}
            and coupon_amt > 0
            and channel_id_yunc in ('1', '5', '6', '7', '13') 
            and order_sts = 'os.completed' 
            and shop_id not in ({filter_shop_list})
            and is_root_order = '1'
            and final_sales_qty > 0 
            group by member_id,shop_id,root_order_id
        )


        insert overwrite table data_mining.data_mining_anticheat_multi_region_user_di_v2  PARTITION(sdt = {end_sdt})
        select  a.member_id
                ,mobile
                ,multi_shop_cnt
                ,multi_addr_cnt
                ,single_shop_cnt
        from tab a
        left join   --多日多地址
        (   
            select  member_id,
                    count(distinct recpt_city_region_addr) as multi_addr_cnt
            from
            (
            select  b1a.member_id
                    ,regexp_replace(concat(recpt_city, '', recpt_region, '', recpt_addr),'\r|\n|\r\n','')  as recpt_city_region_addr
            from mid_tab b1a
            inner join dwd.dwd_fulfill_sub_order_di b1b
            on b1a.root_order_id = b1b.root_order_id
            and order_distr_sts = '80'
            and sdt >= {start_sdt}
            and sdt <= {end_sdt}
            and shop_deliv_type = '1' --配送单
            )b1
            group by member_id
        )b
        on a.member_id = b.member_id
        left join  --单日多门店
        (   
            select  member_id,
                    count(distinct shop_id) as single_shop_cnt
            from
            dwd.dwd_sale_pay_order_item_1d_di   c1
            inner join data_mining.data_mining_anticheat_sku_goods c2
            on regexp_replace(c1.src_goods_id, '[a-zA-Z]-', '')  = c2.dim_goods_id   
            and  c1.sdt = {end_sdt}
            and coupon_amt > 0
            and channel_id_yunc in ('1', '5', '6', '7', '13') 
            and order_sts = 'os.completed'  
            and final_sales_qty > 0 
            group by member_id
        )c
         on a.member_id = c.member_id
        left join --多日多门店
        (   
            select  member_id
                    ,count(distinct shop_id) as multi_shop_cnt
            from mid_tab 
            group by member_id
        )d
        on a.member_id = d.member_id
        where multi_shop_cnt > 1 
        or single_shop_cnt > 1 
        or multi_addr_cnt > 1


    """)
    
    
if __name__ == '__main__':    
    run('${start_sdt}',  '${end_sdt}')


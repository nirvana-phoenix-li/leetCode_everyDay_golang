import logging
import sys
import os
from pyspark import SQLContext,HiveContext
from pyspark import SparkContext, SparkConf
from pyspark.sql import SparkSession
import gc
import warnings
from gensim.models import Word2Vec
from tqdm import tqdm
from sklearn.cluster import DBSCAN
from datetime import datetime, timedelta
import numpy as np
import pandas as pd


warnings.filterwarnings("ignore")
logging.basicConfig(format='%(asctime)s [%(levelname)s] %(message)s',
                    level=logging.INFO)
logger = logging.getLogger(__name__)
logger.info('start........')
conf = SparkConf().setAppName('anticheat').setMaster("yarn")
conf.set("spark.yarn.queue", "root.yhtech.algorithm")
conf.set("spark.driver.memory", '4G')       # 一般这个 2G 就够了 不需要调整
conf.set("spark.executor.memory", '6G')  # 2个G的去调
conf.set("deploy-mode", 'cluster')
conf.set("spark.port.maxRetries", 50)
conf.set("spark.driver.maxResultSize", "8G")
conf.set("spark.dynamicAllocation.enabled", False)      # 打开动态   也可以直接设置False 直接关闭 动态分配
conf.set("spark.dynamicAllocation.minExecutors", 1)    # 最小
conf.set("spark.dynamicAllocation.maxExecutors", 10)    # 最大 10
conf.set("spark.executor.cores", 5) 
conf.set("spark.sql.crossJoin.enabled", True)
sc = SparkContext(conf=conf)
hc=HiveContext(sc)
spark = SparkSession(sc)
sqlContext = SQLContext(sc)
sqlContext.setConf("hive.exec.dynamic.partition.mode","nonstrict")

TODAY = "${sdt}"

DAY_DIFF = 60
DAY_DIFF2 = 30
DAY_DIFF3 = 180 

WEIGHT_FOR_NUM = 0.9

table_name = "data_mining.data_mining_anticheat_black_list"

# 返回分数的范围： 0 ～ 100
def num2score(x, a=2, b=0.5):
    """
    :param x:  >= 4
    :param a:
    :param b:
    :return: score
    """
    if x > 4:
        res = 100 / (1 + np.exp(-1 * (x - a) * b))
    elif x > 1 :
        res = 100 / (1 + np.exp(-1 * (x - a) * b)) - 40
    else:
        res = 0

    return res


def discountRate2score(x):
    return x * 100 // 10 * 10

def get_final_score(x, weight=0.8, weight_decay=0.8, larger_factor=2):
    """
    :param x:
    :param weight:
    :param weight_decay:
    :return:
    分数映射策略：
    （1）设备号、设备指纹关联5个以上（包括5个），为高风险；
    （2）收货手机号、支付账号关联7个以上（包括7个），为高风险；
    """
    if x["type"] == 2 or x["type"] == 4:
        if x["num"] >= 12:
            score = num2score(x["num"])
        else:
            score = num2score(x["num"]) * weight + discountRate2score(x["discount_rate"]) * (1 - weight)
    else:
        if x["num"] >= 20:
            score = num2score(x["num"])
        else:
            score = num2score(x["num"]) * weight + discountRate2score(x["discount_rate"]) * (1 - weight)

    # 当折扣力度小于0.2，并且团伙数小于15
    if x["num"] < 15 and x["discount_rate"] <= 0.15:
        score *= weight_decay

    return round(score, 2)

def get_black_list_table(end_sdt):
    logger.info("get_black_list_table")

    info_hql = f"""
    select a.user_id,
            a.identity,
            a.num,
            a.type,
            a.score,
            c.discount_rate,
            cast(a.level as string) as level,
            a.sdt
        from data_mining.data_mining_anticheat_raw_black_list a
        left join(
            select user_id, discount_rate 
            from data_mining.data_mining_anticheat_raw_black_list_discount_rate b
            where sdt = '{end_sdt}'
        )c
        on a.user_id = c.user_id
        and a.sdt = '{end_sdt}'
        where a.sdt = '{end_sdt}'
    """
    df_data = spark.sql(info_hql)
    # df_data = df_data.toPandas()
    merged_csv = "data_mining_anticheat_black_list"
    
    os.system(f"""hdfs dfs -mkdir /user/81024371/one_person/black_list_file/{TODAY}""")
    df_data.write.format("csv") \
    .option("header", "false") \
    .option("sep", ",") \
    .mode("overwrite") \
    .save(f"/user/81024371/one_person/black_list_file/{TODAY}")
    logger.info("hdfs save success")
    os.system(f"""hdfs dfs -getmerge /user/81024371/one_person/black_list_file/{TODAY}/*.csv {merged_csv}""")
    logger.info("download success")
    logger.info("hdfs save success")
    all_columns = ["user_id","identity","num","type","score","discount_rate","level","sdt"]
    dtype = {
        "user_id":str,
        "identity":str,
        "num":int,
        "type":int,
        "score":float,
        "discount_rate":float,
        "level":str,
        "sdt":str
    }
    df_data = pd.read_csv(merged_csv,
                        header=None, 
                        names=all_columns,
                        # chunksize = 1000,
                        dtype=dtype
                        )
    logger.info("get tests over!")
    print("num:", len(df_data))
    # os.system(f"""hdfs dfs -rm -r /user/81024371/one_person/black_list_file/{TODAY}/*.csv""")
    os.system(f"""hdfs dfs -rm -r /user/81024371/one_person/black_list_file/{TODAY}""")
    logger.info("delete csv file!")
    df_data.columns = all_columns
    print(df_data.head())
    

    df_data["score"] = df_data.apply(
        lambda x: get_final_score(x, WEIGHT_FOR_NUM), axis=1)

    df_data = df_data.fillna(0)
    
    for k, v in dtype.items():
        df_data[k] = df_data[k].astype(v)

    df_data = spark.createDataFrame(df_data)
    
    df_data.registerTempTable("anticheat_black_list")
    
    spark.sql(f"""alter table {table_name} drop if exists partition(sdt={end_sdt})""")
    sql = f"""
    insert overwrite table {table_name}
    partition(sdt)
    select * from anticheat_black_list
    """
    spark.sql(sql)
    spark.sql("drop table if exists anticheat_black_list")
    logger.info('get_black_list_table is Done!')


# 运行相关策略并存入表
def get_anticheat_result(sdt):

    logger.info("get_black_list_table...")
    get_black_list_table(sdt)
    logger.info("get_anticheat_result Done!")


logger.info("----- Pray For No Bug! >_< -----")

# append data to table
get_anticheat_result(TODAY)

logger.info("----- Well Done! <_> -----")

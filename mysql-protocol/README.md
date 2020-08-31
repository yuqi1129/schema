

### 1. 简介
本项目主要实现一个Java版本的'MySQL', 即支持MySQL协议， 以mysql-client、jdbc等形式访问数据库，目前进度如下:

- 简单实现在MySQL连接协议
- 支持建表、建db、show tables
- 支持查询
- 支持查看逻辑执行计划
- 简单实现了RBO与CBO
- 实现了物理执行计划Operator(MPP)

项目地址: [https://github.com/yuqi1129/schema/mysql-protocol](https://github.com/yuqi1129/schema/mysql-protocol), 欢迎小伙伴们一起参加

目前正在进行中
- 数据持久化(表数据持久化已完成, 表元数据todo中)与存储优化
- 支持插入语法与功能(done)
- 列类型丰富


### 2.如何启动该数据库

#### 2.1. 准备元数据数据库

本项目采用MySQL存放元数据, 建表语句如下:

```mysql
create database sloth;

use sloth;

-- store db infomation
CREATE TABLE `schemata` (
  `CATALOG_NAME` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `SCHEMA_NAME` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `DEFAULT_CHARACTER_SET_NAME` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `DEFAULT_COLLATION_NAME` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `SQL_PATH` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci


-- store table info
CREATE TABLE `tables` (
  `TABLE_CATALOG` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `TABLE_SCHEMA` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `TABLE_TYPE` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `ENGINE` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `VERSION` bigint(21) unsigned DEFAULT NULL,
  `ROW_FORMAT` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TABLE_ROWS` bigint(21) unsigned DEFAULT NULL,
  `AVG_ROW_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `DATA_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `MAX_DATA_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `INDEX_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `DATA_FREE` bigint(21) unsigned DEFAULT NULL,
  `AUTO_INCREMENT` bigint(21) unsigned DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CHECK_TIME` datetime DEFAULT NULL,
  `TABLE_COLLATION` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CHECKSUM` bigint(21) unsigned DEFAULT NULL,
  `CREATE_OPTIONS` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TABLE_COMMENT` varchar(2048) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `TABLE_SHARD` int(11) NOT NULL DEFAULT '1' COMMENT 'shard数目'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

-- store column info
CREATE TABLE `columns` (
  `TABLE_CATALOG` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `TABLE_SCHEMA` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `COLUMN_NAME` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `ORDINAL_POSITION` bigint(21) unsigned NOT NULL DEFAULT '0',
  `COLUMN_DEFAULT` longtext COLLATE utf8mb4_unicode_ci,
  `IS_NULLABLE` varchar(3) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `DATA_TYPE` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `CHARACTER_MAXIMUM_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `CHARACTER_OCTET_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `NUMERIC_PRECISION` bigint(21) unsigned DEFAULT NULL,
  `NUMERIC_SCALE` bigint(21) unsigned DEFAULT NULL,
  `DATETIME_PRECISION` bigint(21) unsigned DEFAULT NULL,
  `CHARACTER_SET_NAME` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `COLLATION_NAME` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `COLUMN_TYPE` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `COLUMN_KEY` varchar(3) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `EXTRA` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `PRIVILEGES` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `COLUMN_COMMENT` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `GENERATION_EXPRESSION` longtext COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

```
启动数据库，并替换MySQL相应的配置, 请参考`MysqlConnection`

**说明:**
**如果没有配置MySQL用来存储元数据, 所有的db、table数据均存放在内存之中，无法
在重启后恢复**

#### 2.2. 编译项目

```
mvn clean package
```

#### 2.3. 启动

##### 2.3.1 本地调试

在IDE中找到`FrontEndMain`, 直接启动main函数即可

##### 2.3.2 服务部署
打包项目后， 解压mysql-protocol-1.0-SNAPSHOT-RELEASE.tar.gz, 执行:
- `bin/start.sh start` 启动
- `bin/start.sh stop` 停止


### 3.连接 

#### 3.1 连接地址
```sql
 mysql -h127.0.0.1 -uroot -psxx -P3016
```
目前没有对用户名与密码进行验证，任何用户名与密码登陆都没有问题

#### 3.2 使用范例

```mysql
mysql -h127.0.0.1 -uroot -psxx -P3016
mysql: [Warning] Using a password on the command line interface can be insecure.
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 16
Server version: 5.7.22 Yuqi version

Copyright (c) 2000, 2018, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> show databases;
Empty set (0.01 sec)

mysql> create database db1;
Query OK, 1 row affected (0.01 sec)

mysql> show databases;
+----------+
| Database |
+----------+
| db1      |
+----------+
1 row in set (0.00 sec)

mysql> create database db2;
Query OK, 1 row affected (0.00 sec)

mysql>
mysql> use db1;
Database changed
mysql> show tables;
Empty set (0.00 sec)

mysql> create table sales(id bigint, person_id bigint, addr_id bigint, money double, desc varchar);
Query OK, 0 rows affected (0.01 sec)

mysql> create table person(id bigint, name varchar);
Query OK, 0 rows affected (0.00 sec)

mysql> create table addr(id bigint, name varchar, detail varchar);
Query OK, 0 rows affected (0.00 sec)

mysql> show tables;
+---------------+
| Tables_In_db1 |
+---------------+
| person        |
| addr          |
| sales         |
+---------------+
3 rows in set (0.01 sec)

mysql> explain
    -> select t3.sales_id, desc, t3.person_name, t4.name as addr_name from
    -> (
    -> select addr_id, t1.id as sales_id, desc, name as person_name from sales t1 inner join
    -> person t2 on t1.person_id = t2.id
    -> ) t3 inner join addr t4 on t3.addr_id = t4.id where t4.name in ('hubei', 'hunan')\G
*************************** 1. row ***************************
Plan:
SlothProject(sales_id=[$1], desc=[$2], person_name=[$3], addr_name=[$5]): rowcount = 5625.0, cumulative cost = {8175.0 rows, 3298.0 cpu, 0.0 io}, id = 337
  SlothJoin(condition=[=($0, $4)], joinType=[inner]): rowcount = 5625.0, cumulative cost = {7612.5 rows, 1048.0 cpu, 0.0 io}, id = 336
    SlothProject(addr_id=[$2], sales_id=[$0], desc=[$3], person_name=[$5]): rowcount = 1500.0, cumulative cost = {1860.0 rows, 842.0 cpu, 0.0 io}, id = 333
      SlothJoin(condition=[=($1, $4)], joinType=[inner]): rowcount = 1500.0, cumulative cost = {1710.0 rows, 242.0 cpu, 0.0 io}, id = 332
        SlothProject(id=[$0], person_id=[$1], addr_id=[$2], desc=[$4]): rowcount = 100.0, cumulative cost = {110.0 rows, 141.0 cpu, 0.0 io}, id = 331
          SlothTableScan(table=[[db1, sales]]): rowcount = 100.0, cumulative cost = {100.0 rows, 101.0 cpu, 0.0 io}, id = 22
        SlothTableScan(table=[[db1, person]]): rowcount = 100.0, cumulative cost = {100.0 rows, 101.0 cpu, 0.0 io}, id = 24
    SlothProject(id=[$0], name=[$1]): rowcount = 25.0, cumulative cost = {127.5 rows, 206.0 cpu, 0.0 io}, id = 335
      SlothFilter(condition=[OR(=($1, 'hubei'), =($1, 'hunan'))]): rowcount = 25.0, cumulative cost = {125.0 rows, 201.0 cpu, 0.0 io}, id = 334
        SlothTableScan(table=[[db1, addr]]): rowcount = 100.0, cumulative cost = {100.0 rows, 101.0 cpu, 0.0 io}, id = 27

1 row in set (1.28 sec)

mysql> explain select * from personn where name like 'xx%'\G
ERROR 1064 (HY000): You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'select * from personn where name like 'xx%''
mysql> explain select * from person where name like 'xx%'\G
*************************** 1. row ***************************
Plan:
SlothFilter(condition=[LIKE($1, 'xx%')]): rowcount = 25.0, cumulative cost = {125.0 rows, 201.0 cpu, 0.0 io}, id = 361
  SlothTableScan(table=[[db1, person]]): rowcount = 100.0, cumulative cost = {100.0 rows, 101.0 cpu, 0.0 io}, id = 342

1 row in set (0.06 sec)

mysql> select 1 + 1, 2 * 2 + 1, 102 * 100;
+-------+-----------+-----------+
| 1 + 1 | 2 * 2 + 1 | 102 * 100 |
+-------+-----------+-----------+
| 2     | 5         | 10200     |
+-------+-----------+-----------+
1 row in set (0.16 sec)

mysql> explain select t1.id, t1.money, t1.desc, t2.name from sales t1 right join person t2 on t1.person_id = t2.id limit 2\G
*************************** 1. row ***************************
Plan:
SlothProject(id=[$0], money=[$2], desc=[$3], name=[$5]): rowcount = 2.0, cumulative cost = {244.2 rows, 306.8 cpu, 0.0 io}, id = 167
  SlothSort(fetch=[2]): rowcount = 2.0, cumulative cost = {244.0 rows, 306.0 cpu, 0.0 io}, id = 166
    SlothJoin(condition=[=($1, $4)], joinType=[right]): rowcount = 30.0, cumulative cost = {242.0 rows, 258.0 cpu, 0.0 io}, id = 165
      SlothProject(id=[$0], person_id=[$1], money=[$3], desc=[$4]): rowcount = 100.0, cumulative cost = {110.0 rows, 141.0 cpu, 0.0 io}, id = 163
        SlothTableScan(table=[[db1, sales]]): rowcount = 100.0, cumulative cost = {100.0 rows, 101.0 cpu, 0.0 io}, id = 129
      SlothSort(fetch=[2]): rowcount = 2.0, cumulative cost = {102.0 rows, 117.0 cpu, 0.0 io}, id = 164
        SlothTableScan(table=[[db1, person]]): rowcount = 100.0, cumulative cost = {100.0 rows, 101.0 cpu, 0.0 io}, id = 133

1 row in set (0.39 sec)

mysql> select t1.id, t1.money, t1.desc, t2.name from sales t1 right join person t2 on t1.person_id = t2.id limit 2\G
*************************** 1. row ***************************
   id: NULL
money: NULL
 desc: NULL
 name: hello
*************************** 2. row ***************************
   id: 101
money: 32.5
 desc: lisi
 name: good
2 rows in set (0.62 sec)

mysql> select t1.id, t1.money, t1.desc, t2.name from sales t1 right join person t2 on t1.person_id = t2.id order by money desc limit 3\G
*************************** 1. row ***************************
   id: 101
money: 32.5
 desc: lisi
 name: good
*************************** 2. row ***************************
   id: 100
money: 25.5
 desc: zhangsan
 name: nice
*************************** 3. row ***************************
   id: NULL
money: NULL
 desc: NULL
 name: hello
3 rows in set (0.21 sec)

mysql> explain select t1.id, t1.money, t1.desc, t2.name from sales t1 right join person t2 on t1.person_id = t2.id where money > 30 order by money desc limit 3\G
*************************** 1. row ***************************
Plan:
SlothProject(id=[$0], money=[$2], desc=[$3], name=[$5]): rowcount = 3.0, cumulative cost = {1008.3 rows, 402.3000847841039 cpu, 0.0 io}, id = 494
  SlothSort(sort0=[$2], dir0=[DESC], fetch=[3]): rowcount = 3.0, cumulative cost = {1008.0 rows, 401.1000847841039 cpu, 0.0 io}, id = 493
    SlothJoin(condition=[=($1, $4)], joinType=[inner]): rowcount = 750.0, cumulative cost = {1005.0 rows, 322.0 cpu, 0.0 io}, id = 492
      SlothProject(id=[$0], person_id=[$1], money=[$3], desc=[$4]): rowcount = 50.0, cumulative cost = {155.0 rows, 221.0 cpu, 0.0 io}, id = 491
        SlothFilter(condition=[>($3, 30)]): rowcount = 50.0, cumulative cost = {150.0 rows, 201.0 cpu, 0.0 io}, id = 490
          SlothTableScan(table=[[db1, sales]]): rowcount = 100.0, cumulative cost = {100.0 rows, 101.0 cpu, 0.0 io}, id = 373
      SlothTableScan(table=[[db1, person]]): rowcount = 100.0, cumulative cost = {100.0 rows, 101.0 cpu, 0.0 io}, id = 377

1 row in set (0.25 sec)

mysql> select t1.id, t1.money, t1.desc, t2.name from sales t1 right join person t2 on t1.person_id = t2.id where money > 30 order by money desc limit 3\G
*************************** 1. row ***************************
   id: 101
money: 32.5
 desc: lisi
 name: good
1 row in set (0.15 sec)


mysql> use db2;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed

mysql> show tables;
+---------------+
| Tables_in_db2 |
+---------------+
| t1            |
+---------------+
1 row in set (0.00 sec)

mysql> select * from t1;
Empty set (0.63 sec)

mysql> insert into t1 values(1, 'hanngzhou'),(2, 'wuhan'),(3, 'nanjing'),(4, 'jiujiang');
Query OK, 4 rows affected (0.15 sec)

mysql> select * from t1;
+------+-----------+
| id   | name      |
+------+-----------+
|    1 | hanngzhou |
|    2 | wuhan     |
|    3 | nanjing   |
|    4 | jiujiang  |
+------+-----------+
4 rows in set (0.05 sec)

mysql> select * from t1 where id > 2;
+------+----------+
| id   | name     |
+------+----------+
|    3 | nanjing  |
|    4 | jiujiang |
+------+----------+
2 rows in set (0.06 sec)

mysql> explain select * from t1 where id > 2;
+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Plan                                                                                                                                                                                                                                 |
+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
|
SlothFilter(condition=[>($0, 2)]): rowcount = 50.0, cumulative cost = {150.0 rows, 201.0 cpu, 0.0 io}, id = 138
  SlothTableScan(table=[[db2, t1]]): rowcount = 100.0, cumulative cost = {100.0 rows, 101.0 cpu, 0.0 io}, id = 133
 |
+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
1 row in set (0.01 sec)

mysql>
```

 


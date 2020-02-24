select count(*) from yuqi.t1 a inner join csv.t1 b on a.int_type = b.int_type
select * from yuqi.t1 a inner join csv.t1 b on a.int_type = b.int_type
select * from yuqi.t1 a inner join csv.t1 b on a.int_type = b.int_type where a.smallint_type < 5
select * from yuqi.t1 a inner join csv.t1 b on a.int_type = b.int_type where a.smallint_type is not null

-- Single table select
## yuqi.t1
select tinyint_type from yuqi.t1
select smallint_type from yuqi.t1
select int_type from yuqi.t1
select bigint_type from yuqi.t1
select varchar_type from yuqi.t1
select float_type from yuqi.t1
select double_type from yuqi.t1

select * from yuqi.t1
select count(*) from yuqi.t1

select max(tinyint_type) from yuqi.t1
select max(smallint_type) from yuqi.t1
select max(int_type) from yuqi.t1
select max(bigint_type) from yuqi.t1
select max(varchar_type) from yuqi.t1
select max(float_type) from yuqi.t1
select max(double_type) from yuqi.t1

select min(tinyint_type) from yuqi.t1
select min(smallint_type) from yuqi.t1
select min(int_type) from yuqi.t1
select min(bigint_type) from yuqi.t1
select min(varchar_type) from yuqi.t1
select min(float_type) from yuqi.t1
select min(double_type) from yuqi.t1

## csv.t1
select tinyint_type from csv.t1
select smallint_type from csv.t1
select int_type from csv.t1
select bigint_type from csv.t1
select varchar_type from csv.t1
select float_type from csv.t1
select double_type from csv.t1

select * from csv.t1
select count(*) from csv.t1

select max(tinyint_type) from csv.t1
select max(smallint_type) from csv.t1
select max(int_type) from csv.t1
select max(bigint_type) from csv.t1
select max(varchar_type) from csv.t1
select max(float_type) from csv.t1
select max(double_type) from csv.t1

select min(tinyint_type) from csv.t1
select min(smallint_type) from csv.t1
select min(int_type) from csv.t1
select min(bigint_type) from csv.t1
select min(varchar_type) from csv.t1
select min(float_type) from csv.t1
select min(double_type) from csv.t1

## json.t1
select tinyint_type from json.t1
select smallint_type from json.t1
select int_type from json.t1
select bigint_type from json.t1
select varchar_type from json.t1
select float_type from json.t1
select double_type from json.t1

select * from json.t1
select count(*) from json.t1

select max(tinyint_type) from json.t1
select max(smallint_type) from json.t1
select max(int_type) from json.t1
select max(bigint_type) from json.t1
select max(varchar_type) from json.t1
select max(float_type) from json.t1
select max(double_type) from json.t1

select min(tinyint_type) from json.t1
select min(smallint_type) from json.t1
select min(int_type) from json.t1
select min(bigint_type) from json.t1
select min(varchar_type) from json.t1
select min(float_type) from json.t1
select min(double_type) from json.t1


## Join operation


select count(*) from yuqi.t1 a inner join csv.t1 b on a.int_type = b.int_type
select * from yuqi.t1 a inner join csv.t1 b on a.tinyint_type = b.tinyint_type
select * from yuqi.t1 a inner join csv.t1 b on a.smallint_type = b.smallint_type
select * from yuqi.t1 a inner join csv.t1 b on a.int_type = b.int_type
select * from yuqi.t1 a inner join csv.t1 b on a.bigint_type = b.bigint_type
select * from yuqi.t1 a inner join csv.t1 b on a.varchar_type = b.varchar_type
select * from yuqi.t1 a inner join csv.t1 b on a.float_type = b.float_type
select * from yuqi.t1 a inner join csv.t1 b on a.double_type = b.double_type
select * from yuqi.t1 a inner join csv.t1 b on a.int_type = b.int_type where a.smallint_type < 5
select * from yuqi.t1 a inner join csv.t1 b on a.int_type = b.int_type where a.smallint_type is not null


select count(*) from yuqi.t1 a inner join json.t1 b on a.int_type = b.int_type
select * from yuqi.t1 a inner join json.t1 b on a.int_type = b.int_type
select * from yuqi.t1 a inner join json.t1 b on a.tinyint_type = b.tinyint_type
select * from yuqi.t1 a inner join json.t1 b on a.smallint_type = b.smallint_type
select * from yuqi.t1 a inner join json.t1 b on a.int_type = b.int_type
select * from yuqi.t1 a inner join json.t1 b on a.bigint_type = b.bigint_type
select * from yuqi.t1 a inner join json.t1 b on a.varchar_type = b.varchar_type
select * from yuqi.t1 a inner join json.t1 b on a.float_type = b.float_type
select * from yuqi.t1 a inner join json.t1 b on a.double_type = b.double_type
select * from yuqi.t1 a inner join json.t1 b on a.int_type = b.int_type where a.smallint_type < 5
select * from yuqi.t1 a inner join json.t1 b on a.int_type = b.int_type where a.smallint_type is not null

select count(*) from csv.t1 a inner join json.t1 b on a.int_type = b.int_type
select * from csv.t1 a inner join json.t1 b on a.tinyint_type = b.tinyint_type
select * from csv.t1 a inner join json.t1 b on a.smallint_type = b.smallint_type
select * from csv.t1 a inner join json.t1 b on a.int_type = b.int_type
select * from csv.t1 a inner join json.t1 b on a.bigint_type = b.bigint_type
select * from csv.t1 a inner join json.t1 b on a.varchar_type = b.varchar_type
select * from csv.t1 a inner join json.t1 b on a.float_type = b.float_type
select * from csv.t1 a inner join json.t1 b on a.double_type = b.double_type
select * from csv.t1 a inner join json.t1 b on a.int_type = b.int_type where a.smallint_type < 5
select * from csv.t1 a inner join json.t1 b on a.int_type = b.int_type where a.smallint_type is not null

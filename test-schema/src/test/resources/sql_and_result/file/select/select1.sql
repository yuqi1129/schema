select * from csv.t1 where tinyint_type = 1 and smallint_type = 1 and int_type = 21 and bigint_type = 4 limit 1
select int_type from csv.t1 where tinyint_type is null
select count(*) from csv.t1 where tinyint_type is not null
select count(*) from csv.t1 where smallint_type is not null
select count(*) from csv.t1 where int_type is not null
select count(*) from csv.t1 where bigint_type is not null
select count(*) from csv.t1 where char_type is not null
select count(*) from csv.t1 where varchar_type is not null
select count(*) from csv.t1 where float_type is not null
select count(*) from csv.t1 where double_type is not null
select count(*) from csv.t1
-- this is comment
select * from csv.t1 where tinyint_type is null


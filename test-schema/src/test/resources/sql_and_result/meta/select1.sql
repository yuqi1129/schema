-- cast should add alias name
select int_type inttype, smallint_type as s, tinyint_type LL, cast(bigint_type as char) v from yuqi.t1
-- count(*) should add alias
select count(*) from yuqi.t1

-- error
select a.*, b.int_type from csv.t1 a inner join yuqi.t1 b on a.int_type = b.int_type
select a.float_type, b.int_type from csv.t1 a inner join yuqi.t1 b on a.int_type = b.int_type
select a.int_type, b.varchar_type from csv.t1 a inner join yuqi.t1 b on a.int_type = b.int_type where a.int_type > 1

select 1 from yuqi.t1

#bugy
#select int_type + 1 from yuqi.t1
#select (int_type / 5) b from json.t1
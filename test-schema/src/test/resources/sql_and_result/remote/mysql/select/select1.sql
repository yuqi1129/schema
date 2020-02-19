select * from yuqi.t1
select count(*) from yuqi.t1
select a.*, b.int_type from csv.t1 a inner join yuqi.t1 b on a.int_type = b.int_type
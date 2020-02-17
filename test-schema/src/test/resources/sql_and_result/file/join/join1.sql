-- select a.smallint_type, b.int_type from csv.t1 a inner join yuqi.type b on a.smallint_type = b.smallint_type
select a.smallint_type, b.int_type from csv.t1 a inner join yuqi.type b on a.int_type = b.int_type

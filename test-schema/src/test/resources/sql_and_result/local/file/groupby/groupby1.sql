select count(*) c, smallint_type from csv.t1 group by smallint_type
select count(*) c, smallint_type from csv.t1 group by smallint_type having count(*) >= 2


select sum(smallint_type) from csv.t1
select sum(smallint_type), tinyint_type from csv.t1 group by tinyint_type


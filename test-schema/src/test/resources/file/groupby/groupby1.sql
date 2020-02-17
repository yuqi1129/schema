select count(*) c, smallint_type from csv.t1 group by smallint_type
select count(*) c, smallint_type from csv.t1 group by smallint_type having count(*) > 2
select count(*) c, smallint_type from csv.t1 where smallint_type is not null group by smallint_type


select sum(smallint_type) from csv.t1
select sum(tinyint_type), tinyint_type from csv.t1 group by tinyint_type


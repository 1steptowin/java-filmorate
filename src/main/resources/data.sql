insert into rating select * from (
select 1, 'G' union
select 2, 'PG' union
select 3, 'PG13' union
select 4, 'R' union
select 5, 'NC17'
) x where not exists(select * from rating);

insert into genre select * from (
select 1, 'COMEDY' union
select 2, 'DRAMA' union
select 3, 'CARTOON' union
select 4, 'THRILLER' union
select 5, 'DOCUMENTARY' union
select 6, 'ACTION'
) x where not exists (select * from genre);
/*create table car*/
create table car(
	brand varchar(255),
	model varchar(255),
	year int
);


/*display all rows*/
select* from car;


/*insert single row*/
insert into car(brand,model,year)
values('Ford','Mustang',1964);


/*insert multiple rows*/
insert into car(brand,model,year)
values('Volvo','p1800',1968),
	('BMW','M1',1978),
	('Toyota','Celica',1975);


/*select specific column*/
select brand,year from car;


/*alter table and add columns*/
alter table car
add color varchar(255);


/*update values*/
UPDATE car
SET color = 'red'
WHERE brand = 'Volvo';


/*update multiple columns*/
UPDATE car
SET color = 'white', year = 1970
WHERE brand = 'Toyota';


/*alter column type*/
ALTER TABLE car
ALTER COLUMN year TYPE VARCHAR(4);


/*drop column*/
ALTER TABLE car
DROP COLUMN color;


/*delete single record*/
DELETE FROM cars
WHERE brand = 'Volvo';


/*delete all record*/
DELETE FROM cars;

/*delete all records instantly*/
TRUNCATE TABLE car;


/*delete complete table*/
DROP TABLE car;


/*unique value*/
SELECT DISTINCT brand from car;


/*count distinct values*/
SELECT COUNT(DISTINCT brand) FROM car;


/*sort values*/
SELECT * FROM car
ORDER BY brand;

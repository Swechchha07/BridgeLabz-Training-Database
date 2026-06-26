create table employees(
	emp_id serial primary key,
	emp_name varchar(50),
	department varchar(50),
	salary int,
	city varchar(50)
);


/*find max salary a/c to department*/
select max(salary) from employees
group by department;



/*find min salary a/c to department*/
select min(salary) from employees
group by department;


/*display department having more than 1 employee*/
select department from employees
group by department
having count(*)>1;


/*update aman's salary*/
update employees
set salary=55000
where emp_name='Aman';


/*increase hr department salary*/
update employees
set salary=5000
where department='HR';


/*update rahul city*/
update employees
set city='Bangalore'
where emp_name='Rahul;


/*delete finance department employee*/
delete from employees
where department='Finance';


/*delete whose salary < 45000*/
delete from employees
where salary<45000;


/*display whose name start with A*/
select emp_name from employees
where emp_name like 'A%';


/*display whose name end with A*/
select emp_name from employees
where emp_name like '%a';


/*display name in which h is present*/
select emp_name from employees
where emp_name like '%h%';


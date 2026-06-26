/*create table*/

create table employees(
	emp_id serial primary key,
	emp_name varchar(50),
	department varchar(50),
	salary int,
	city varchar(50)
);

/*insert values*/
insert into employees(emp_name,department,salary,city)
values('Aman', 'IT', 50000, 'Delhi'),
('Riya', 'HR', 40000, 'Mumbai'),
('Mohit', 'IT', 60000, 'Delhi'),
('Neha', 'Finance', 55000, 'Pune'),
('Rahul', 'HR', 45000, 'Mumbai');


/*display all record*/
select * from employees;


/*display only names*/
select emp_name from employees;


/*display it deartment employees*/
select emp_name from employees 
where department='IT';


/*display employees whose salary is >50000*/
select emp_name from employees
where salary>50000;


/*display employees from mumbai*/
select emp_name from employees
where city='Mumbai';


/*display emp_name aur salary*/
select emp_name, salary from employees;


/*display salary in desc order*/
select salary from employees 
order by salary desc;


/*sort employees according to name*/
select * from employees
order by emp_name;



/*how many emp in IT department*/
select count(emp_name) from employees
where department='IT';


/*find max salary*/
select max(salary) from employees;

/*count total employee*/
select count(emp_name) from employees;


/*find avg salary*/
select avg(salary) from employees;


/*find min salary*/
select min(salary) from employees;


/*find avg salary of each department*/
select department,avg(salary) from employees
group by department;


/*find total expenditure*/
select sum(salary) as total_expenditure
from employees;


/*how many employees in each department*/
select count(emp_name) from employees
group by department;



/*count employee in city*/
select count(emp_name) from employees
group by city;










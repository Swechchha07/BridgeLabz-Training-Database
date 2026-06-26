/*create department table*/
CREATE TABLE departments(
    dept_id SERIAL PRIMARY KEY,
    dept_name VARCHAR(50)
);


/*create workers table*/

CREATE TABLE workers(
    worker_id SERIAL PRIMARY KEY,
    worker_name VARCHAR(50),
    dept_id INT REFERENCES departments(dept_id)
);


/*display worker aur department name using inner join*/
select w.worker_name, d.dept_name from workers w
inner join departments d
on w.dept_id=d.dept_id;


/*display all workers using left join*/
select w.worker_name,d.dept_name from workers w
left join departments d
on w.dept_id=d.dept_id;



/*display all departments using right join*/
select w.worker_name, d.dept_name from workers w
right join departments d
on w.dept_id=d.dept_id;


/*show IT department worker*/
select w.worker_name from workers w
inner join departments d
on w.dept_id=d.dept_id
where d.dept_name='IT';



/*display highest salary employee using subquery*/
select emp_name from employees
where salary=(
	select max(salary) from employees
);



/*display employees whose salary is more than avg using subquery*/
select * from employees
where salary>(
select avg(salary) from employees);


/*find second highest salary*/
select max(salary) from employees
where salary<(
select max(salary) from employees);


/*another method to find second highest salary*/
select distinct salary from employees
order by salary desc
limit 1 offset 1;



/*Top 3 highest salary employees*/
select * from employees
order by salary desc
limit 3;


/*find duplicate records*/
select emp_name,count(*)
from employees
group by emp_name
having count(*)>1;


/*find third highest salary*/
select distinct salary from employees
order by salary desc
limit 1 offset 2;


/*display employees whose salary equal to max salary*/
select * from employees
where salary=(
select max(salary) from employees);


/*display highest paid employee of each department*/
select e1.* from employees e1
where salary=(
	select max(e2.salary) from employees e2
	where e1.department=e2.department
);
/*create department table*/
CREATE TABLE department (
    dept_id INT PRIMARY KEY,
    dept_name VARCHAR(50)
);


/*insert values*/
INSERT INTO department VALUES
(1, 'HR'),
(2, 'IT'),
(3, 'Finance'),
(4, 'Marketing');


/*create employee table*/
CREATE TABLE employee (
    emp_id INT PRIMARY KEY,
    emp_name VARCHAR(50),
    salary INT,
    dept_id INT
);


/*insert values*/
INSERT INTO employee VALUES
(101, 'Aman', 50000, 1),
(102, 'Riya', 60000, 2),
(103, 'Karan', 55000, 2),
(104, 'Neha', 45000, 3),
(105, 'Rahul', 70000, NULL),
(106, 'Priya', 65000, 5);


/*display Employee name and department name*/
select e.emp_name,d.dept_name from employee e
inner join department d
on e.dept_id=d.dept_id;



/*display only IT department employee*/
select * from employee e
inner join department d
on e.dept_id=d.dept_id
where dept_name='IT';


/*display name,salary and department*/
select e.emp_name,e.salary,d.dept_name from employee e
inner join department d
on e.dept_id=d.dept_id;


/*display employee and department whose salary more than 55000*/
select e.emp_name, e.salary, d.dept_name from employee e
inner join department d
on e.dept_id=d.dept_id
where salary>55000;



/*display HR department employee*/
select e.emp_name from employee e
inner join department d
on e.dept_id=d.dept_id
where dept_name='HR';



/*display all employee*/
select e.emp_name,d.dept_name from employee e
left join department d
on e.dept_id=d.dept_id;

/*display all employee whose department not assigned*/
select e.emp_name from employee e
left join department d
on e.dept_id=d.dept_id
where d.dept_id is null;




/*if department is null then show not assigned*/
select e.emp_name ,
coalesce(d.dept_name,'Not Assigned') as department
from employee e
left join department d
on e.dept_id=d.dept_id;


/*show all departments*/
select e.emp_name,d.dept_name from employee e
right join department d
on e.dept_id=d.dept_id;


/*show department in which no employee*/
select d.dept_name from employee e
right join department d
on e.dept_id=d.dept_id
where e.emp_id is null;



/*display all department and employee*/
select e.emp_name,d.dept_name from employee e
full join department d
on e.dept_id=d.dept_id;


/*display unmatched employee and unmatched department*/
select e.emp_name,d.dept_name from employee e
full join department d
on e.dept_id=d.dept_id
where e.emp_id is null
or d.dept_id is null;



/*how many employee in department*/
select d.dept_name,count(e.emp_id) as total_employee
from department d
left join employee e
on e.dept_id=d.dept_id
group by d.dept_name;



/*max employee department*/
select d.dept_name,count(e.emp_id) as total_employee
from department d
left join employee e
on e.dept_id=d.dept_id
group by d.dept_name
order by total_employee desc limit 1;




/*Department-wise average salary*/
select d.dept_name,avg(e.salary) as avg_salary
from department d
left join employee e
on d.dept_id=e.dept_id
group by dept_name;



/*employee and whose department averagee salary*/
select e.emp_name,e.salary,d.dept_name,
avg(e.salary) over(partition by e.dept_id) as avg_salary
from employee e
join department d
on e.dept_id=d.dept_id;








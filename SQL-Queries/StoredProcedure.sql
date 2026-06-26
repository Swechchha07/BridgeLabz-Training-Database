/*create stored procedure*/

create or replace procedure add_employee(
	p_id int,
	p_name varchar(50),
	p_salary numeric
)
language plpgsql
as $$
begin
	insert into employee
	values(p_id,p_name,p_salary);
end;
$$;


/*call procedure*/
call add_employee(6,'Priya',50000);


/*procedure with update*/
create or replace  procedure update_salary(
	p_id int,
	p_salary numeric
)
language plpgsql
as $$
begin
	update employee
	set salary=p_salary
	where emp_id=p_id;
end;
$$;


/*call procedure*/
call update_salary(1,60000);


/*if condition in procedure*/
create or replace procedure check_salary(
	p_salary numeric
)
language plpgsql
as $$
begin
	if p_salary>50000 then
		raise notice 'High Salary';
	else
		raise notice 'Normal Salary';
	end if;
end;
$$;


/*call procedure*/
call check_salary(70000);
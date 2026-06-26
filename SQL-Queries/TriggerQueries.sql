/*trigger function*/
create or replace function check_salary()
returns trigger
as $$
begin
	if new.salary<0 then
		raise exception 'Salary cannot be negative';
	end if;
	return new;
end;
$$ language plpgsql;



/*create trigger*/
create trigger salary_trigger
before insert
on employee
for each row
execute function check_salary();


/*test trigger by inserting*/
insert into employee
values(1,'Rahul',50000);


insert into employee
values(2,'Aman',-1000);


/*create audit table for after trigger events*/
create table employee_audit(
	audit_id serial primary key,
	action varchar(20),
	action_time timestamp
);


/*create trigger function*/
create or replace function audit_employee()
returns trigger
as $$
begin
	insert into employee_audit(action,action_time)
	values('INSERT',NOW());

	return new;
end;
$$ language plpgsql;



/*create trigger*/
create trigger employee_insert_audit
after insert
on employee
for each row
execute function audit_employee();


/*test trigger*/
insert into employee
values(5,'Neha');


/*disable trigger*/
alter table employee
disable trigger salary_trigger;


/*enable trigger*/
alter table employee
enable trigger salary_trigger;


/*delete trigger*/
drop trigger salary_trigger
on employee;



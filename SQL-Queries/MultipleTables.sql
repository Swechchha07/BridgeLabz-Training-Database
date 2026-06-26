/*create author table*/
create table Authors(
	author_id Serial Primary Key,
	name varchar(100) not null,
	birth_year int,
	country varchar(100)
);


/*insert values into authors table*/
insert into Authors(name,birth_year,country)
values ('George Orwell', 1903, 'UK'),
	('J.K. Rowling', 1965, 'UK'),
	('Isaac Asimov', 1920, 'Russia'),
	('Mark Twain', 1835, 'USA'),
	('Harper Lee', 1926, 'USA');


/*create books table*/
create table Books(
	book_id serial primary key,
	title varchar(150) not null,
	author_id int references Authors(author_id),
	category varchar(50),
	published_year int,
	copies_available int
);


/*insert values in books table*/
insert into Books (title, author_id, category, published_year, copies_available)
values ('1984', 1, 'Dystopian', 1949, 5),
('Animal Farm', 1, 'Political Satire', 1945, 3),
('Harry Potter and the Philosopher''s Stone', 2, 'Fantasy', 1997, 7),
('Harry Potter and the Chamber of Secrets', 2, 'Fantasy', 1998, 6),
('Foundation', 3, 'Science Fiction', 1951, 4),
('The Adventures of Tom Sawyer', 4, 'Adventure', 1876, 8),
('To Kill a Mockingbird', 5, 'Fiction', 1960, 10);



/*create members table*/
create table Members(
	member_id serial primary key,
	name varchar(100) not null,
	email varchar(150) unique not null,
	membership_date date
);



/* Insert values into the Members table*/
INSERT INTO Members (name, email, membership_date)
VALUES
('Alice Johnson', 'alice.johnson@example.com', '2023-01-15'),
('Bob Smith', 'bob.smith@example.com', '2023-02-10'),
('Charlie Brown', 'charlie.brown@example.com', '2023-03-05'),
('Diana Prince', 'diana.prince@example.com', '2023-04-20'),
('Edward Stark', 'edward.stark@example.com', '2023-05-25');



/*create borrowing table*/
CREATE TABLE Borrowings (
    borrowing_id SERIAL PRIMARY KEY,
    book_id INT REFERENCES Books(book_id),
    member_id INT REFERENCES Members(member_id),
    borrowed_date DATE,
    return_date DATE
);



/*Insert values into the Borrowings table*/
INSERT INTO Borrowings (book_id, member_id, borrowed_date, return_date)
VALUES
(1, 1, '2023-07-10', '2023-07-20'),
(3, 2, '2023-06-15', '2023-06-25'),
(5, 3, '2023-08-05', NULL),
(7, 4, '2023-09-01', '2023-09-15'),
(2, 5, '2023-09-10', NULL);




/*Select all books from the database*/
select * from Books;


/*find title and category published in 2020*/
select title, category from Books
where published_year=2020;


/*List all authors from the USA.*/
select name from Authors
where country='USA';




/*Insert a new book into the Books table*/
INSERT INTO Books (book_id, title, author_id, category, published_year, copies_available)
VALUES (101, 'The Pragmatic Programmer', 1, 'Programming', 1999, 5);



/*Find all members who joined in the year 2023*/
select * from Members where extract(year from membership_date)=2023;

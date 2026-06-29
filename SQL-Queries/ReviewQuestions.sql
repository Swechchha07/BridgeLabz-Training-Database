CREATE TABLE locations (
    location_id SERIAL PRIMARY KEY,
    delivery_location VARCHAR(100)
);

CREATE TABLE inventory (
    inventory_id SERIAL PRIMARY KEY,
    product_name VARCHAR(250),
    number_of_supplies INT,
    delivery_location INT REFERENCES locations(location_id),
    order_id INT
);

INSERT INTO locations (delivery_location)
VALUES
('Delhi'),
('Mumbai'),
('Agra'),
('Kanpur'),
('Lucknow'),
('Noida'),
('Jaipur'),
('Pune'),
('Hyderabad'),
('Bengaluru');


INSERT INTO inventory
(product_name, number_of_supplies, delivery_location, order_id)
VALUES
('Laptop', 850, 1, 101),
('Mouse', 920, 2, 102),
('Keyboard', 1100, 3, 103),
('Monitor', 875, 4, 104),
('Printer', 980, 5, 105),
('Scanner', 1200, 6, 106),
('Speaker', 1050, 7, 107),
('Webcam', 890, 8, 108),
('Hard Disk', 1150, 9, 109),
('UPS', 1000, 10, 110);



SELECT inventory_id,product_name,number_of_supplies,
 RANK() OVER (ORDER BY number_of_supplies DESC) AS supply_rank
FROM inventory;



SELECT * FROM (SELECT inventory_id,product_name,number_of_supplies,
RANK() OVER (ORDER BY number_of_supplies DESC) AS supply_rank
FROM inventory
) AS ranked_inventory;



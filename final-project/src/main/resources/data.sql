insert into Product (`name`, price, stock) values ('water',5.00,5);
insert into Product (`name`, price, stock) values ('soda',2.00,20);
insert into Product (`name`, price, stock) values ('chocolate',3.00,25);
insert into Product (`name`, price, stock) values ('wings',8.00,12);

-- insert into Product values (1l,'Water',5.00,5);
-- insert into Product values (2l,'Soda',2.00,20);
-- insert into Product values (3l,'Chocolate',3.00,25);
-- insert into Product values (4l,'Wings',8.00,12);
--
--
-- insert into Users values (1l,'irahetajuanjose@hotmail.com','Juan','Iraheta','+503 7129 9991','Loading');
-- insert into Users values (2l,'irahetajuanjose@gmail.com','Jose','Martinez','+503 2288 3394','Sephirtod');
-- insert into Users values (3l,'test@gmail.com','tester','tester','+503 2385 3444','Tester');
insert into Users (email, user_Name, first_Name, last_Name, phone_Number) values ('irahetajuanjose@hotmail.com','Loading','Juan','Iraheta','+503 2288 3394');
insert into Users (email, user_Name, first_Name, last_Name, phone_Number) values ('irahetajuanjose@gmail.com','Sephirtod','Jose','Martinez','+503 2288 3394');
insert into Users (email, user_Name, first_Name, last_Name, phone_Number) values ('itest@gmail.com','tester','tester','tester','+503 2385 3444');
--
--
-- insert into Address values (1l,'12-2','10 calle ote','santa tecla','La Libertad',1);
-- insert into Address values (2l,'17','calle teotl','Antiguo Cuscatlan','San Salvador',1);
-- insert into Address values (3l,'20','AV yumuri','San Salvador','San Salvador',2);
insert into Address (house_Number, street, city, `state`, user_id) values ('12-2','10 calle ote','santa tecla','La Libertad',1);
insert into Address (house_Number, street, city, `state`, user_id) values ('17','calle teotl','Antiguo Cuscatlan','San Salvador',1);
insert into Address (house_Number, street, city, `state`, user_id) values ('20','av yumuri','san salvador','san salvador',2);
--
--
-- insert into Payment_Method values (1l,50.00,'Debit Card',1);
-- insert into Payment_Method values (2l,100.00,'Credit Card',1);
insert into Payment_Method (founds, payment_Type, user_id) values (5.00,'Debit Card',1);
insert into Payment_Method (founds, payment_Type, user_id) values (100.00,'Credit Card',1);



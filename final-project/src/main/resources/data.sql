insert into Product (`name`, price, stock) values ('water',5.00,5);
insert into Product (`name`, price, stock) values ('soda',2.00,20);
insert into Product (`name`, price, stock) values ('chocolate',3.00,25);
insert into Product (`name`, price, stock) values ('wings',8.00,12);

insert into Users (email, user_Name, first_Name, last_Name, phone_Number) values ('irahetajuanjose@hotmail.com','loading','Juan','Iraheta','+503 2288 3394');
insert into Users (email, user_Name, first_Name, last_Name, phone_Number) values ('test@gmail.com','tester','Jose','Martinez','+503 2288 3394');

insert into Address (house_Number, street, city, `state`, user_id) values ('12-2','10 calle ote','santa tecla','La Libertad',1);
insert into Address (house_Number, street, city, `state`, user_id) values ('17','calle teotl','Antiguo Cuscatlan','San Salvador',1);


insert into Payment_Method (`name`,founds, payment_Type, user_id) values ('my debit card',5.00,'Debit Card',1);
insert into Payment_Method (`name`,founds, payment_Type, user_id) values ('my credit card',100.00,'Credit Card',1);



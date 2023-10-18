delete from visits where 1 = 1;
delete from persons where 1 = 1;

insert into persons(id, email, name, dtype) values(123, 'vasya@gmail.com', 'Vasya', 'Doctor');
insert into persons(id, email, name, dtype) values(124, 'petya@gmail.com', 'Petya', 'Doctor');
insert into persons(id, email, name, dtype) values(125, 'olya@gmail.com', 'Olya', 'Patient');
insert into persons(id, email, name, dtype) values(126, 'olya@gmail.com', 'Olya', 'Patient');
insert into visits(doctor_id, patient_id, date) values(123, 125, '2023-10-10');
insert into visits(doctor_id, patient_id, date) values(124, 125, '2023-10-12');
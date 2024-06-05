CREATE TABLE cliente (
codigo bigint(20) primary key auto_increment,
nome varchar(50) NOT NULL)
ENGINE=InnoDB default charset=utf8;

insert into cliente(nome) 
values 
('JOSE'),
('CLAUDIO'),
('VLADIMIR'),
('DAVID'),
('ADMIN');
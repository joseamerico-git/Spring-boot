CREATE TABLE categoria (
codigo bigint(20) primary key auto_increment,
nome varchar(50) NOT NULL
)
ENGINE=InnoDB default charset=utf8;

insert into categoria(nome) 
values 
('Lazer'),
('Alimentação'),
('Supermercado'),
('Farmacia'),
('Outros');
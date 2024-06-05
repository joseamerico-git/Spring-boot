
CREATE TABLE PESSOA (
codigo bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
nome varchar(50)not null,
ativo boolean not null,
logradouro varchar(50),
numero varchar(5),
complemento varchar(20),
bairro varchar(50),
cep varchar(20),
cidade varchar(50),
estado varchar(5))
ENGINE=InnoDB default charset=utf8;

INSERT INTO pessoa 
(nome,ativo,logradouro,numero,complemento,bairro,cep,cidade,estado)
values
('Juninho',true,'Rua NOVEMBR','10','CASA','CENTRO','1980456','ASSIS','SP'),
('Sebastião',true,'Rua NOVEMBR','10','CASA','CENTRO','1980456','ASSIS','SP'),
('Fernando',true,'Rua NOVEMBR','10','CASA','CENTRO','1980456','ASSIS','SP'),
('David Jose',true,'Rua NOVEMBR','10','CASA','CENTRO','1980456','ASSIS','SP'),
('Joaquim',true,'Rua NOVEMBR','10','CASA','CENTRO','1980456','ASSIS','SP'),
('João Pedro',true,'Rua NOVEMBR','10','CASA','CENTRO','1980456','ASSIS','SP');
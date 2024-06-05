create table lancamento(
codigo bigint(20) primary key auto_increment,
descricao varchar(50) not null,
data_vencimento date not null,
data_pagamento date,
valor decimal(10,2) not null,
observacao varchar(100),
tipo varchar(20) not null,
codigo_categoria bigint(20) not null,
codigo_pessoa bigint(20) not null,
foreign key(codigo_categoria) references categoria(codigo),
foreign key(codigo_pessoa) references pessoa(codigo))
ENGINE=InnoDB default charset=utf8;

insert into lancamento(descricao,data_vencimento,data_pagamento,valor,observacao,tipo,codigo_categoria,codigo_pessoa)values
("Salário",'2021-08-09','2021-08-09',5000,"Pagamento extra funcionário do mês","DESPESA",1,1),
("Recebimento cliente",'2021-08-09','2021-08-09',5000,"Parcela do carnê","RECEITA",2,1),
("Mercado",'2021-08-09','2021-08-09',5000,"Pagamento extra funcionário do mês","RECEITA",3,1);

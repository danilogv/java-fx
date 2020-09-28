CREATE DATABASE Java_FX CHARSET = utf8 COLLATE = utf8_general_ci;

USE Java_FX;

CREATE TABLE Departamento (
	id int auto_increment,
	nome varchar(50) not null,
	unique key nome_unique(nome),
	primary key(id)
) ENGINE = INNODB;

CREATE TABLE Vendedor (
	id int auto_increment,
	nome varchar(50) not null,
	email varchar(50) not null,
	data_nascimento date not null,
	salario decimal(10,2) not null,
	departamento_id int not null,
	primary key(id),
	unique key nome_unique(nome),
	unique key email_unique(email),
	foreign key(departamento_id) references Departamento(id) on delete cascade
) ENGINE = INNODB;
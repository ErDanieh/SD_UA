CREATE DATABASE SD_MYSQL;

USE SD_MYSQL;

CREATE TABLE Usuarios
(
idUsuario INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
nombre VARCHAR (25),
ciudad VARCHAR (25),
correo VARCHAR (25)
);

INSERT INTO Usuarios (nombre,ciudad,correo) VALUES ('María López','Alicante','mlopez@miempresa.es');
INSERT INTO Usuarios (nombre,ciudad,correo) VALUES ('Juan Fernández','Alicante','jfernandez@miempresa.es');
INSERT INTO Usuarios (nombre,ciudad,correo) VALUES ('Lucía Martínez','Madrid','lmartinez@miempresa.es');
INSERT INTO Usuarios (nombre,ciudad,correo) VALUES ('José Luis Gutiérrez','Alicante','jgutierrez@miempresa.es');
INSERT INTO Usuarios (nombre,ciudad,correo) VALUES ('Francisco García','Barcelona','fgarciaUsuarios@miempresa.es');

COMMIT;
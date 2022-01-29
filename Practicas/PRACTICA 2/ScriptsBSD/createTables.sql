
CREATE TABLE `Mapa` (
	`ID` INT NOT NULL AUTO_INCREMENT,
	`Nombre` VARCHAR(20) NOT NULL,
	`AforoMax` INT NOT NULL DEFAULT '10',
	`abierto` BOOLEAN NOT NULL DEFAULT false,
	PRIMARY KEY (`ID`)
);


CREATE TABLE `Casillas` (
	`fila` INT,
	`columna` INT,
	`idMapa` INT,
	PRIMARY KEY (`fila`, `columna`, `idMapa`),
	FOREIGN KEY (`idMapa`) REFERENCES Mapa(ID)
);


CREATE TABLE `Atracciones` (
	`ID` INT NOT NULL AUTO_INCREMENT,
	`Nombre` VARCHAR(20) DEFAULT 'Atraccion',
	`nTurno` INT DEFAULT '5',
	`nCola` INT DEFAULT '0',
	`tTurno` INT,
	`posFila` INT,
	`posColumna` INT,
	PRIMARY KEY (`ID`),
	FOREIGN KEY (`posFila`, `posColumna`) REFERENCES Casillas(fila, columna)
);


CREATE TABLE `Sensores` (
	`ID` INT NOT NULL AUTO_INCREMENT,
	`idAtraccion` INT,
	PRIMARY KEY (`ID`),
	FOREIGN KEY (`idAtraccion`) REFERENCES Atracciones(ID)
);

CREATE TABLE `Visitantes` (
	`ID` INT NOT NULL AUTO_INCREMENT,
	`Nombre` VARCHAR(20) NOT NULL,
	`Password` VARCHAR(20) NOT NULL,
	`enParque` BOOLEAN NOT NULL DEFAULT false,
	`posFila` INT,
	`posColumna` INT,
	PRIMARY KEY (`ID`),
	FOREIGN KEY (`posFila`, `posColumna`) REFERENCES Casillas(fila, columna)

);


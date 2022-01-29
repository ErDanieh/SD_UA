// Dependencias necesarias
const express = require("express");
const mysql = require ("mysql");
const bodyParser = require("body-parser");

// Se define el puerto
const port=3000;

// Se crea instancia de express
const appSD = express();

appSD.use(bodyParser.json());

// Configuración de la conexión a la base de datos MySQL
const connection = mysql.createConnection({
    host: 'localhost',
    user:'root',
    password: 'root',
    database:'SD_MYSQL'
});

// Comprobar conexión a la base de datos
connection.connect(error=> {
    if (error) throw error;
    console.log('Conexión a la base de datos SD_MYSQL correcta');
});


// Rutas de la aplicación
// Página por defecto
appSD.get("/",(request, response) => {
response.json({message:'Página de inicio de aplicación de ejemplo de SD'})
});


// Listado de todos los usuarios
appSD.get("/usuarios",(request, response) => {
    console.log('Listado de todos los usuarios');
    
    const sql = 'SELECT * FROM Usuarios';
    connection.query(sql,(error,resultado)=>{
        if (error) throw error;
        if (resultado.length > 0){
            response.json(resultado);
        } else {
            response.send('No hay resultados');
        }
    });
});


// Añadir un nuevo usuario
appSD.post("/usuarios",(request, response) => {
    console.log('Añadir nuevo usuario');
    const sql = 'INSERT INTO Usuarios SET ?';
    
        const usuarioObj = {
            nombre: request.body.nombre,
            ciudad: request.body.ciudad,
            correo: request.body.correo
        }
        connection.query(sql,usuarioObj,error => {
        if (error) throw error;
        response.send('Usuario creado');
        }); 
});
 

// Modificar un usuario
appSD.put("/usuarios/:id",(request, response) => {
    console.log('Modificar usuario');
    
    const {id} = request.params;
    const {nombre,ciudad,correo} = request.body;
    const sql = `UPDATE Usuarios SET nombre='${nombre}', ciudad='${ciudad}', correo='${correo}' WHERE idUsuario=${id}`;
    connection.query(sql,error => {
        if (error) throw error;
        response.send('Usuario modificado');
    }); 
});
    

// Borrar un usuario
appSD.delete("/usuarios/:id",(request, response) => {
    console.log('Borrar usuario');

    const {id} = request.params;
    sql = `DELETE FROM Usuarios WHERE idUsuario= ${id}`;
    connection.query(sql,error => {
        if (error) throw error;
        response.send('Usuario borrado');
    }); 
});


// Obtener datos de un usuarios
appSD.get("/usuarios/:id",(request, response) => {
    console.log('Obtener datos de un usuario');

    const {id} = request.params;
    const sql = `SELECT * FROM Usuarios WHERE idUsuario = ${id}`;
    connection.query(sql,(error,resultado)=>{
        if (error) throw error;
        if (resultado.length > 0){
            response.json(resultado);
        } else {
            response.send('No hay resultados');
        }
    })
});


// Arrancar el servidor
appSD.listen(port, () => {
    console.log(`Ejecutando la aplicación API REST de SD en el puerto ${port}`);
});
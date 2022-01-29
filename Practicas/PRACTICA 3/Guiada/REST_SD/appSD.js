
const express = require('express');
const app = express();

//Defininmos el puerto al que nos conectamos
const port = 3000;

//Conexion a la base de datos mysql
const mysql = require('mysql');
const bodyParser = require('body-parser');
const { response } = require('express');
const connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'new-password',
    database: 'SD_MYSQL' 
});

//Para enviar datos en tipo de formato JSON
app.use(bodyParser.json());

app.get('/', (req, res) => {
    res.json({message:'Página de inicio de la aplicación de SD'})
});

//Comprobar conexión a la base de datos
connection.connect(error=>{
    if(error) throw error;
    else console.log('Conexión a la base de datos establecida');
});

//Ejecutar la aplicacion
app.listen(port, () => {
    console.log(`Aplicación ejecutandose en el puerto ${port}`);
});

//Listado de todos los usuarios
app.get('/usuarios', (request, resultado) => {
    console.log('Listado de todos los usuarios');

    const sql = 'SELECT * FROM usuarios';
    connection.query(sql, (error, result) => {
        if(error) throw error;
        else resultado.json(result);
    });
});

// Obtener datos de un usuario
// A partir de los ":" son los parámetros
app.get("/usuarios/:id",(request, response) => {
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


// Añadir un nuevo usuario
app.post("/usuarios",(request, response) => {
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


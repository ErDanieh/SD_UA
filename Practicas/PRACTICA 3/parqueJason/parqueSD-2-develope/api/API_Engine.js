const express = require("express");
const bodyParser = require("body-parser");
const mysql = require("mysql");
const cors = require('cors');

var fs = require('fs');
var https = require('https');

var options = {
    key: fs.readFileSync('./key.pem'),
    cert: fs.readFileSync('./cert.pem'),
};


const app = express();
app.use(cors())

const atracciones_route = require('./routes/atracciones_routes.js');
const casillas_route = require('./routes/casillas_routes.js');
const sensores_route = require('./routes/sensores_routes.js');
const visitantes_route = require('./routes/visitantes_routes.js');
const mapas_route = require('./routes/mapas_routes.js');
const temperatura_route = require('./routes/temperaturas_routes');

app.use(atracciones_route);
app.use(casillas_route);
app.use(sensores_route);
app.use(visitantes_route);
app.use(mapas_route);
app.use(temperatura_route);

//Parsear el body usando body parser
app.use(bodyParser.json()); // body en formato json

// Se define el puerto
const port=3010;


app.get("/", (req, res) => {
    res.json({message: 'Pagina de inicio de aplicacion de ejemplo de SD'})
});

// Ejecutar la aplicacion
https.createServer(options, app).listen(port, () => {
    console.log(`Ejecutando la aplicacion API REST de SD parque en el puerto ${port}`);
});


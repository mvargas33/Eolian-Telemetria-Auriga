'use strict' // Para usar nueva notación de JS

const express = require('express') // Para montar servidor
const bodyParser = require('body-parser') // Para parsear solicitudes POST y otras

const app = express() // Servidor
const port = process.env.PORT || 3000 // Puerto del servidor

app.use(bodyParser.urlencoded({extended: false })) // Estas dos líneas son estándar
app.use(bodyParser.json())

app.get('/hola/:name', (req, res) => { // /hola/:name envía parametro por GET y es accesible con ${req.params.name}
    res.send({ message: `Hola ${req.params.name}`})
}) // Ruta a la que estucha este bicho con peticion GET. Lambda recibe request y envía response

app.listen(port, () => { // Pasamos una lambda que solo hace log
    console.log(`Servidor corriendo en http://localhost:${port}`) // Print inicial del servidor
})
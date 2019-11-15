'use strict' // Para usar nueva notación de JS

const express = require('express') // Para montar servidor
const bodyParser = require('body-parser') // Para parsear solicitudes POST y otras
const HashMap = require('hashmap')

const app = express() // Servidor
const port = process.env.PORT || 3000 // Puerto del servidor

app.use(bodyParser.urlencoded({extended: true })) // Estas dos líneas son estándar
app.use(bodyParser.json()) // Usar formato JSON para POST

// Variable global con todos los pares (componenteID, valores[])
var map = new HashMap()
map.set("BMS_DESTINO", [])
map.set("MPPT1", [])


app.post('/update', (req, res) => { // Listener a la ruta /update
    console.log(req.body) // DEBUG, Parse de los datos
    var componente = req.body.componente // Tomamos el nombre del componente
    var valores = JSON.parse(req.body.valores) // Transformamos el String de valors a Array
    map.set(componente, valores) // Actualizamos variable global
    console.log(map.get(componente)) // Imprimimos en consola para ver si se actualiza
    res.status(200).send({message: 'El componente se ha actualizado'}) // Cósigo de retorno para App

})

app.listen(port, () => { // Pasamos una lambda que solo hace log
    console.log(`Servidor corriendo en http://localhost:${port}`) // Print inicial del servidor
})
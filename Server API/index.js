'use strict' // Para usar nueva notación de JS

const delay = require('delay');
const bodyParser = require('body-parser'); // Para parsear solicitudes POST y otras
const HashMap = require('hashmap');

const app = require('express')() // Servidor
const server = require('http').Server(app);
const io = require('socket.io')(server);

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
    var valores = JSON.parse(req.body.valores) // Transformamos el String de valores a Array
    map.set(componente, valores) // Actualizamos variable global
    console.log(map.get(componente)) // Imprimimos en consola para ver si se actualiza
    res.status(200).send({message: 'El componente se ha actualizado'}) // Cósigo de retorno para App

})

var v = {mainData : [56, 3.4, 1.024, 33, 4.048, 3.876, 34.5, 27.9]};

app.get('/getdata', (req, res) => {
    res.status(200).send(v) // Enviar velocidad
})




/*
while(true){
    v.velocidad = Math.random()*100;
    console.log(v.velocidad);
}*/

io.on('connection', function(socket) {
    console.log('A user connected');

    socket.on('disconnect', () => {
        console.log('A user disconnected');
    })

    //socket.emit('dataChannel', v)
})

server.listen(port, () => { // Pasamos una lambda que solo hace log
    console.log(`Servidor corriendo en http://localhost:${port}`) // Print inicial del servidor
})

async function init(){
    while(true){
        v.mainData[0] = Math.trunc(Math.random()*100);
        console.log(v.mainData[0]);
        io.emit('mainData', v); // To all sockets
        await delay(100)

    }
}
init();
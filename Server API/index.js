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

// var v = {mainData : [56, 3.4, 1.024, 33, 4.048, 3.876, 34.5, 27.9]};

// app.get('/getdata', (req, res) => {
//     res.status(200).send(v) // Enviar velocidad
// })

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


var data = {
    mainData: [56, 3.4, 1.024, 33, 4.048, 3.876, 34.5, 27.9], // 8 [velocidad, potencia, mppt_in, soc, max_volt, min_volt, max_temp, min_temp]
    kelly_der: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13], // 14 [ia, ib, ic, va, vb, vc, rpm, err_code, pwm, emr, motor_temp, kelly_temp, throttle, reverse]
    kelly_izq: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13], // 14 [ia, ib, ic, va, vb, vc, rpm, err_code, pwm, emr, motor_temp, kelly_temp, throttle, reverse]
    bms: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14], // 15 [SOC, current, inst_volt, open_volt, abs_current, max_volt, min_volt, max_temp, max_temp_id, min_temp, min_temp_id, avg_temp, internal_temp, max_volt_id, min_volt_id]
    bms_temp: [0, 70, 60, 50, 40, 30, 20, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], // 60
    bms_volt: [3.4, 3.5, 3.65, 3.75, 3.8, 3.9, 4.0, 4.1, 4.2, 4.3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3.666, 0, 0, 0, 0], // 30
    mppt1: [0, 1, 2, 3, 4, 5, 6, 7], // 8 [uin, iin, uout, blvr, ovt, noc, undv, temp]
    mppt2: [0, 1, 2, 3, 4, 5, 6, 7], // 8
    mppt3: [0, 1, 2, 3, 4, 5, 6, 7], // 8
    mppt4: [0, 1, 2, 3, 4, 5, 6, 7], // 8
    potencia_mppt: [0, 0, 0, 0], // 4 [pot_mppt1, pot_mppt2, pot_mppt3, pot_mppt4]
    data_grafico_1: [0, 0, 0], // 3 [Velocidad, Consumo, Paneles IN]
    data_grafico_2: [0, 0, 0] // 3 [Temp BMS, Temp Kelly_Der, Temp Kelly_Izq]
  }

function random_Velocidad(prev){
    var max = 150;
    var min = 0;
    var array = new Array(Math.floor((max - min)));
    // array.forEach(element => {
    //     Math.floor(Math.random() * (max - min) + min);
    // });
    for (let index = 0; index < array.length; index++) {
        array[index] = Math.floor(Math.random() * (max - min) + min);
    }
    var min = max + 1
    for (let index = 0; index < array.length; index++) {
        var d = array[index] - prev % (max-min)
        //console.log(array[index])
        if(d < min){
            min = array[index];
        }
        
    }
    return min;
}

var v = 50;

async function init(){
    while(true){
        //v.mainData[0] = Math.trunc(Math.random()*100);
        //console.log(v.mainData[0]);
        io.emit('mainData', data.mainData); // To all sockets
        //var v = random_Velocidad(v)
        console.log(v)
        await delay(100)

    }
}
init();
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

// function random_Velocidad(prev){
//     var max = 150;
//     var min = 0;
//     var array = new Array(Math.floor((max - min)));
//     // array.forEach(element => {
//     //     Math.floor(Math.random() * (max - min) + min);
//     // });
//     for (let index = 0; index < array.length; index++) {
//         array[index] = Math.floor(Math.random() * (max - min) + min);
//     }
//     var min = max + 1
//     for (let index = 0; index < array.length; index++) {
//         var d = array[index] - prev % (max-min)
//         //console.log(array[index])
//         if(d < min){
//             min = array[index];
//         }
        
//     }
//     return min;
// }



function randomArray(length, minValue, maxValue, sinuses, cosinuses, amplsin, amplcos, noise){
    if(length <= 0){
        return []
    }
    var returnValues = []

    for (let i = 0; i < length; i++) {
        var newVal = 0;
        // sin
        for (let sin = 1; sin <= sinuses; sin++)
        {
            newVal += amplsin * Math.sin((2 * sin * i * Math.PI) / length);
        }
        // cos
        for (let cos = 1; cos <= cosinuses; cos++)
        {
            newVal += amplcos * Math.cos((2 * cos * i * Math.PI) / length);
        }
        // noise
        newVal += (noise * Math.random()) - (noise * Math.random());
        returnValues.push(newVal);
    }
    // give offset so it be higher than 0
    // console.log(returnValues)
    var min = Math.min(...returnValues);
    // console.log(min)
    for (let i = 0; i < returnValues.length; i++) {
        returnValues[i] += - min;
    }
    
    // console.log(returnValues)

    min = 0
    //console.log(min)
    //console.log(minValue)
    //console.log(returnValues)
    // if(min < minValue){
    //     min *= -1;
    //     for (let i = 0; i < length; i++) {
    //         returnValues[i] += min + minValue;
    //     }
    //     //console.log(returnValues)
    // }
    // resize to be fit in 100
    var max = Math.max(...returnValues);
    // if(max >= maxValue){
    //     var scaler = max / maxValue;
    //     for (let i = 0; i < length; i++) {
    //         returnValues[i] /= scaler;
    //     }
    // }
    // console.log(min)
    // console.log(max)
    // console.log(minValue)
    // console.log(maxValue)
    //console.log(returnValues)
    for (let i = 0; i < returnValues.length; i++) {
        returnValues[i] = minValue + (((maxValue - minValue)/(max - min)) * (returnValues[i] - min))
    }

    // returnValues.forEach(element => {
    //     element = minValue + (((maxValue - minValue)/(max - min)) * (element - min))
    // });
    //console.log(returnValues)
    return returnValues;
}



//var speedArray = randomArray(numberOfPoints, 0, 150, Math.random()*10, Math.random()*10, Math.random() > 0.5 ? Math.random()*10 : Math.random()*-10, Math.random() > 0.5 ? Math.random()*10 : Math.random()*-10, 0);

var mainData_MaxMin = [[0, 150], [0, 10], [0, 4], [0, 100], [3.4, 4.2], [3.4, 4.2], [0, 50], [0,50]]
var kelly_der_MaxMin = [[0, 9], [0, 9], [0, 9], [0, 100], [0, 100], [0, 100], [0, 500], [0, 9], [0, 1], [0, 1], [0, 100], [0, 100], [0, 1], [0, 1]] // 14 [ia, ib, ic, va, vb, vc, rpm, err_code, pwm, emr, motor_temp, kelly_temp, throttle, reverse]
var kelly_izq_MaxMin = [[0, 9], [0, 9], [0, 9], [0, 100], [0, 100], [0, 100], [0, 500], [0, 9], [0, 1], [0, 1], [0, 100], [0, 100], [0, 1], [0, 1]] // 14 [ia, ib, ic, va, vb, vc, rpm, err_code, pwm, emr, motor_temp, kelly_temp, throttle, reverse]
var bms_MaxMin = [[0, 100], [0, 90], [80, 115], [80, 115], [0, 90], [3.4, 4.2], [3.4, 4.2], [0, 50], [0, 30], [0, 50], [0, 30], [0, 50], [0, 50], [0, 30], [0, 30]]
var bms_temp_MaxMin = [[0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50], [0, 50]]
var bms_volt_MaxMin = [[3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], [3.4, 4.2], ]
var mppt1_MaxMin = [[0, 80], [0, 10], [80, 115], [0, 1], [0, 1], [0, 1], [0, 1], [0, 50]]
var mppt2_MaxMin = [[0, 80], [0, 10], [80, 115], [0, 1], [0, 1], [0, 1], [0, 1], [0, 50]]
var mppt3_MaxMin = [[0, 80], [0, 10], [80, 115], [0, 1], [0, 1], [0, 1], [0, 1], [0, 50]]
var mppt4_MaxMin = [[0, 80], [0, 10], [80, 115], [0, 1], [0, 1], [0, 1], [0, 1], [0, 50]]
var potencia_mppt_MaxMin = [[0, 450], [0, 450], [0, 450], [0, 450]]

function genArrays(numberOfPoints, MaxMinArray){
    var r = []
    MaxMinArray.forEach(element => {
        r.push(randomArray(numberOfPoints, element[0], element[1], Math.random()*10, Math.random()*10, Math.random() > 0.5 ? Math.random()*10 : Math.random()*-10, Math.random() > 0.5 ? Math.random()*10 : Math.random()*-10, 0))
    });
    return r;
}

function getIesimoArrayOneDimension(oneDimensionArray, i){
    var r = []
    oneDimensionArray.forEach(element => {
        r.push(element[i])
    });
    return r;
}

function getDataIesimo(multiArray, i){
    return {
        mainData: getIesimoArrayOneDimension(multiArray.mainData, i),
        kelly_der: getIesimoArrayOneDimension(multiArray.kelly_der, i),
        kelly_izq: getIesimoArrayOneDimension(multiArray.kelly_izq, i),
        bms: getIesimoArrayOneDimension(multiArray.bms, i),
        bms_temp: getIesimoArrayOneDimension(multiArray.bms_temp, i),
        bms_volt: getIesimoArrayOneDimension(multiArray.bms_volt, i),
        mppt1: getIesimoArrayOneDimension(multiArray.mppt1, i),
        mppt2: getIesimoArrayOneDimension(multiArray.mppt2, i),
        mppt3: getIesimoArrayOneDimension(multiArray.mppt3, i),
        mppt4: getIesimoArrayOneDimension(multiArray.mppt4, i),
        potencia_mppt: getIesimoArrayOneDimension(multiArray.potencia_mppt, i)
    }
}

var N = 100;
var dataArray = {
    mainData: genArrays(N, mainData_MaxMin),
    kelly_der: genArrays(N, kelly_der_MaxMin),
    kelly_izq: genArrays(N, kelly_izq_MaxMin),
    bms: genArrays(N, bms_MaxMin),
    bms_temp: genArrays(N, bms_temp_MaxMin),
    bms_volt: genArrays(N, bms_volt_MaxMin),
    mppt1: genArrays(N, mppt1_MaxMin),
    mppt2: genArrays(N, mppt2_MaxMin),
    mppt3: genArrays(N, mppt3_MaxMin),
    mppt4: genArrays(N, mppt4_MaxMin),
    potencia_mppt: genArrays(N, potencia_mppt_MaxMin)
}

async function init(){
    var i = 0;
    while(true){
        if(i >= N){
            i = 0;
        }
        var data = getDataIesimo(dataArray, i);


        //v.mainData[0] = Math.trunc(Math.random()*100);
        //console.log(i)
        //console.log(speedArray[i]);
        //data.mainData[0] = speedArray[i];


        io.emit('mainData', data.mainData); // To all sockets
        await delay(10)
        io.emit('kelly_der', data.kelly_der); // To all sockets
        await delay(10)
        io.emit('kelly_izq', data.kelly_izq); // To all sockets
        await delay(10)
        io.emit('bms', data.bms); // To all sockets
        await delay(10)
        io.emit('bms_temp', data.bms_temp); // To all sockets
        await delay(10)
        io.emit('bms_volt', data.bms_volt); // To all sockets
        await delay(10)
        io.emit('mppt1', data.mppt1); // To all sockets
        await delay(10)
        io.emit('mppt2', data.mppt2); // To all sockets
        await delay(10)
        io.emit('mppt3', data.mppt3); // To all sockets
        await delay(10)
        io.emit('mppt4', data.mppt4); // To all sockets
        await delay(10)
        io.emit('potencia_mppt', data.potencia_mppt); // To all sockets

        i +=1;
        
        await delay(100)

    }
}
init();

// var data = {
//     mainData: [56, 3.4, 1.024, 33, 4.048, 3.876, 34.5, 27.9], // 8 [velocidad, potencia, mppt_in, soc, max_volt, min_volt, max_temp, min_temp]
//     kelly_der: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13], // 14 [ia, ib, ic, va, vb, vc, rpm, err_code, pwm, emr, motor_temp, kelly_temp, throttle, reverse]
//     kelly_izq: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13], // 14 [ia, ib, ic, va, vb, vc, rpm, err_code, pwm, emr, motor_temp, kelly_temp, throttle, reverse]
//     bms: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14], // 15 [SOC, current, inst_volt, open_volt, abs_current, max_volt, min_volt, max_temp, max_temp_id, min_temp, min_temp_id, avg_temp, internal_temp, max_volt_id, min_volt_id]
//     bms_temp: [0, 70, 60, 50, 40, 30, 20, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], // 60
//     bms_volt: [3.4, 3.5, 3.65, 3.75, 3.8, 3.9, 4.0, 4.1, 4.2, 4.3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3.666, 0, 0, 0, 0], // 30
//     mppt1: [0, 1, 2, 3, 4, 5, 6, 7], // 8 [uin, iin, uout, blvr, ovt, noc, undv, temp]
//     mppt2: [0, 1, 2, 3, 4, 5, 6, 7], // 8
//     mppt3: [0, 1, 2, 3, 4, 5, 6, 7], // 8
//     mppt4: [0, 1, 2, 3, 4, 5, 6, 7], // 8
//     potencia_mppt: [0, 0, 0, 0], // 4 [pot_mppt1, pot_mppt2, pot_mppt3, pot_mppt4]
//   }
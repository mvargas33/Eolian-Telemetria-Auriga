const path = require('path');       // Hace simple el uso de rutas
var io = null;
const controller = {};

controller.init = function(socketio) {
  io = socketio;
}

// Realiza una visualización de los datos
controller.show = (req, res) => {
  req.getConnection((err, conn) => {  // Request de conexión a DB con getConnection()
    conn.query('SELECT * FROM datosgenerales', (err, filas) => {
      if (err) {
        res.json(err);
      }
      //console.log(filas); // arreglo de la bse de datos
      
      res.render('index', { // en monitoreo_general.ejs
        data: filas
      });

      //res.sendFile(path.join(__dirname+'/index.html'))
    })
  })
};

// Guarda en la database un objeto
controller.save = (req, res) => {
  const data = req.body;

  req.getConnection((err, conn) => {  // getConnection to batabase
    if (err) throw err;
    conn.query('INSERT INTO datosgenerales set ?', [data], (err, row) => {
      if (err) throw err;
      //console.log(row); // Musetra fila en consola

      io.emit('new_general_data', data);  // Emitir evento 'new_data' a los sockets pasando 'data'
      console.log('IO EMIT NEW_DATA EVENT');
      console.log(data);  // req.body gracias a urlencoded (body pasrser)
      res.redirect('/');  // Redireccion a home
    });

  });
};

controller.gauge = (req, res) => {
  res.render('gauge', { // gauge.ejs
  });
}
module.exports = controller;

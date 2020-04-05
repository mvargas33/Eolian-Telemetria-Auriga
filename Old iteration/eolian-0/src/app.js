const express = require('express'); // Servidor simple
const path = require('path');       // Hace simple el uso de rutas
const morgan = require('morgan');   // Visualizar peticiones del cliente
const mysql   = require('mysql');   // Base de datos MySQL
const myConnection = require('express-myconnection');  // Conectar a DB más simple

const app = express();  // Creación del objeto express()
const server = require('http').createServer(app); // Creación del servidor http
const io = require('socket.io')(server);  // Montar socket.io sobre el objeto servidor

// Se declara eolianController para poder pasar objeto socket.io
const eolianController = require('./controllers/eolian_controller');

io.on('connection', function (socket) { // En evento conexión logear socket
  console.log('Un nuevo socket conectado');
});

// Importanción de Rutas
const eolianRoutes = require('./routes/eolian_routes');  // Añade todas las rutas

// Settings
app.set('port', process.env.PORT || 3000);  // Lo que de el OS o 3000
app.set('view engine', 'ejs');  // Utilizar 'ejs' como motor de plantilla (embbeled JS)
app.set('views', path.join(__dirname, 'views')) // Concatena el directorio src con views

// Middlewares: Funciones antes de enviar al cliente
app.use(morgan('dev')); // Morgan loguea los GET/POST/etc en consola
app.use(myConnection(mysql, { // Configuración de conexión a base de datos
  host: 'localhost',
  user: 'root',
  password: 'Dos46810$',
  port: 3306,
  database: 'eolianmysql'
}, 'single'))
app.use(express.urlencoded({extended: false})); // Desde express usar modulo para obtener datos del formulario

// Routes: Rutas de acceso
app.use('/', eolianRoutes); // Utiliza las rutas en customerRoutes

// Static files
app.use(express.static(path.join(__dirname, 'public')));  // Encuentra la carpeta public

// Comenzando el servidor
server.listen(app.get('port'), () => {
  eolianController.init(io);  // Pasar el objeto de socket.io a controladores
  console.log(`Servidor inciado en puerto ${app.get('port')}`);
});

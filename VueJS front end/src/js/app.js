// Import Vue
import Vue from 'vue';


// Import Framework7
import Framework7 from 'framework7/framework7-lite.esm.bundle.js';

// Import Framework7-Vue Plugin
import Framework7Vue from 'framework7-vue/framework7-vue.esm.bundle.js';

// Import Framework7 Styles
import 'framework7/css/framework7.bundle.css';

// Import Icons and App Custom Styles
import '../css/icons.css';
import '../css/app.css';

// Import moment
import moment from 'moment';
moment.locale('es');
Vue.prototype.$moment = moment;

// Import App Component
import App from '../components/app.vue';

// Init Framework7-Vue Plugin
Framework7.use(Framework7Vue);

//_------------------------///
import VueSocketIO from 'vue-socket.io'

//const options = { path: '/js/app' }; //Options object to pass into SocketIO

Vue.use(new VueSocketIO({
  debug: true,
  connection: 'http://localhost:3000', //options object is Optional
  
})
);

//var socket = io.connect('http://localhost:3000', {'forceNew': true});

// Init App
new Vue({
  el: '#app',
  render: (h) => h(App),

  // Register App Component
  components: {
    app: App
  },
  sockets: {
    /*broadcast: function(data) {
      console.log('broadcast event in client received');
      console.log(data);
      console.log(this);

      //this.$refs.VueSpeedometer.updateMyValues(data);
    },*/

    connect: function () {
        console.log('socket connected')
    },
    customEmit: function (data) {
        console.log('this method was fired by the socket server. eg: io.emit("customEmit", data)')
    }

  },
  methods: {
      clickButton: function (data) {
          // $socket is socket.io-client instance
          this.$socket.emit('emit_method', data)
      }
  },
  /*data: {
    velocidad: 11
  },
  /*methods:{
    updateData: function () {
      $.get('/getdata', function (response) {
        this.velocidad = response.velocidad;
      }.bind(this));
    }
  },
  ready: function () {
    this.updateData();

    this.interval = setInterval(function () {
      this.updateData;
    }.bind(this), 1000);
  }*/
});

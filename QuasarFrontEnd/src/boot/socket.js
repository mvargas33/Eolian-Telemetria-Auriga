// import io from 'socket.io-client'

// export default async ({ Vue}) => {
//   Vue.prototype.$mysocket= await io('http://localhost')
// }

/* ------------------------------------ */

import Vue from 'vue'
import VueSocketIO from 'vue-socket.io'
import store from '../store/index.js'

// const settings = {
//   debug: false,
//   connection: 'http://localhost:3000',
//   vuex: {
//     store,
//     actionPrefix: 'SOCKET_',
//     mutationPrefix: 'SOCKET_'
//   }
// }

// Vue.use(new VueSocketIO(settings))

Vue.use(new VueSocketIO({
  debug: true,
  connection: 'http://190.45.34.49:3000',
  vuex: {
    store,
    actionPrefix: 'SOCKET_',
    mutationPrefix: 'SOCKET_'
  }
}))

/* ------------------------------------ */
// import Vue from 'vue'
// import VueSocketIOExt from 'vue-socket.io-extended'
// import io from 'socket.io-client'
// import store from '../store/index.js'

// const socket = io('http://localhost:3000')

// Vue.use(VueSocketIOExt, socket, { store })

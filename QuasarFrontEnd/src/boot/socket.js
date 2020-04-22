// import io from 'socket.io-client'

// export default async ({ Vue}) => {
//   Vue.prototype.$mysocket= await io('http://localhost')
// }
import Vue from 'vue'
import VueSocketIO from 'vue-socket.io'

Vue.use(new VueSocketIO({
  debug: true,
  connection: 'http://localhost:3000'
}))

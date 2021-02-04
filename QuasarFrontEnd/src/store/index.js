import Vue from 'vue'
import Vuex from 'vuex'
import fenix from './module-fenix/index'
import general from './module-general/index'

// import example from './module-example'

Vue.use(Vuex)

/*
 * If not building with SSR mode, you can
 * directly export the Store instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Store instance.
 */

export default function (/* { ssrContext } */) {
  const Store = new Vuex.Store({
    state: {
      recibiendoDatos: false,
      connected: false,
      error: '',
      message: ''
    },
    mutations: {
      SOCKET_CONNECT (state) {
        state.connected = true
        console.log('connected')
      },
      SOCKET_DISCONNECT (state) {
        state.connected = false
        console.log('disconnected')
      },
      SOCKET_MESSAGE (state, message) {
        state.message = message
        console.log('message')
      },
      SOCKET_HELLO_WORLD (state, message) {
        state.message = message
        console.log('hello world')
      },
      SOCKET_ERROR (state, message) {
        state.error = message.error
        console.log('error')
      }
    },
    modules: {
      fenix,
      general
    },

    // enable strict mode (adds overhead!)
    // for dev mode only
    strict: process.env.DEV
  })

  return Store
}

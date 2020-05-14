import Vue from 'vue'
/* Mutations para debug, test y pruebas */
export function SOCKET_CHAT_MESSAGE (state, message) {
  console.log('mutation called')
}

export function SOCKET_updateData (context, data) {
  console.log('Action triggered! socket_updateMainData\n')
  console.log(data)
}

export function updateVelocidad (state) {
  Vue.set(state.mainData, 0, state.mainData[0] + 1) // state.mainData[0]++ No funciona, usar Vue.set https://vuejs.org/v2/guide/reactivity.html#Change-Detection-Caveats
}

export function replaceMainData (state) {
  var a = [100, 3, 0.98, 80, 3.7, 3.6, 38, 20.5] // 8 [velocidad, potencia, mppt_in, soc, max_volt, min_volt, max_temp, min_temp]
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E0
  var x = 0
  var l = a.length
  while (x < l) {
    a[x] = Math.round(a[x] * decimales) / decimales
    x++
  }
  Vue.set(state, 'mainData', [...a]) // [..myArray] will prevent changes to the object from other source to change your source, so it's a good idea to implement in getters too. https://stackoverflow.com/questions/50767191/vuex-update-an-entire-array/50767787
}

export function updatePotencia (state) {
  console.log(state.mainData)
  state.mainData[1]++
  console.log(state.mainData)
}

export function updateModulo (state, index) {
  var newVal = Math.round((state.bms_temp[index] + 1) * 10) / 10
  Vue.set(state.bms_temp, index, newVal)
}

export function sumsoc (state) {
  Vue.set(state.mainData, 3, state.mainData[3] + 1) // state.mainData[0]++ No funciona, usar Vue.set https://vuejs.org/v2/guide/reactivity.html#Change-Detection-Caveats
}

export function updateTemps (state) {
  Vue.set(state.bms, 11, state.bms[11] + 1)
  Vue.set(state.data_grafico_2, 0, state.bms[11])

  Vue.set(state.kelly_der, 11, state.kelly_der[11] + 2)
  Vue.set(state.data_grafico_2, 1, state.kelly_der[11])

  Vue.set(state.kelly_izq, 11, state.kelly_izq[11] + 3)
  Vue.set(state.data_grafico_2, 2, state.kelly_izq[11])
}

/* Mutations reales de update de arreglos de componentes en el store */

export function updateMainData (state, array) {
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E1
  var x = 0
  var l = array.length
  while (x < l) {
    array[x] = Math.round(array[x] * decimales) / decimales
    x++
  }

  // Cambiamos el state
  Vue.set(state, 'mainData', [...array]) // [...array] Copiamos para prevenir cambios de terceros

  // Update del gráfico
  Vue.set(state.data_grafico_1, 0, state.mainData[0]) // Velocidad
  Vue.set(state.data_grafico_1, 1, state.mainData[1]) // Potencia (Consumo)
  Vue.set(state.data_grafico_1, 2, state.mainData[2]) // Paneles IN (MPPT)
}

export function updateKellyDer (state, array) {
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E1
  var x = 0
  var l = array.length
  while (x < l) {
    array[x] = Math.round(array[x] * decimales) / decimales
    x++
  }

  // Cambiamos el state
  Vue.set(state, 'kelly_der', [...array]) // [...array] Copiamos para prevenir cambios de terceros

  // Update del gráfico
  Vue.set(state, 'data_grafico_2', 1, state.kelly_der[10]) // Temp Kelly Der
}

export function updateKellyIzq (state, array) {
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E1
  var x = 0
  var l = array.length
  while (x < l) {
    array[x] = Math.round(array[x] * decimales) / decimales
    x++
  }

  // Cambiamos el state
  Vue.set(state, 'kelly_izq', [...array]) // [...array] Copiamos para prevenir cambios de terceros

  // Update del gráfico
  Vue.set(state, 'data_grafico_2', 2, state.kelly_izq[10]) // Temp Kelly Izq
}

export function updateBms (state, array) {
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E1
  var x = 0
  var l = array.length
  while (x < l) {
    array[x] = Math.round(array[x] * decimales) / decimales
    x++
  }

  // Cambiamos el state
  Vue.set(state, 'bms', [...array]) // [...array] Copiamos para prevenir cambios de terceros

  // Update del gráfico
  Vue.set(state, 'data_grafico_2', 0, state.bms[11]) // Average Temp BMS
}

export function updateBmsTemp (state, array) {
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E1
  var x = 0
  var l = array.length
  while (x < l) {
    array[x] = Math.round(array[x] * decimales) / decimales
    x++
  }

  // Cambiamos el state
  Vue.set(state, 'bms_temp', [...array]) // [...array] Copiamos para prevenir cambios de terceros
}

export function updateBmsVolt (state, array) {
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E3
  var x = 0
  var l = array.length
  while (x < l) {
    array[x] = Math.round(array[x] * decimales) / decimales
    x++
  }

  // Cambiamos el state
  Vue.set(state, 'bms_volt', [...array]) // [...array] Copiamos para prevenir cambios de terceros
}

export function updateMppt1 (state, array) {
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E0
  var x = 0
  var l = array.length
  while (x < l) {
    array[x] = Math.round(array[x] * decimales) / decimales
    x++
  }

  // Cambiamos el state
  Vue.set(state, 'mppt1', [...array]) // [...array] Copiamos para prevenir cambios de terceros
}

export function updateMppt2 (state, array) {
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E0
  var x = 0
  var l = array.length
  while (x < l) {
    array[x] = Math.round(array[x] * decimales) / decimales
    x++
  }

  // Cambiamos el state
  Vue.set(state, 'mppt2', [...array]) // [...array] Copiamos para prevenir cambios de terceros
}

export function updateMppt3 (state, array) {
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E0
  var x = 0
  var l = array.length
  while (x < l) {
    array[x] = Math.round(array[x] * decimales) / decimales
    x++
  }

  // Cambiamos el state
  Vue.set(state, 'mppt3', [...array]) // [...array] Copiamos para prevenir cambios de terceros
}

export function updateMppt4 (state, array) {
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E0
  var x = 0
  var l = array.length
  while (x < l) {
    array[x] = Math.round(array[x] * decimales) / decimales
    x++
  }

  // Cambiamos el state
  Vue.set(state, 'mppt4', [...array]) // [...array] Copiamos para prevenir cambios de terceros
}

export function updatePotenciaMppt (state, array) {
  // Formateamos a tres decimales para visualizar bien
  var decimales = 1E1
  var x = 0
  var l = array.length
  while (x < l) {
    array[x] = Math.round(array[x] * decimales) / decimales
    x++
  }

  // Cambiamos el state
  Vue.set(state, 'potencia_mppt', [...array]) // [...array] Copiamos para prevenir cambios de terceros
}

/* Update de data_grafico_1 y data_grafico_2 en mutaciones que contienen esos datos (mainData, kelly_der, kelly_izq, bms) */

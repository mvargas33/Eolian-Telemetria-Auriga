import Vue from 'vue'

export function updateVelocidad (state) {
  Vue.set(state.mainData, 0, state.mainData[0] + 1) // state.mainData[0]++ No funciona, usar Vue.set https://vuejs.org/v2/guide/reactivity.html#Change-Detection-Caveats
}

export function replaceMainData (state) {
  var a = [100, 3, 0.98, 80, 3.7, 3.6, 38, 20.5] // 8 [velocidad, potencia, mppt_in, soc, max_volt, min_volt, max_temp, min_temp]
  // Formateamos a tres decimales para visualizar bien
  var x = 0
  var l = a.length
  while (x < l) {
    a[x] = Math.round(a[x] * 1000) / 1000
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
  var newVal = Math.round((state.bms_volt[index] + 0.01) * 1000) / 1000
  Vue.set(state.bms_volt, index, newVal)
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

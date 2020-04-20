/* Actions para debug, test y pruebas */

export function addSpeed (context) {
  context.commit('updateVelocidad')
}

export function replaceData (context) {
  context.commit('replaceMainData')
}

export function sumsoc (context) {
  context.commit('sumsoc')
}

export function updateTemps (context) {
  context.commit('updateTemps')
}

/* Actions reales que llaman a mutations reales */

export function update_mainData(context, array) {
  context.commit('update_mainData', array)
}

export function update_kelly_der(context, array) {
  context.commit('update_kelly_der', array)
}

export function update_kelly_izq(context, array) {
  context.commit('update_kelly_izq', array)
}

export function update_bms(context, array) {
  context.commit('update_bms', array)
}

export function update_bms_temp(context, array) {
  context.commit('update_bms_temp', array)
}

export function update_bms_volt(context, array) {
  context.commit('update_bms_volt', array)
}

export function update_mppt1(context, array) {
  context.commit('update_mppt1', array)
}

export function update_mppt2(context, array) {
  context.commit('update_mppt2', array)
}

export function update_mppt3(context, array) {
  context.commit('update_mppt3', array)
}

export function update_mppt4(context, array) {
  context.commit('update_mppt4', array)
}

export function update_potencia_mppt(context, array) {
  context.commit('update_potencia_mppt', array)
}
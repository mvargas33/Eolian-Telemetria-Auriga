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

export function updateMainData (context, array) {
  context.commit('updateMainData', array)
}

export function updateKellyDer (context, array) {
  context.commit('updateKellyDer', array)
}

export function updateKellyIzq (context, array) {
  context.commit('updateKellyIzq', array)
}

export function updateBms (context, array) {
  context.commit('updateBms', array)
}

export function updateBmsTemp (context, array) {
  context.commit('updateBmsTemp', array)
}

export function updateBmsVolt (context, array) {
  context.commit('updateBmsVolt', array)
}

export function updateMppt1 (context, array) {
  context.commit('updateMppt1', array)
}

export function updateMppt2 (context, array) {
  context.commit('updateMppt2', array)
}

export function updateMppt3 (context, array) {
  context.commit('updateMppt3', array)
}

export function updateMppt4 (context, array) {
  context.commit('updateMppt4', array)
}

export function updatePotenciaMppt (context, array) {
  context.commit('updatePotenciaMppt', array)
}

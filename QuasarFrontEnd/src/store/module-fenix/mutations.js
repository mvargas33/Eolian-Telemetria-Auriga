export function updateVelocidad (state) {
  console.log(state.mainData)
  state.mainData[0]++
  console.log(state.mainData)
}

export function updatePotencia (state) {
  console.log(state.mainData)
  state.mainData[1]++
  console.log(state.mainData)
}

export function updateModulo (state, index) {
  console.log(state.bms_volt)
  state.bms_volt[index]++
  console.log(state.bms_volt)
}

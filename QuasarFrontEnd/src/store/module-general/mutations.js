import Vue from 'vue'

export function turnDarkMode (state) {
  Vue.set(state, 'darkMode', !state.darkMode)
}

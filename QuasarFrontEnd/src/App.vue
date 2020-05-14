<template>
  <div id="q-app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  sockets: {
    mainData (data) {
      // console.log('broadcast event listened\n')
      // console.log(this.$store)
      console.log(data)
      this.$store.dispatch('fenix/updateMainData', data)
    },
    kelly_der (data) {
      this.$store.dispatch('fenix/updateKellyDer', data)
    },
    kelly_izq (data) {
      this.$store.dispatch('fenix/updateKellyIzq', data)
    },
    bms (data) {
      this.$store.dispatch('fenix/updateBms', data)
    },
    bms_temp (data) {
      this.$store.dispatch('fenix/updateBmsTemp', data)
    },
    bms_volt (data) {
      this.$store.dispatch('fenix/updateBmsVolt', data)
    },
    mppt1 (data) {
      this.$store.dispatch('fenix/updateMppt1', data)
    },
    mppt2 (data) {
      this.$store.dispatch('fenix/updateMppt2', data)
    },
    mppt3 (data) {
      this.$store.dispatch('fenix/updateMppt3', data)
    },
    mppt4 (data) {
      this.$store.dispatch('fenix/updateMppt4', data)
    },
    potencia_mppt (data) {
      this.$store.dispatch('fenix/updatePotenciaMppt', data)
    },
    hola (data) {
      console.log(data)
    }
  }
}
</script>

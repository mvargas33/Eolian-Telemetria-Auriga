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
      console.log('broadcast event listened\n')
      // console.log(this.$store)
      console.log(data)
      this.$store.dispatch('fenix/updateMainData', data.data)
    },
    kelly_der (data) {
      console.log(data)
      this.$store.dispatch('fenix/updateKellyDer', data.data)
    },
    kelly_izq (data) {
      console.log(data)
      this.$store.dispatch('fenix/updateKellyIzq', data.data)
    },
    bms (data) {
      console.log(data)
      this.$store.dispatch('fenix/updateBms', data.data)
    },
    bms_temp (data) {
      console.log(data)
      this.$store.dispatch('fenix/updateBmsTemp', data.data)
    },
    bms_volt (data) {
      console.log(data)
      this.$store.dispatch('fenix/updateBmsVolt', data.data)
    },
    mppt1 (data) {
      console.log(data)
      this.$store.dispatch('fenix/updateMppt1', data.data)
    },
    mppt2 (data) {
      console.log(data)
      this.$store.dispatch('fenix/updateMppt2', data.data)
    },
    mppt3 (data) {
      console.log(data)
      this.$store.dispatch('fenix/updateMppt3', data.data)
    },
    mppt4 (data) {
      console.log(data)
      this.$store.dispatch('fenix/updateMppt4', data.data)
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

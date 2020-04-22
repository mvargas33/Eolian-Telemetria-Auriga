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
      this.$store.dispatch('fenix/updateMainData', data.mainData)
    }
  }
}
</script>

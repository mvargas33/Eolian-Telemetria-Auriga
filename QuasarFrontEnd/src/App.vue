<template>
  <div id="q-app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  sockets: {
    broadcast (data) {
      console.log('broadcast event listened\n')
      console.log(data)
    }
  }
}
</script>

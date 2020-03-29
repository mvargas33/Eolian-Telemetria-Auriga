<template>
  <q-page class="flex flex-center">

      <h6>Información básica de eolian fénix</h6>
      <ul>
          <li>{{ velocidad }}</li>
          <li>{{ potencia }}</li>
          <li>{{ temperatura }}</li>
      </ul>

  </q-page>
</template>

<script>
export default {
  name: 'FenixBasic',
  data () {
    return {
      velocidad: 0,
      potencia: 0,
      temperatura: 0
    }
  }
}
</script>

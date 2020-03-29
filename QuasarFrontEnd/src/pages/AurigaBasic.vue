<template>
  <q-page class="flex flex-center">

      <h6>Información básica eolian Áuriga</h6>
      <div>
        <ul>
            <li>{{ velocidad }}</li>
            <li>{{ potencia }}</li>
            <li>{{ temperatura }}</li>
        </ul>
      </div>

  </q-page>
</template>

<script>
export default {
  name: 'AurigaBasic',
  data () {
    return {
      velocidad: 0,
      potencia: 0,
      temperatura: 0
    }
  }
}
</script>

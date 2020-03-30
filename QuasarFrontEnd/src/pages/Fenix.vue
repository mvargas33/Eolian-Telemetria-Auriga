<template>
  <q-page class="flex flex-center">

      Hola eolian fénix!
      Velocidad: {{ velocidad }} - {{ potencia }} - {{ recibiendoDatos }}
      <q-btn color="white" text-color="black" label="++Velocidad" @click='updateVelocidad'/>
      <q-btn color="red" text-color="black" label="++Potencia" @click='updatePotencia'/>
  </q-page>
</template>

<script>
import { mapState, mapMutations } from 'vuex'

export default {
  name: 'Fenix',
  computed: {
    ...mapState('fenix', ['velocidad', 'potencia']),
    ...mapState(['recibiendoDatos'])
  },
  methods: {
    ...mapMutations('fenix', ['updateVelocidad', 'updatePotencia'])
  }
}
</script>

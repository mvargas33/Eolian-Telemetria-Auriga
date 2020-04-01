<template>
    <div>
        Grafico de Temperaturas
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'GraficoTemp',
  computed: {
    ...mapState('fenix', ['mainData'])
  }
}
</script>

<template>
    <div>
        Grafico Consumo
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'GraficoConsumo',
  computed: {
    ...mapState('fenix', ['mainData'])
  }
}
</script>

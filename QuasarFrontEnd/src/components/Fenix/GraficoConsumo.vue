<template>
    <div class="row justify-center full-height full-width text-center">
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

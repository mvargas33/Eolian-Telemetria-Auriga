<template>
    <div class="row justify-center full-height full-width text-center">
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

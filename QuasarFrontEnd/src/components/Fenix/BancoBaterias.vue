<template>
    <div class="row justify-center full-height full-width text-center">
        Banco de baterías
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'BancoBaterias',
  computed: {
    ...mapState('fenix', ['mainData'])
  }
}
</script>

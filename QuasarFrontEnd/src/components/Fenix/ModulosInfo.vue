<template>
    <div class="row justify-center full-height full-width text-center">
        Modulos Info
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'ModulosInfo',
  computed: {
    ...mapState('fenix', ['mainData'])
  }
}
</script>

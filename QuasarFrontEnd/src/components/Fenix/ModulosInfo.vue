<template>
    <div>
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

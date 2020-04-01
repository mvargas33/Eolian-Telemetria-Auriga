<template>
    <div class="row justify-center full-height full-width text-center">
        MPPT Info
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'MPPTInfo',
  computed: {
    ...mapState('fenix', ['mainData'])
  }
}
</script>

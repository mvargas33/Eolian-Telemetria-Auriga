<template>
    <div class="row justify-center full-height full-width text-center">
        BMS Info
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'BMSInfo',
  computed: {
    ...mapState('fenix', ['mainData'])
  }
}
</script>

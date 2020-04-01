<template>
    <div>
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

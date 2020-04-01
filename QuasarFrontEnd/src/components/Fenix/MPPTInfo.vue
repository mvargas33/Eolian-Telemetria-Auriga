<template>
    <div>
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

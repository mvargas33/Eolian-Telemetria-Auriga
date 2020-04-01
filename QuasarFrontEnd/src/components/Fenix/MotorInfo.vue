<template>
    <div>
        Motor Info
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'MotorInfo',
  computed: {
    ...mapState('fenix', ['mainData'])
  }
}
</script>

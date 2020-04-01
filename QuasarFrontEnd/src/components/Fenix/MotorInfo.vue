<template>
    <div class="row justify-center full-height full-width text-center">
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

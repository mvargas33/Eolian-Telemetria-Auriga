<template>
  <div>
    <div class="allCenter">
            SOC
    </div>
    <div class="row justify-center full-width text-center">
        <div class="allCenter numero">
          {{ mainData[3] }}
        </div>
        <div class="allCenter numero">
          %
        </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'SocBar',
  computed: {
    ...mapState('fenix', ['mainData'])
  }
}
</script>

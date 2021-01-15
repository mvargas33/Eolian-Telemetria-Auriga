<template>
  <div style="padding-top: 2%">
    <div class="allCenter">
            <!-- SOC -->
    </div>
    <div class="row justify-center full-width text-center">
      <q-linear-progress stripe size="50px" :value="mainData[3]/100"
      track-color="rgbInterpolationSOC(mainData[3]/100)" class="q-mt-sm" :rounded="true">
        <div class="absolute-full flex flex-center">
          <q-badge color="white" text-color="black" :label="progressLabel2" />
        </div>
      </q-linear-progress>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'SocAuriga',
  computed: {
    ...mapState('fenix', ['mainData']),
    progressLabel2 () {
      return (this.$store.state.fenix.mainData[3]).toFixed(2) + '%'
    }
  },
  methods: {
    rgbInterpolationSOC (valor) {
      var maxV = 1.00
      var minV = 0.00
      var middle = (maxV + minV) / 2

      var amount = 255 / (middle - minV)

      var r = 0
      var g = 0
      var b = 0

      if (valor <= minV) {
        r = 255
        console.log('Hola')
      } else if (valor >= maxV) {
        g = 255
      } else if (valor < middle) {
        r = 255
        g = (valor - minV) * amount
      } else {
        r = 255 - (valor - middle) * amount
        g = 255
      }

      return 'background-color: rgb(' + r + ',' + g + ',' + b + ');'
    }
  }
}
</script>

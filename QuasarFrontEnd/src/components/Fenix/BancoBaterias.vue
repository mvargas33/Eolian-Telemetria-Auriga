<template>
    <div class="padding-5">
      <q-btn color="white" text-color="black" label="Change Modulo 1" @click="updateModulo(0)"/>
        <div class="" v-for='(modulo, index) in bms_volt' :key="index" >
          <div class="row padding-1" v-if="index % 3 === 0">
            <div class="col">
              <div class="row">
                <div class="col-1">
                  {{ index + 1 }}
                </div>
                <div class="col">
                  <div class="row">
                    <div class="col allCenter">
                      {{ bms_temp[index*2] }}
                    </div>
                    <div>
                      |
                    </div>
                    <div class="col allCenter">
                      {{ bms_temp[index*2 + 1] }}
                    </div>
                  </div>
                  <div class="text-center numero" :style="rgbInterpolation(bms_volt[(index)])">
                    {{ bms_volt[index] }}
                  </div>
                </div>
              </div>
            </div>
            <div class="col">
              <div class="row">
                <div class="col-1">
                  {{ (index + 1) + 1 }}
                </div>
                <div class="col">
                  <div class="row">
                    <div class="col allCenter">
                      {{ bms_temp[(index + 1)*2] }}
                    </div>
                    <div>
                      |
                    </div>
                    <div class="col allCenter">
                      {{ bms_temp[(index + 1)*2 + 1] }}
                    </div>
                  </div>
                  <div class="text-center numero" :style="rgbInterpolation(bms_volt[(index + 1)])">
                    {{ bms_volt[(index + 1)] }}
                  </div>
                </div>
              </div>
            </div>
            <div class="col">
              <div class="row">
                <div class="col-1">
                  {{ (index + 2) + 1 }}
                </div>
                <div class="col">
                  <div class="row">
                    <div class="col allCenter">
                      {{ bms_temp[(index + 2)*2] }}
                    </div>
                    <div>
                      |
                    </div>
                    <div class="col allCenter">
                      {{ bms_temp[(index + 2)*2 + 1] }}
                    </div>
                  </div>
                    <div class="text-center numero" :style="rgbInterpolation(bms_volt[(index + 2)])">
                    {{ bms_volt[(index + 2)] }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
    </div>
</template>

<script>
import { mapState, mapMutations } from 'vuex'

export default {
  name: 'BancoBaterias',
  computed: {
    ...mapState('fenix', ['bms_volt', 'bms_temp'])
  },
  methods: {
    ...mapMutations('fenix', ['updateModulo']),
    rgbInterpolation (valor) {
      var maxV = 4.2
      var minV = 3.4
      var middle = (maxV + minV) / 2
      // var amount = (valor - minV) / (maxV - minV)
      var amount = 255 / (middle - minV)
      console.log(amount)

      // var color1 = [255, 0, 0]
      // var color2 = [0, 255, 0]

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

      // var r = (color2[0] - color1[0]) * amount + color1[0]
      // var g = (color2[1] - color1[1]) * amount + color1[1]
      // var b = (color2[2] - color1[2]) * amount + color1[2]

      var str = 'background-color: rgb(' + r + ',' + g + ',' + b + ');'
      console.log(str)
      return str
    }
  }
}
</script>

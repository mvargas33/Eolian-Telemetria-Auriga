<template>
    <div class="padding-5">
      <q-btn color="white" text-color="black" label="Change Modulo 1" @click="updateModulo(0)"/>
        <div class="" v-for='(modulo, index) in bms_volt' :key="index" >
          <div class="row padding-1" v-if="index % 3 === 0">
            <div class="col">
              <div class="row">
                <div class="col-1">
                  {{ index + 1 }} <!-- Módulo Número (parte del 0, que es el módulo 1) -->
                </div>
                <div class="col">
                  <div class="row">
                    <div class="col allCenter" :style="hslInterpolationTEMPERATURA(bms_temp[index*2])">
                      {{ bms_temp[index*2] }} <!-- Primer Termistor son los pares (0 en a 58) -->
                    </div>
                    <div>
                      |
                    </div>
                    <div class="col allCenter" :style="hslInterpolationTEMPERATURA(bms_temp[index*2 + 1])">
                      {{ bms_temp[index*2 + 1] }} <!-- Segundo Termistor son los impares (1 a 59) -->
                    </div>
                  </div>
                  <div class="text-center numero" :style="rgbInterpolationVOLTAJE(bms_volt[(index)])">
                    {{ bms_volt[index] }} <!-- Módulo va a la par con el índice -->
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
                    <div class="col allCenter" :style="hslInterpolationTEMPERATURA(bms_temp[(index + 1)*2])">
                      {{ bms_temp[(index + 1)*2] }}
                    </div>
                    <div>
                      |
                    </div>
                    <div class="col allCenter" :style="hslInterpolationTEMPERATURA(bms_temp[(index + 1)*2 + 1])">
                      {{ bms_temp[(index + 1)*2 + 1] }}
                    </div>
                  </div>
                  <div class="text-center numero" :style="rgbInterpolationVOLTAJE(bms_volt[(index + 1)])">
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
                    <div class="col allCenter" :style="hslInterpolationTEMPERATURA(bms_temp[(index + 2)*2])">
                      {{ bms_temp[(index + 2)*2] }}
                    </div>
                    <div>
                      |
                    </div>
                    <div class="col allCenter" :style="hslInterpolationTEMPERATURA(bms_temp[(index + 2)*2 + 1])">
                      {{ bms_temp[(index + 2)*2 + 1] }}
                    </div>
                  </div>
                    <div class="text-center numero" :style="rgbInterpolationVOLTAJE(bms_volt[(index + 2)])">
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
function hue2rgb (p, q, t) {
  if (t < 0) t += 1
  if (t > 1) t -= 1
  if (t < 1 / 6) return p + (q - p) * 6 * t
  if (t < 1 / 2) return q
  if (t < 2 / 3) return p + (q - p) * (2 / 3 - t) * 6
  return p
}

import { mapState, mapMutations } from 'vuex'

export default {
  name: 'BancoBaterias',
  computed: {
    ...mapState('fenix', ['bms_volt', 'bms_temp'])
  },
  methods: {
    ...mapMutations('fenix', ['updateModulo']),
    rgbInterpolationVOLTAJE (valor) {
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
    },
    hslInterpolationTEMPERATURA (valor) {
      var maxT = 50
      var minT = 5
      var factor = (valor - minT) / (maxT - minT)
      var color1 = [0, 0, 255]
      var color2 = [255, 0, 0]

      // RGB GENERAL INTERPOLATION

      // console.log(factor)
      // var result = color1.slice()
      // for (var i = 0; i < 3; i++) {
      //   result[i] = Math.round(result[i] + factor * (color2[i] - color1[i]))
      // }
      // console.log(result)

      // var str = 'background-color: rgb(' + result[0] + ',' + result[1] + ',' + result[2] + ');'
      // console.log(str)
      // return str

      // HSL GENERAL INTERPOLATION

      // Primero, RGB->HSL

      // Color1
      var r = color1[0] / 255
      var g = color1[1] / 255
      var b = color1[2] / 255
      var max = Math.max(r, g, b), min = Math.min(r, g, b)
      var h, s, l = (max + min) / 2
      if (max === min) {
        h = s = 0 // achromatic
      } else {
        var d = max - min
        s = (l > 0.5 ? d / (2 - max - min) : d / (max + min))
        switch (max) {
          case r: h = (g - b) / d + (g < b ? 6 : 0); break
          case g: h = (b - r) / d + 2; break
          case b: h = (r - g) / d + 4; break
        }
        h /= 6
      }
      var hsl1 = [h, s, l]

      // Color2
      r = color2[0] / 255
      g = color2[1] / 255
      b = color2[2] / 255
      max = Math.max(r, g, b)
      min = Math.min(r, g, b)
      h = (max + min) / 2
      s = (max + min) / 2
      l = (max + min) / 2
      if (max === min) {
        h = s = 0 // achromatic
      } else {
        d = max - min
        s = (l > 0.5 ? d / (2 - max - min) : d / (max + min))
        switch (max) {
          case r: h = (g - b) / d + (g < b ? 6 : 0); break
          case g: h = (b - r) / d + 2; break
          case b: h = (r - g) / d + 4; break
        }
        h /= 6
      }
      var hsl2 = [h, s, l]

      // HSL Interpolation
      for (var i = 0; i < 3; i++) {
        hsl1[i] += factor * (hsl2[i] - hsl1[i])
      }

      // HSL->RGB
      var finalRGB

      l = hsl1[2]
      if (hsl1[1] === 0) {
        l = Math.round(l * 255)
        finalRGB = [l, l, l]
      } else {
        s = hsl1[1]
        var q = (l < 0.5 ? l * (1 + s) : l + s - l * s)
        var p = 2 * l - q
        r = hue2rgb(p, q, hsl1[0] + 1 / 3)
        g = hue2rgb(p, q, hsl1[0])
        b = hue2rgb(p, q, hsl1[0] - 1 / 3)

        finalRGB = [Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)]
      }

      var str = 'background-color: rgb(' + finalRGB[0] + ',' + finalRGB[1] + ',' + finalRGB[2] + ');'
      // console.log(str)
      return str
    }
  }
}
</script>

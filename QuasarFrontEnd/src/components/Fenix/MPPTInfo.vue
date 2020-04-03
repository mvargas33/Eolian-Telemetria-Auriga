<template>
    <div class="padding-5">
        <div class="allCenter">
            <!-- MPPT -->
        </div>
        <div class="row">
            <!-- 1 Columna  -->
            <div class="col-1">
              <div class="text-center etiqueta texto-bold">
                  1
              </div>
              <div class="text-center etiqueta texto-bold">
                  2
              </div>
              <div class="text-center etiqueta texto-bold">
                  3
              </div>
              <div class="text-center etiqueta texto-bold">
                  4
              </div>
              <div class="text-center etiqueta">
              </div>
            </div>
            <!-- 2 Columna V in -->
            <div class="col-2">
              <div class="text-center">
                  {{ mppt1[0] }}
              </div>
              <div class="text-center">
                  {{ mppt2[0] }}
              </div>
              <div class="text-center">
                  {{ mppt3[0] }}
              </div>
              <div class="text-center">
                  {{ mppt4[0] }}
              </div>
              <div class="text-center etiqueta texto-bold">
                  V in
              </div>
            </div>
            <!-- 3 Columna I in -->
            <div class="col-2">
              <div class="text-center">
                  {{ mppt1[1] }}
              </div>
              <div class="text-center">
                  {{ mppt2[1] }}
              </div>
              <div class="text-center">
                  {{ mppt3[1] }}
              </div>
              <div class="text-center">
                  {{ mppt4[1] }}
              </div>
              <div class="text-center etiqueta texto-bold">
                  I in
              </div>
            </div>
            <!-- 4 Columna V out-->
            <div class="col-2">
              <div class="text-center">
                  {{ mppt1[2] }}
              </div>
              <div class="text-center">
                  {{ mppt2[2] }}
              </div>
              <div class="text-center">
                  {{ mppt3[2] }}
              </div>
              <div class="text-center">
                  {{ mppt4[2] }}
              </div>
              <div class="text-center etiqueta texto-bold">
                  V out
              </div>
            </div>
            <!-- 5 Columna Temp -->
            <div class="col-1">
              <div class="text-center">
                  {{ mppt1[7] }}
              </div>
              <div class="text-center">
                  {{ mppt2[7] }}
              </div>
              <div class="text-center">
                  {{ mppt3[7] }}
              </div>
              <div class="text-center">
                  {{ mppt4[7] }}
              </div>
              <div class="text-center etiqueta texto-bold">
                  T°
              </div>
            </div>
            <!-- 6 Columna BVLR -->
            <div class="col-1">
              <div class="text-center">
                  {{ mppt1[3] }}
              </div>
              <div class="text-center">
                  {{ mppt2[3] }}
              </div>
              <div class="text-center">
                  {{ mppt3[3] }}
              </div>
              <div class="text-center">
                  {{ mppt4[3] }}
              </div>
              <div class="text-center etiqueta texto-bold">
                  BVLR
              </div>
            </div>
            <!-- 7 Columna OVT-->
            <div class="col-1">
              <div class="text-center">
                  {{ mppt1[4] }}
              </div>
              <div class="text-center">
                  {{ mppt2[4] }}
              </div>
              <div class="text-center">
                  {{ mppt3[4] }}
              </div>
              <div class="text-center">
                  {{ mppt4[4] }}
              </div>
              <div class="text-center etiqueta texto-bold">
                  OVT
              </div>
            </div>
            <!-- 8 Columna NOC -->
            <div class="col-1">
              <div class="text-center">
                  {{ mppt1[5] }}
              </div>
              <div class="text-center">
                  {{ mppt2[5] }}
              </div>
              <div class="text-center">
                  {{ mppt3[5] }}
              </div>
              <div class="text-center">
                  {{ mppt4[5] }}
              </div>
              <div class="text-center etiqueta texto-bold">
                  NOC
              </div>
            </div>
            <!-- 9 Columna UNDV -->
            <div class="col-1">
              <div class="text-center">
                  {{ mppt1[6] }}
              </div>
              <div class="text-center">
                  {{ mppt2[6] }}
              </div>
              <div class="text-center">
                  {{ mppt3[6] }}
              </div>
              <div class="text-center">
                  {{ mppt4[6] }}
              </div>
              <div class="text-center etiqueta texto-bold">
                  UNDV
              </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'MPPTInfo',
  computed: {
    ...mapState('fenix', ['mppt1', 'mppt2', 'mppt3', 'mppt4'])
  }
}
</script>

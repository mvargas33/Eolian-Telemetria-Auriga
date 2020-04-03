<template>
  <div class="padding-5">
    <div class="allCenter">
        <!-- BMS -->
    </div>
    <div class="row">
      <!-- 1 Etiquetas  -->
      <div class="col">
          <div class="text-left etiqueta texto-bold">
              Pack Abs I
          </div>
          <div class="text-left etiqueta texto-bold">
              Max Pack V
          </div>
      </div>
      <!-- 2 Valores  -->
      <div class="col">
          <div class="text-center">
              {{ bms[4] }} <!-- Pack Abs I -->
          </div>
          <div class="text-center">
              {{ bms[13] }} | {{ bms[5] }} <!-- Max_Volt_ID | Max_Volt -->
          </div>
      </div>
      <!-- 3 Current -->
      <div class="col">
          <div class="allCenter numero" style="color:red">
              {{ bms[1] }} <!-- Current -->
          </div>
      </div>
      <!-- 4 Etiqueta  -->
      <div class="col">
          <div class="text-left etiqueta texto-bold">
              Pack Open V
          </div>
          <div class="text-left etiqueta texto-bold">
              Min Pack V
          </div>
      </div>
      <!-- 5 Valores  -->
      <div class="col">
          <div class="text-center">
              {{ bms[3] }} <!-- Pack Open Voltage -->
          </div>
          <div class="text-center">
              {{ bms[14] }} | {{ bms[6] }} <!-- Min_Volt_ID | Min_Volt -->
          </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'BMSInfo',
  computed: {
    ...mapState('fenix', ['bms'])
  }
}
</script>

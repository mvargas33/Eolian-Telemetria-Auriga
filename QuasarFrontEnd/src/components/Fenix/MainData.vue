<template>
    <div class="row justify-center full-height full-width text-center">
        <!-- Panel izquierdo -->
        <div class="col">
            <div class="text-center">
                {{ kelly_izq[6] }} <!-- RPM -->
            </div>
            <div class="text-center">
                {{ kelly_izq[10] }} <!-- Motor_temp -->
            </div>
            <div class="text-center">
                {{ kelly_izq[11] }} <!-- Kelly_temp -->
            </div>
            <div class="text-center">
                {{ kelly_izq[8] }} <!-- PWM -->
            </div>
            <div class="text-center">
                {{ kelly_izq[9] }} <!-- EMR -->
            </div>
        </div>
        <!-- Velocímetro -->
        <div class="col-6">
            {{ mainData[0] }} <!-- Velocidad -->
        </div>
        <!-- Panel derecho -->
        <div class="col">
            <div class="text-center">
                {{ kelly_der[6] }} <!-- RPM -->
            </div>
            <div class="text-center">
                {{ kelly_der[10] }} <!-- Motor_temp -->
            </div>
            <div class="text-center">
                {{ kelly_der[11] }} <!-- Kelly_temp -->
            </div>
            <div class="text-center">
                {{ kelly_der[8] }} <!-- PWM -->
            </div>
            <div class="text-center">
                {{ kelly_der[9] }} <!-- EMR -->
            </div>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'MainData',
  computed: {
    ...mapState('fenix', ['mainData', 'kelly_der', 'kelly_izq'])
  }
}
</script>

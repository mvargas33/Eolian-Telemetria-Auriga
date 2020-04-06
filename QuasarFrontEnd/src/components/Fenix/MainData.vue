<template>
    <div class="padding-5">
        <div class="allCenter">
            <!-- Main Data -->
        </div>

        <div class="row">
            <!-- Panel izquierdo -->
            <div class="col">
                <div class="">
                    <div class="allCenter numero">
                        {{ kelly_izq[6] }} <!-- RPM -->
                    </div>
                    <div class="allCenter etiqueta">
                        RPM
                    </div>
                </div>
                <div class="">
                    <div class="allCenter numero">
                        {{ kelly_izq[10] }} <!-- Motor_temp -->
                    </div>
                    <div class="allCenter etiqueta">
                        T°Motor
                    </div>
                </div>
                <div class="">
                    <div class="allCenter numero">
                        {{ kelly_izq[11] }} <!-- Kelly_temp -->
                    </div>
                    <div class="allCenter etiqueta">
                        T°Kelly
                    </div>
                </div>
                <div class="">
                    <div class="allCenter numero">
                        {{ kelly_izq[8] }} <!-- PWM -->
                    </div>
                    <div class="allCenter etiqueta">
                        PWM
                    </div>
                </div>
                <div class="">
                    <div class="allCenter numero">
                        {{ kelly_izq[9] }} <!-- EMR -->
                    </div>
                    <div class="allCenter etiqueta">
                        EMR
                    </div>
                </div>
            </div>
            <!-- Velocímetro -->
            <div class="col-6">
                <div class="allCenter">
                    <div>
                        <div class="allCenter numero">
                            <Velocimetro></Velocimetro>
                            <!-- {{ mainData[0] }} Velocidad -->
                        </div>
                        <div class="allCenter etiqueta">
                            Km/hr
                        </div>
                    </div>
                </div>
            </div>
            <!-- Panel derecho -->
            <div class="col">
                <div class="">
                    <div class="allCenter numero">
                        {{ kelly_der[6] }} <!-- RPM -->
                    </div>
                    <div class="allCenter etiqueta">
                        RPM
                    </div>
                </div>
                <div class="">
                    <div class="allCenter numero">
                        {{ kelly_der[10] }} <!-- Motor_temp -->
                    </div>
                    <div class="allCenter etiqueta">
                        T°Motor
                    </div>
                </div>
                <div class="">
                    <div class="allCenter numero">
                        {{ kelly_der[11] }} <!-- Kelly_temp -->
                    </div>
                    <div class="allCenter etiqueta">
                        T°Kelly
                    </div>
                </div>
                <div class="">
                    <div class="allCenter numero">
                        {{ kelly_der[8] }} <!-- PWM -->
                    </div>
                    <div class="allCenter etiqueta">
                        PWM
                    </div>
                </div>
                <div class="">
                    <div class="allCenter numero">
                        {{ kelly_der[9] }} <!-- EMR -->
                    </div>
                    <div class="allCenter etiqueta">
                        EMR
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'
import Velocimetro from './Velocimetro'

export default {
  name: 'MainData',
  components: { Velocimetro },
  computed: {
    ...mapState('fenix', ['mainData', 'kelly_der', 'kelly_izq'])
  }
}
</script>

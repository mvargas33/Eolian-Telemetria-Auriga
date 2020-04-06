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
                <div class="allCenter" >
                    <VueSvgGauge
                    :start-angle="-145"
                    :end-angle="145"
                    :value=mainData[0]
                    :separator-step="20"
                    :scale-interval="5"
                    :inner-radius="75"
                    :gaugeColor="[{ offset: 0, color: '#347AB0' }, { offset: 100, color: '#8CDFAD' }]"
                    :transitionDuration="10"
                    >
                    <div class="col">
                        <div style="padding-top:60%">
                            <div class="allCenter numero">
                                {{ mainData[0] }}
                            </div>
                            <div class="allCenter">
                                Km/Hr
                            </div>
                        </div>
                    </div>
                    </VueSvgGauge>
                    <!-- {{ mainData[0] }} Velocidad -->
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
import { VueSvgGauge } from 'vue-svg-gauge'

export default {
  name: 'MainData',
  components: { VueSvgGauge },
  computed: {
    ...mapState('fenix', ['mainData', 'kelly_der', 'kelly_izq'])
  }
}
</script>

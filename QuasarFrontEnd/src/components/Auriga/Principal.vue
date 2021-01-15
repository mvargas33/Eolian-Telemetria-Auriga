<template>
    <div class="padding-5" style="width:100%">
        <div>
            <AlertasSuperiores></AlertasSuperiores>
        </div>
        <div class="row">
            <div class="col-5">
                <div class="allCenter" >
                    <VueSvgGauge
                    :min="0"
                    :max="150"
                    :start-angle="-145"
                    :end-angle="145"
                    :value="50"
                    :separator-step="20"
                    :scale-interval="5"
                    :inner-radius="75"
                    :gaugeColor="[{ offset: 0, color: '#347AB0' }, { offset: 100, color: '#8CDFAD' }]"
                    :transitionDuration="10"
                    >
                    <div class="col">
                        <div style="padding-top:60%">
                            <div class="allCenter numero">
                                {{ 50 }}
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
            <div class="col-2">
                <Marcha></Marcha>
                <KmRecorridos></KmRecorridos>
            </div>
            <div class="col-5">
                <div class="allCenter" >
                    <VueSvgGauge
                    :min="0"
                    :max="20"
                    :start-angle="-145"
                    :end-angle="145"
                    :value="10.8"
                    :separator-step="3"
                    :scale-interval="1"
                    :inner-radius="75"
                    :gaugeColor="[{ offset: 0, color: '#347AB0' }, { offset: 100, color: '#8CDFAD' }]"
                    :transitionDuration="10"
                    >
                    <div class="col">
                        <div style="padding-top:60%">
                            <div class="allCenter numero">
                                {{ 10.8 }}
                            </div>
                            <div class="allCenter">
                                KW
                            </div>
                        </div>
                    </div>
                    </VueSvgGauge>
                    <!-- {{ mainData[0] }} Velocidad -->
                </div>
            </div>
        </div>
        <div>
            <AlertasInferiores></AlertasInferiores>
        </div>
    </div>
</template>

<script>
import { VueSvgGauge } from 'vue-svg-gauge'
import Marcha from './Marcha'
import KmRecorridos from './KmRecorridos'
import AlertasSuperiores from './AlertasSuperiores'
import AlertasInferiores from './AlertasInferiores'

export default {
  name: 'Principal',
  components: { VueSvgGauge, Marcha, KmRecorridos, AlertasSuperiores, AlertasInferiores },
  data () {
    return { hola: 0 }
  }
}
</script>

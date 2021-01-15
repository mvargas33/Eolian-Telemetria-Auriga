<template>
    <div class="padding-5">
        <div class="row">
Hola
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
            </div>
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
        </div>
    </div>
</template>

<script>
import { VueSvgGauge } from 'vue-svg-gauge'
import Marcha from './Marcha'

export default {
  name: 'Principal',
  components: { VueSvgGauge, Marcha },
  data () {
    return { hola: 0 }
  }
}
</script>

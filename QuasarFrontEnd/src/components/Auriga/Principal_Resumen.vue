<template>
    <div class="padding-5" style="width:100%">
        <div>
            <AlertasSuperiores></AlertasSuperiores>
        </div>
        <div class="row">
            <div class="col-5">
                <div class="allCenter" >
                    <div class="col">
                        <div style="padding-top:0%">
                            <div class="allCenter biggernumero">
                                {{ 50 }}
                            </div>
                            <div class="allCenter biggertag">
                                Km/Hr
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-2">
                <Marcha></Marcha>
                <!--<KmRecorridos></KmRecorridos> -->
            </div>
            <div class="col-5">
                <div class="allCenter" >
                    <div class="col">
                        <div style="padding-top:0%">
                            <div class="allCenter biggernumero">
                                {{ 10.8 }}
                            </div>
                            <div class="allCenter biggertag">
                                KW
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div>
            <SocAuriga></SocAuriga>
        </div>
        <div>
            <AlertasInferiores></AlertasInferiores>
        </div>
    </div>
</template>

<script>
import Marcha from './Marcha'
import AlertasSuperiores from './AlertasSuperiores'
import AlertasInferiores from './AlertasInferiores'
import SocAuriga from './SocAuriga'

export default {
  name: 'Principal',
  components: { Marcha, AlertasSuperiores, AlertasInferiores, SocAuriga },
  data () {
    return { hola: 0 }
  }
}
</script>

<template>
  <f7-page name="velocimetro">
    <f7-navbar>
      <f7-nav-right>
        <img src="/static/logo.png" class="izq"/>
      </f7-nav-right>
    </f7-navbar>

    <f7-block>
      <f7-row>
        <f7-col >
          <div align="center">
            <vue-speedometer
              :fluidWidth="false"
              :width="220"

              :minValue="0"
              :maxValue="10"
              :needleHeightRatio="0.7"
              :maxSegmentLabels="6"
              :segments="20"
              :value="datos.potencia"
              :ringWidth="20"
              startColor="#2E3440"
              endColor="#4C566A"
              needleColor="#D8DEE9"
              currentValueText="${value} KW"
            />
          </div>
        </f7-col>
        <f7-col  width="40">
          <div align="center">
            <vue-speedometer
              :fluidWidth="false"
              :width="290"

              :minValue="0"
              :maxValue="120"
              :needleHeightRatio="0.7"
              :maxSegmentLabels="8"
              :segments="22"
              :value="datos.velocidad"
              :ringWidth="25"
              currentValueText="${value} Km/hr"
            />
          </div>
        </f7-col>
        
        <f7-col>
          <div align="center">
            <vue-speedometer
              :fluidWidth="false"
              :width="220"

              :minValue="0"
              :maxValue="100"
              :needleHeightRatio="0.7"
              :maxSegmentLabels="6"
              :segments="20"
              :value="datos.soc"
              :ringWidth="20"
              startColor="#2E3440"
              endColor="#4C566A"
              needleColor="#D8DEE9"
              currentValueText="${value} %"
            />
          </div>
        </f7-col>

      </f7-row>
    </f7-block>


    

  </f7-page>


</template>



<script>
import io from 'socket.io-client';
const socket = io('http://localhost:3000');
  //socket.on('connect', function(){console.log('connected')});
  //socket.on('event', function(data){});
  //socket.on('disconnect', function(){console.log('disconnected')});
//socket.on('broadcast', obj => {
  //console.log(obj);
  //vue.$emit('update', 90)
  //updateMyValues(obj);
  //$this.updateMyValues(obj);
  //datos.velocidad = 90;
  //datos.velocidad = obj.velocidad;
  //console.log(datos)
//})


import VueSpeedometer from "vue-speedometer";

export default {
  components: {VueSpeedometer},
  data: function () {
    return {
      datos: {velocidad: 0},
    }
  },

  sockets: {
    broadcast: function(data) {
      console.log('broadcast event in client received');
      console.log(data.velocidad);
      this.datos.velocidad = data.velocidad;
    }
  }

 
}


</script>

<style>
.izq{
    margin-right: 15px!important;
}
</style>
<template>
  <f7-page name="home">
    <f7-navbar>
      <f7-nav-right>
        <img src="/static/logo.png" class="izq"/>
      </f7-nav-right>
    </f7-navbar>

    <f7-block-title>Velocímetro (km/hr)</f7-block-title>

    <div align="center">
      <vue-speedometer
        :minValue="0"
        :maxValue="120"
        :needleHeightRatio="0.7"
        :maxSegmentLabels="12"
        :segments="22"
        :value="datos.velocidad"
        :width="350"
        :height="200"
        :ringWidth="30"
      />
    </div>

    <f7-block-title>Estado de carga (%)</f7-block-title>

    <div align="center">
      <vue-speedometer
        :minValue="0"
        :maxValue="100"
        :needleHeightRatio="0.7"
        :maxSegmentLabels="10"
        :segments="1000"
        :value= "98"
        :width="350"
        :height="200"
        :ringWidth="30"
      />
    </div>

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
      datos: {velocidad: 0}
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



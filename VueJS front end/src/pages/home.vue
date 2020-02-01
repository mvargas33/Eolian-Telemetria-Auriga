<template>
  <f7-page name="home">
    <f7-navbar>
      <f7-nav-right>
        <img src="/static/logo.png" class="izq"/>
      </f7-nav-right>
    </f7-navbar>

    <!-- Views/Tabs container -->
  <f7-views tabs class="safe-areas">
    <!-- Tabbar for switching views-tabs -->
    <f7-toolbar tabbar labels bottom>
      <f7-link tab-link="#view-home" tab-link-active icon-md="material:directions_car" text="General"></f7-link>
      <f7-link tab-link="#view-details" icon-md="material:list" text="Detalles"></f7-link>
      <f7-link tab-link="#view-modulos" icon-md="material:battery_charging_full" text="Módulos"></f7-link>
      <f7-link tab-link="#view-graphs" icon-md="material:show_chart" text="Gráficos"></f7-link>
      <f7-link tab-link="#view-map" icon-md="material:near_me" text="Mapa"></f7-link>
      <f7-link tab-link="#view-vel" icon-md="material:home" text="Velocimetro"></f7-link>
    </f7-toolbar>

    <!-- Your main view/tab, should have "view-main" class. It also has "tab-active" class -->
    <f7-view id="view-home" main tab tab-active url="/"></f7-view>

    <!-- Details View -->
    <f7-view id="view-details" name="details" tab url="/details/"></f7-view>

    <!-- Modulos View -->
    <f7-view id="view-modulos" name="modulos" tab url="/modulos/"></f7-view>

    <!-- Graphs View -->
    <f7-view id="view-graphs" name="graphs" tab url="/graphs/"></f7-view>

    <!-- Map View -->
    <f7-view id="view-map" name="map" tab url="/map/"></f7-view>

    <f7-view id="view-vel" name="vel" tab url="/velocimetro/"></f7-view>

  </f7-views>

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



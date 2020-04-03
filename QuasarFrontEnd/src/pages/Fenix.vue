<template>
  <q-page class="justify-between items-start">
    <div class="row wrap">
          <!-- Velocímetro, motores y paneles -->
          <div class="col-xs-12 col-sm-12 col-md-3 col-lg-3">
            <!-- Velocímetro y motores -->
              <MainData></MainData>
            <!-- Soc Bar -->
              <SocBar></SocBar>
            <!-- Motores [fases, ERR, Thr, Rev] -->
              <MotorInfo></MotorInfo>
            <!-- BMS [Voltage, current] -->
              <BMSInfo></BMSInfo>
            <!-- MPPT [Vin Iin Vout, parámetros ...] -->
              <MPPTInfo></MPPTInfo>
          </div>

          <!-- Gráficos -->
          <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
            <!-- Gráfico de Consumo -->
            <div class="padding-1">
                <ConsumoChart></ConsumoChart>
            </div>
            <!-- Gráfico de Temperaturas -->
            <div class="padding-1">
                <TempChart></TempChart>
            </div>
          </div>
          <!-- Averages, Banco de baterías -->
          <div class="col-xs-12 col-sm-12 col-md-3 col-lg-3">
            <!-- Info de módulos [max min temp/volt] -->
              <ModulosInfo></ModulosInfo>
            <!-- Banco de baterías -->
              <BancoBaterias></BancoBaterias>
          </div>
    </div>
  </q-page>
</template>

<script>
import { mapState, mapMutations } from 'vuex'

import MainData from '../components/Fenix/MainData'
import SocBar from '../components/Fenix/SocBar'
import MotorInfo from '../components/Fenix/MotorInfo'
import BMSInfo from '../components/Fenix/BMSInfo'
import MPPTInfo from '../components/Fenix/MPPTInfo'

import ConsumoChart from '../components/Fenix/ConsumoChart'
import TempChart from '../components/Fenix/TempChart'

import ModulosInfo from '../components/Fenix/ModulosInfo'
import BancoBaterias from '../components/Fenix/BancoBaterias'

export default {
  name: 'Fenix',
  components: { MainData, SocBar, MotorInfo, BMSInfo, MPPTInfo, ConsumoChart, TempChart, ModulosInfo, BancoBaterias },
  computed: {
    ...mapState('fenix', ['mainData']),
    ...mapState(['recibiendoDatos'])
  },
  methods: {
    ...mapMutations('fenix', ['updateVelocidad', 'updatePotencia'])
  }
}
</script>

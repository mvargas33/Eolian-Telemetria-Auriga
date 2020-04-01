<template>
  <q-page class="justify-between items-start">
    <div class="row">
          <!-- Velocímetro, motores y paneles -->
          <div class="col-3">
            <!-- Velocímetro y motores -->
            <div class="row wrap">
              <MainData></MainData>
            </div>
            <!-- Soc Bar -->
            <div class="row">
              <SocBar></SocBar>
            </div>
            <!-- Motores [fases, ERR, Thr, Rev] -->
            <div class="row">
              <MotorInfo></MotorInfo>
            </div>
            <!-- BMS [Voltage, current] -->
            <div class="row">
              <BMSInfo></BMSInfo>
            </div>
            <!-- MPPT [Vin Iin Vout, parámetros ...] -->
            <div class="row">
              <MPPTInfo></MPPTInfo>
            </div>
          </div>

          <!-- Gráficos -->
          <div class="col-6">
            <!-- Gráfico de Consumo -->
            <div class="row">
              <GraficoConsumo></GraficoConsumo>
            </div>
            <!-- Gráfico de Temperaturas -->
            <div class="row">
              <GraficoTemp></GraficoTemp>
            </div>
          </div>
          <!-- Averages, Banco de baterías -->
          <div class="col-3">
            <!-- Info de módulos [max min temp/volt] -->
            <div class="row">
              <ModulosInfo></ModulosInfo>
            </div>
            <!-- Banco de baterías -->
            <div class="row">
              <BancoBaterias></BancoBaterias>
            </div>
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

import GraficoConsumo from '../components/Fenix/GraficoConsumo'
import GraficoTemp from '../components/Fenix/GraficoTemp'

import ModulosInfo from '../components/Fenix/ModulosInfo'
import BancoBaterias from '../components/Fenix/BancoBaterias'

export default {
  name: 'Fenix',
  components: { MainData, SocBar, MotorInfo, BMSInfo, MPPTInfo, GraficoConsumo, GraficoTemp, ModulosInfo, BancoBaterias },
  computed: {
    ...mapState('fenix', ['mainData']),
    ...mapState(['recibiendoDatos'])
  },
  methods: {
    ...mapMutations('fenix', ['updateVelocidad', 'updatePotencia'])
  }
}
</script>

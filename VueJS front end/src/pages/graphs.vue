<template>
  <f7-page name="graphs" class="backgriser">
    <f7-navbar title="Gráficos"></f7-navbar>

    <f7-block-title>Gráfico de Consumo</f7-block-title>
    <f7-block strong>
      <grafico-linea :chart-data="datacollection"  :options="{
          legend: {
            display: false
          },
      }"
      ></grafico-linea>
    </f7-block>

  </f7-page>
</template>
<script>

import GraficoLinea from "../components/GraficoLinea.js";
import moment from 'moment';

export default {
  components: {
    GraficoLinea
  },
  computed: {
    datacollection: function() {
      let contador = [0,1,2,3,4,5,6,7,8,9];
      let ejey = [];
      let ejex = [];
      contador.forEach(function(dataset) {
	  	  ejex.push(moment().format('HH:mm:ss'));
			  ejey.push((Math.random() > 0.5 ? 1.0 : -1.0) * Math.round(Math.random() * 100)) 
		  });
      return {
        labels: ejex,
        datasets: [
          {
           label: 'Gráfico de Consumo',
            backgroundColor: '#f87979',
            pointBackgroundColor: '#FF9800',
            pointBorderColor: '#FF9800',
            data: ejey,
            borderWidth: 1,
            showLine: true,
            fill: false, 
          }
        ],
      }
    }
  }
}
</script>

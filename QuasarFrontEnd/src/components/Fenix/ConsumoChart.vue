<script>
import { Line } from 'vue-chartjs'
import 'chartjs-plugin-streaming'
import { mapState } from 'vuex'

var chartColors = {
  red: 'rgb(255, 99, 132)',
  orange: 'rgb(255, 159, 64)',
  yellow: 'rgb(255, 205, 86)',
  green: 'rgb(75, 192, 192)',
  blue: 'rgb(54, 162, 235)',
  purple: 'rgb(153, 102, 255)',
  grey: 'rgb(201, 203, 207)'
}

export default {
  extends: Line,
  computed: {
    ...mapState('fenix', ['data_grafico_1'])
  },
  watch: {
    // Cada vez que cambia el store se cambia el onRefresh que se queda pegado con el último valor del Store que se le dió a onRefresh,
    // de esta forma, el gráfico mustra el valor anterior hasta que cambie el store y se vuelva a cambiar el onRefresh con el nuevo valor
    data_grafico_1: function (newValue, oldValue) {
      this.$data._chart.options.scales.xAxes[0] =
      {
        type: 'realtime',
        realtime: {
          duration: 20000, // Ventana de tiempo
          refresh: 1, // Cada cuanto agregar un punto
          delay: 100, // Corrimiento a la derecha del grafico. Deve ser mayor que el refresh
          onRefresh: function (chart) {
            chart.config.data.datasets[0].data.push({
              x: Date.now(),
              y: newValue[0]
            })
            chart.config.data.datasets[1].data.push({
              x: Date.now(),
              y: newValue[1] * 10
            })
            chart.config.data.datasets[2].data.push({
              x: Date.now(),
              y: newValue[2] * 10
            })
          }
        }
      }
    }
  },
  // Configuración inicial antes que lleguen los datos
  mounted () {
    this.renderChart(
      // Data
      {
        datasets: [{
          label: 'Velocidad',
          backgroundColor: chartColors.red,
          borderColor: chartColors.red,
          fill: false,
          cubicInterpolationMode: 'monotone',
          pointRadius: 0, // No point
          data: []
        }, {
          label: 'Consumo',
          backgroundColor: chartColors.blue,
          borderColor: chartColors.blue,
          fill: false,
          cubicInterpolationMode: 'monotone',
          pointRadius: 0, // No point
          data: []
        }, {
          label: 'Paneles',
          backgroundColor: chartColors.yellow,
          borderColor: chartColors.yellow,
          fill: false,
          cubicInterpolationMode: 'monotone',
          pointRadius: 0, // No point
          data: []
        }]
      }
      ,
      // Options
      {
        responsive: true,
        maintainAspectRatio: false,
        title: {
          display: true,
          text: 'Consumo en Watts'
        },
        scales: {
          xAxes: [{
            type: 'realtime',
            realtime: {
              duration: 20000, // Ventana de tiempo
              refresh: 1, // Cada cuanto agregar un punto
              delay: 100, // Corrimiento a la derecha del grafico. Deve ser mayor que el refresh
              onRefresh: function (chart) {
                // console.log(chart)
                chart.config.data.datasets.forEach(function (dataset) {
                  dataset.data.push({
                    x: Date.now(),
                    y: 0
                  })
                })
              }
            }
          }],
          yAxes: [{
            scaleLabel: {
              display: true,
              labelString: 'value'
            }
          }]
        },
        tooltips: {
          mode: 'nearest',
          intersect: false
        },
        hover: {
          mode: 'nearest',
          intersect: false
        },
        legend: {
          position: 'bottom'
        }
      } // End Options
    ) // End renderChart
  } // End Mounted
}

</script>

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

// function randomScalingFactor () {
//   return (Math.random() > 0.5 ? 1.0 : -1.0) * Math.round(Math.random() * 100)
// }

// function onRefresh1 (chart) {
//   chart.config.data.datasets.forEach(function (dataset) {
//     dataset.data.push({
//       x: Date.now(),
//       y: randomScalingFactor()
//     })
//   })
// }

export default {
  extends: Line,
  computed: {
    ...mapState('fenix', ['mainData'])
  },
  watch: {
    mainData: function (newValue, oldValue) {
      // console.log('was: ', oldValue, ' now: ', newValue)
      this.$data._chart.data.datasets.forEach((dataset) => {
        dataset.data.push({
          x: Date.now(),
          y: newValue[0]
        })
      })
      this.$data._chart.update()
      // console.log(this.$data._chart)
      // this.updateChart(newValue)
    }
  },
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
          data: []
        }, {
          label: 'Consumo',
          backgroundColor: chartColors.blue,
          borderColor: chartColors.blue,
          fill: false,
          cubicInterpolationMode: 'monotone',
          data: []
        }, {
          label: 'Paneles',
          backgroundColor: chartColors.yellow,
          borderColor: chartColors.yellow,
          fill: false,
          cubicInterpolationMode: 'monotone',
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
            type: 'realtime'// ,
            // realtime: {
            //   duration: 20000,
            //   refresh: 1000,
            //   delay: 2000,
            //   onRefresh: function (chart) {
            //     console.log('onrefreshh')
            //     chart.config.data.datasets.forEach(function (dataset) {
            //       dataset.data.push({
            //         x: Date.now(),
            //         y: 0
            //       })
            //     })
            //   }
            // }
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
  }, // End Mounted
  methods: {
    updateChart (data) {
      this.chart.data.datasets.forEach((dataset) => {
        dataset.data.push(data[0])
      })
    }
  }
}

</script>

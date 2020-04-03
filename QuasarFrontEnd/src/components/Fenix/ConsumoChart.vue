<script>
import { Line } from 'vue-chartjs'
import 'chartjs-plugin-streaming'

var chartColors = {
  red: 'rgb(255, 99, 132)',
  orange: 'rgb(255, 159, 64)',
  yellow: 'rgb(255, 205, 86)',
  green: 'rgb(75, 192, 192)',
  blue: 'rgb(54, 162, 235)',
  purple: 'rgb(153, 102, 255)',
  grey: 'rgb(201, 203, 207)'
}

function randomScalingFactor () {
  return (Math.random() > 0.5 ? 1.0 : -1.0) * Math.round(Math.random() * 100)
}

function onRefresh (chart) {
  chart.config.data.datasets.forEach(function (dataset) {
    dataset.data.push({
      x: Date.now(),
      y: randomScalingFactor()
    })
  })
}

var data = {
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

var options = {
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
        duration: 20000,
        refresh: 1000,
        delay: 2000,
        onRefresh: onRefresh
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
}

export default {
  extends: Line,
  mounted () {
    this.renderChart(data, options)
  },
  methods: {
    updateChart (data) {
      this.renderChart({
        datasets: [{
          label: 'Dataset 1',
          borderColor: 'rgb(255, 99, 132)',
          backgroundColor: 'rgba(255, 99, 132, 0.5)',
          lineTension: 0,
          borderDash: [8, 4]
        }, {
          label: 'Dataset 2',
          borderColor: 'rgb(54, 162, 235)',
          backgroundColor: 'rgba(54, 162, 235, 0.5)'
        }]
      }, {
        scales: {
          xAxes: [{
            realtime: {
              onRefresh: function (chart) {
                chart.data.datasets.forEach(function (dataset) {
                  dataset.data.push({
                    x: Date.now(),
                    y: Math.random()
                  })
                })
              },
              delay: 2000
            }
          }]
        }
      })
    }
  }
}

</script>

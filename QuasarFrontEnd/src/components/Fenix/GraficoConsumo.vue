<template>
  <div class="small">
    <line-chart
    v-if="loaded"
    :chartData="datacollection"
    :options="options" />
    <button @click="fillData()">Randomize</button>
    <button @click="addData()">Add</button>
  </div>
</template>

<script>
import LineChart from './LineChart'

export default {
  name: 'GraficoConsumo',
  components: { LineChart },
  data () {
    return {
      loaded: false,
      datacollection: {},
      options: {}
    }
  },
  async mounted () {
    this.loaded = false
    try {
      this.datacollection = {
        labels: [],
        datasets: []
      }
      this.options = {
        responsive: true,
        maintainAspectRatio: false
      }
      this.loaded = true
    } catch (e) {
      console.error(e)
    }
  },
  methods: {
    fillData () {
      this.datacollection = {
        labels: [0, 1, 2, 3, 4],
        datasets: [
          {
            label: 'Velocidad',
            backgroundColor: '#08a6bb',
            data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]
          }, {
            label: 'Consumo',
            backgroundColor: '#f87979',
            data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]
          }
        ]
      }
    },
    addData () {
      this.datacollection.datasets[0].data.concat(10)
      this.datacollection.datasets[1].data.concat(11)
      this.datacollection = {
        ...this.datacollection,
        labels: this.datacollection.labels.concat(9)
      }
    },
    getRandomInt () {
      return Math.floor(Math.random() * (50 - 5 + 1)) + 5
    }
  }
}
</script>

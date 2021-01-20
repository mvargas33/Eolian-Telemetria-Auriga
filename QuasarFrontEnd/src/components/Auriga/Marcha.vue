<template>
    <div class="topCenter">
        <div>
            <span :style="estiloCondicional(marchaActual === 0)">
                D
            </span>
            <span :style="estiloCondicional(marchaActual === 1)">
                R
            </span>
            <span :style="estiloCondicional(marchaActual === 2)">
                C
            </span>
            <span :style="estiloCondicional(marchaActual === 3)">
                N
            </span>
        </div>
    </div>
</template>

<script>

export default {
  name: 'Marcha',
  data () {
    return { marchaActual: 2 }
  },
  methods: {
    estiloCondicional (marcha) {
      if (marcha === true) {
        return 'font-size: 350%; color: red'
      } else {
        return 'font-size: 250%; '
      }
    }
  }
}
</script>

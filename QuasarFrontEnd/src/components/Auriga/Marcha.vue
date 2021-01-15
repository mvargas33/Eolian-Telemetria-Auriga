<template>
    <div class="topCenter padding-5">
        <div class="marcha">
            <span :style="estiloCondicional(true)">
                D
            </span>
            <span :style="estiloCondicional(false)">
                R
            </span>
            <span :style="estiloCondicional(false)">
                C
            </span>
            <span :style="estiloCondicional(false)">
                N
            </span>
        </div>
    </div>
</template>

<script>

export default {
  name: 'Marcha',
  data () {
    return { hola: 0 }
  },
  methods: {
    estiloCondicional (marcha) {
      if (marcha === true) {
        return 'font-size: 35px; color: red'
      } else {
        return 'font-size: 25px; '
      }
    }
  }
}
</script>

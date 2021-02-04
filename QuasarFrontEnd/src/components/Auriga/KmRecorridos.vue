<template>
    <div class="botCenter padding-5">
        <div class="column">
            <div class="row allCenter km-recorridos">
                {{ 300.7 }}
            </div>
            <div class="row allCenter km-recorridos-label">
                Km Recorridos
            </div>
        </div>
    </div>
</template>

<script>

export default {
  name: 'KmRecorridos',
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

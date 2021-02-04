<template>
    <div class="row" style="padding-top: 3%">
        <div class="col allCenter alert-icon">
            <q-icon color="yellow" name="warning" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="red" name="local_fire_department" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="red" name="whatshot" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="red" name="build" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="red" name="error" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="purple" name="speed" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="green" name="battery_charging_full" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="yellow" name="battery_unknown" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="red" name="battery_alert" />
        </div>
    </div>
</template>

<script>

export default {
  name: 'AlertasInferiores',
  data () {
    return {
      hola: 0,
      musicON: true,
      gpsON: true,
      telemetryON: true
    }
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

<template>
    <div class="row" style="padding-bottom: 3%">
        <!-- Luces -->
        <div class="col allCenter alert-icon">
            <q-icon color="white" name="flare" />
        </div>
        <!-- Luces altas -->
        <div class="col allCenter alert-icon">
            <q-icon color="white" name="filter_tilt_shift" />
        </div>
        <!-- Intermitente izquierdo -->
        <div class="col allCenter alert-icon">
            <q-icon color="green" name="chevron_left" />
        </div>
        <!-- Intermitente derecho -->
        <div class="col allCenter alert-icon">
            <q-icon color="green" name="chevron_right" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="green" name="check_circle" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="green" name="eco" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="blue" name="explore" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="white" name="offline_bolt" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="white" name="power_settings_new" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon v-if="musicON" color="white" name="volume_up" />
            <q-icon v-if="!musicON" color="white" name="volume_off" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon v-if="gpsON" color="blue" name="location_on" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon v-if="telemetryON" color="blue" name="rss_feed" />
        </div>
        <div class="col allCenter alert-icon">
            <q-icon color="blue" name="toys" />
        </div>
    </div>
</template>

<script>

export default {
  name: 'AlertasSuperiores',
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

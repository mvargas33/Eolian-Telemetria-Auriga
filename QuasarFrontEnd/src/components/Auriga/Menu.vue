<template>
    <div>
        <q-toolbar class="bg-purple-10 text-white shadow-2 absolute-bottom">
            <q-space />
            <q-btn size="30px" flat round dense icon="home" type="a" href="/auriga" />
            <q-space />
            <q-btn size="30px" flat round dense icon="battery_charging_full" />
            <q-space />
            <q-btn size="30px" flat round dense icon="map" type="a" href="/map" />
            <q-space />
            <q-btn size="30px" flat round dense icon="show_chart"  type="a" href="/graphs" />
            <q-space />
        </q-toolbar>
    </div>
</template>

<script>
export default {
  name: 'Menu',
  components: {},
  data () {
    return { hola: 0 }
  }
}
</script>

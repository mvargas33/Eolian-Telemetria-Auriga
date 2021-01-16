<template>
  <q-page class="flex flex-center topCenter-auriga">
    <div class="col">
      <div class="row">
        <Principal></Principal>
      </div>
      <div class="row">
        <Temperaturas></Temperaturas>
      </div>
    </div>
  </q-page>
</template>

<script>
import Principal from '../components/Auriga/Principal'
import Temperaturas from '../components/Auriga/Temperaturas'

export default {
  name: 'Auriga',
  components: { Principal, Temperaturas }
}
</script>

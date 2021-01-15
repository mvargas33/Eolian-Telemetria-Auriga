<template>
  <q-page class="flex flex-center">
    <div class="col">
      <div class="row">
        <Principal></Principal>
      </div>
      <div class="row">
      </div>
    </div>
  </q-page>
</template>

<script>
import Principal from '../components/Auriga/Principal'

export default {
  name: 'Auriga',
  components: { Principal }
}
</script>

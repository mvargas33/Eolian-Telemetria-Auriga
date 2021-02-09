<template>
  <q-page class="flex flex-center topCenter-auriga">
    <div class="col">
      <div class="row">
        <PrincipalResumen></PrincipalResumen>
      </div>
      <div class="row">
        <Menu></Menu>
      </div>
    </div>
  </q-page>
</template>

<script>
import PrincipalResumen from '../components/Auriga/Principal_Resumen'
import Menu from '../components/Auriga/Menu'

export default {
  name: 'Auriga',
  components: { PrincipalResumen, Menu }
}
</script>

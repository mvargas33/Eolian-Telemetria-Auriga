<template>
    <div>
        SOC Bar: {{ mainData[3] }}
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'SocBar',
  computed: {
    ...mapState('fenix', ['mainData'])
  }
}
</script>

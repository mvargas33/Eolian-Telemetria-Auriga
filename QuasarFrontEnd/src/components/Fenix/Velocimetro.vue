<template>
    <div>
        <vue-speedometer
        :minValue="0"
        :maxValue="120"
        :needleHeightRatio="0.7"
        :maxSegmentLabels="12"
        :segments="22"
        :value=mainData[0]
        :width="350"
        :height="200"
        :ringWidth="30"
        />
    </div>
</template>

<script>
import VueSpeedometer from 'vue-speedometer'
import { mapState } from 'vuex'

export default {
  components: { VueSpeedometer },
  computed: {
    ...mapState('fenix', ['mainData'])
  }
}
</script>

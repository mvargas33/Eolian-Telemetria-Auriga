<template>
    <div class="padding-5">
        <div class="" v-for='(modulo, index) in bms_volt' :key="index" >
          <div class="row padding-1" v-if="index % 3 === 0">
            <div class="col">
              <div class="row">
                <div class="col-1">
                  {{ index + 1 }}
                </div>
                <div class="col">
                  <div class="row">
                    <div class="col allCenter">
                      {{ bms_temp[index*2] }}
                    </div>
                    <div>
                      |
                    </div>
                    <div class="col allCenter">
                      {{ bms_temp[index*2 + 1] }}
                    </div>
                  </div>
                  <div class="text-center numero">
                    {{ bms_volt[index] }}
                  </div>
                </div>
              </div>
            </div>
            <div class="col">
              <div class="row">
                <div class="col-1">
                  {{ (index + 1) + 1 }}
                </div>
                <div class="col">
                  <div class="row">
                    <div class="col allCenter">
                      {{ bms_temp[(index + 1)*2] }}
                    </div>
                    <div>
                      |
                    </div>
                    <div class="col allCenter">
                      {{ bms_temp[(index + 1)*2 + 1] }}
                    </div>
                  </div>
                  <div class="text-center numero">
                    {{ bms_volt[(index + 1)] }}
                  </div>
                </div>
              </div>
            </div>
            <div class="col">
              <div class="row">
                <div class="col-1">
                  {{ (index + 2) + 1 }}
                </div>
                <div class="col">
                  <div class="row">
                    <div class="col allCenter">
                      {{ bms_temp[(index + 2)*2] }}
                    </div>
                    <div>
                      |
                    </div>
                    <div class="col allCenter">
                      {{ bms_temp[(index + 2)*2 + 1] }}
                    </div>
                  </div>
                  <div class="text-center numero">
                    {{ bms_volt[(index + 2)] }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'BancoBaterias',
  computed: {
    ...mapState('fenix', ['bms_volt', 'bms_temp'])
  }
}
</script>

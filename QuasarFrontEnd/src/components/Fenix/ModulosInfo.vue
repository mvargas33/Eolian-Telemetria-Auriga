<template>
    <div class="padding-5">
        <div class="allCenter">
            <!-- Motor -->
        </div>
        <div class="row">
            <!-- 1 Columna High Temp -->
            <div class="col">
                <div class="text-center">
                  High
                </div>
                <div class="row text-center">
                  <div class="col">
                    <div class="allCenter numero">
                    {{ bms[8] }}
                    </div>
                  </div>
                  <div class="numero">
                  |
                  </div>
                  <div class="col">
                    <div class="allCenter numero">
                    {{ bms[7] }}
                    </div>
                  </div>
                </div>
            </div>
            <!-- 2 Columna Average Temp-->
            <div class="col">
                <div class="text-center">
                    Average
                </div>
                <div class="text-center">
                  <div class="allCenter numero">
                    {{ bms[11] }}
                  </div>
                </div>
            </div>
            <!-- 3 Columna Intenral Temp -->
            <div class="col">
                <div class="text-center">
                    Internal
                </div>
                <div class="text-center">
                  <div class="allCenter numero">
                    {{ bms[12] }}
                  </div>
                </div>
            </div>
            <!-- 4 Columna Low Temp-->
            <div class="col">
                <div class="text-center">
                  Low
                </div>
                <div class="row text-center">
                  <div class="col">
                    <div class="allCenter numero">
                    {{ bms[10] }}
                  </div>
                  </div>
                  <div class="numero">
                  |
                  </div>
                  <div class="col">
                    <div class="allCenter numero">
                    {{ bms[9] }}
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
  name: 'ModulosInfo',
  computed: {
    ...mapState('fenix', ['bms'])
  }
}
</script>

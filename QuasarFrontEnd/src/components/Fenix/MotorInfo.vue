<template>
    <div>
        <div class="allCenter">
            Motor
        </div>
        <div class="row">
            <!-- 1 Columna  -->
            <div class="col">
                <div class="text-center etiqueta texto-bold">
                    D
                </div>
                <div class="text-center etiqueta texto-bold">
                    I
                </div>
                <div class="text-center etiqueta">
                </div>
            </div>
            <!-- 2 Columna Ia -->
            <div class="col">
                <div class="text-center">
                    {{ kelly_der[0] }}
                </div>
                <div class="text-center">
                    {{ kelly_izq[0] }}
                </div>
                <div class="text-center etiqueta texto-bold">
                    Ia
                </div>
            </div>
            <!-- 3 Columna Ib -->
            <div class="col">
                <div class="text-center">
                    {{ kelly_der[1] }}
                </div>
                <div class="text-center">
                    {{ kelly_izq[1] }}
                </div>
                <div class="text-center etiqueta texto-bold">
                    Ib
                </div>
            </div>
            <!-- 4 Columna Ic-->
            <div class="col">
                <div class="text-center">
                    {{ kelly_der[2] }}
                </div>
                <div class="text-center">
                    {{ kelly_izq[2] }}
                </div>
                <div class="text-center etiqueta texto-bold">
                    Ic
                </div>
            </div>
            <!-- 5 Columna Va -->
            <div class="col">
                <div class="text-center">
                    {{ kelly_der[3] }}
                </div>
                <div class="text-center">
                    {{ kelly_izq[3] }}
                </div>
                <div class="text-center etiqueta texto-bold">
                    Va
                </div>
            </div>
            <!-- 6 Columna Vb -->
            <div class="col">
                <div class="text-center">
                    {{ kelly_der[4] }}
                </div>
                <div class="text-center">
                    {{ kelly_izq[4] }}
                </div>
                <div class="text-center etiqueta texto-bold">
                    Vb
                </div>
            </div>
            <!-- 7 Columna Vc-->
            <div class="col">
                <div class="text-center">
                    {{ kelly_der[5] }}
                </div>
                <div class="text-center">
                    {{ kelly_izq[5] }}
                </div>
                <div class="text-center etiqueta texto-bold">
                    Vc
                </div>
            </div>
            <!-- 8 Columna ERR -->
            <div class="col">
                <div class="text-center">
                    {{ kelly_der[7] }}
                </div>
                <div class="text-center">
                    {{ kelly_izq[7] }}
                </div>
                <div class="text-center etiqueta texto-bold">
                    Err
                </div>
            </div>
            <!-- 9 Columna Throttle -->
            <div class="col">
                <div class="text-center">
                    {{ kelly_der[12] }}
                </div>
                <div class="text-center">
                    {{ kelly_izq[12] }}
                </div>
                <div class="text-center etiqueta texto-bold">
                    Thr
                </div>
            </div>
            <!-- 10 Columna Reverse-->
            <div class="col">
                <div class="text-center">
                    {{ kelly_der[13] }}
                </div>
                <div class="text-center">
                    {{ kelly_izq[13] }}
                </div>
                <div class="text-center etiqueta texto-bold">
                    Rev
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'MotorInfo',
  computed: {
    ...mapState('fenix', ['kelly_izq', 'kelly_der'])
  }
}
</script>

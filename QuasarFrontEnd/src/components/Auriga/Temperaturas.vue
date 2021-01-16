<template>
    <div class="padding-1 temperaturas" style="width:100%">
        <div class="row">
            <div class="col-2 allCenter">
                Imagen
            </div>
            <div class="col">
                <div class="row">
                    <div class="column col allCenter">
                        <div class="row">
                            35°
                        </div>
                        <div class="row">
                            <q-badge style="width:100%" color="green">MIN</q-badge>
                        </div>
                    </div>
                    <div class="column col allCenter">
                        <div class="row">
                            35°
                        </div>
                        <div class="row">
                            <q-badge style="width:100%">AVG</q-badge>
                        </div>
                    </div>
                    <div class="column col allCenter">
                        <div class="row">
                            35°
                        </div>
                        <div class="row">
                            <q-badge style="width:100%" color="red">MAX</q-badge>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="row">
                    <div class="column col allCenter">
                        <div class="row">
                            3.92
                        </div>
                        <div class="row">
                            <q-badge style="width:100%" color="green">MIN</q-badge>
                        </div>
                    </div>
                    <div class="column col allCenter">
                        <div class="row">
                            3.81
                        </div>
                        <div class="row">
                            <q-badge style="width:100%">AVG</q-badge>
                        </div>
                    </div>
                    <div class="column col allCenter">
                        <div class="row">
                            4.12
                        </div>
                        <div class="row">
                            <q-badge style="width:100%" color="red">MAX</q-badge>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col">
                <div class="row allCenter">
                    Imagen
                </div>
                <div class="row">
                    <div class="col allCenter">
                        35°
                    </div>
                    <div class="col allCenter">
                        34°
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="row allCenter">
                    Imagen
                </div>
                <div class="row">
                    <div class="col allCenter">
                        35°
                    </div>
                    <div class="col allCenter">
                        34°
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="row allCenter">
                    Imagen
                </div>
                <div class="row">
                    <div class="col allCenter">
                        <div class="row">
                            <div class="col">
                                35°
                            </div>
                            <div class="col badge-etiqueta">
                                <q-badge align="bottom">1</q-badge>
                            </div>
                        </div>
                    </div>
                    <div class="col allCenter">
                        <div class="row">
                            <div class="col">
                                35°
                            </div>
                            <div class="col badge-etiqueta">
                                <q-badge align="bottom">2</q-badge>
                            </div>
                        </div>
                    </div>
                    <div class="col allCenter">
                        <div class="row">
                            <div class="col">
                                35°
                            </div>
                            <div class="col badge-etiqueta">
                                <q-badge align="bottom">3</q-badge>
                            </div>
                        </div>
                    </div>
                    <div class="col allCenter">
                        <div class="row">
                            <div class="col">
                                35°
                            </div>
                            <div class="col badge-etiqueta">
                                <q-badge align="bottom">4</q-badge>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

export default {
  name: 'Temperaturas',
  components: {},
  data () {
    return { hola: 0 }
  }
}
</script>

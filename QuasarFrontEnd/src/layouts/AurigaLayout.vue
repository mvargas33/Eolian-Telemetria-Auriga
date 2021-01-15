<template>
<!-- Layout principal de monitoreo de eolian Áuriga -->
    <q-layout view="hHh Lpr lff" :class="darkModeClass(darkMode)">
        <!-- Header -->
        <q-header elevated class="bg-purple-10">
            <q-toolbar >
                <q-btn
                flat
                dense
                round
                icon="menu"
                aria-label="Menu"
                @click="leftDrawerOpen = !leftDrawerOpen"
                />
                <q-toolbar-title>
                Eolian Áuriga
                </q-toolbar-title>
                <q-btn
                :loading="loading2"
                :percentage="percentage2"
                round
                :color="darkMode ? 'grey' : 'grey'"
                @click="turnDarkMode()"
                :icon="darkMode ? 'o_dark_mode' : 'dark_mode'"
                >
                </q-btn>
            </q-toolbar>
        </q-header>

        <!-- Left Drawer -->
        <q-drawer
            v-model="leftDrawerOpen"
            show-if-above="false"
            :breakpoint="0"
            elevated
            :content-class="darkModeClass(darkMode)"
        >
            <template v-for="(menuItem, index) in menuList">
                <q-item :key="index" clickable v-ripple
                >
                <q-item-section avatar>
                  <q-icon :name="menuItem.icon.icon" />
                </q-item-section>
                <q-item-section>
                  {{menuItem.label}}
                </q-item-section>
                </q-item>
            </template>
        </q-drawer>
        <q-page-container class="">
            <router-view />
        </q-page-container>

    </q-layout>
</template>

<script>
const menuList = [
  {
    icon: {
      icon: 'inbox',
      label: 'Inbox',
      size: 'xl',
      separator: true
    },
    label: 'Principal'
  },
  {
    icon: {
      icon: 'battery_charging_full',
      label: 'Outbox',
      separator: false
    },
    label: 'Baterías'
  },
  {
    icon: {
      icon: 'brightness_5',
      label: 'Trash',
      separator: false
    },
    label: 'Paneles solares'
  },
  {
    icon: {
      icon: 'offline_bolt',
      label: 'Spam',
      separator: true
    },
    label: 'Motores'
  },
  {
    icon: {
      icon: 'explore',
      label: 'Settings',
      separator: false
    },
    label: 'Navegación'
  }
]

export default {
  name: 'Auriga',
  data () {
    return {
      leftDrawerOpen: false,
      menuList,
      darkMode: true
    }
  },
  methods: {
    turnDarkMode () {
      this.darkMode = !this.darkMode
    },
    darkModeClass (mode) {
      if (mode === true) {
        return 'dark'
      } else {
        return 'light'
      }
    }
  }
}
</script>

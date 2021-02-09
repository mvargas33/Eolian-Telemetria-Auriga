const routes = [
  {
    path: '/',
    component: () => import('layouts/FenixLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Fenix.vue') }
    ]
  },
  {
    path: '/fenix',
    component: () => import('layouts/FenixLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Fenix.vue') }
    ]
  },
  {
    path: '/fenixbasic',
    component: () => import('layouts/EmptyLayout.vue'),
    children: [
      { path: '', component: () => import('pages/FenixBasic.vue') }
    ]
  },
  {
    path: '/auriga',
    component: () => import('layouts/AurigaLayout.vue'),
    children: [
      { path: '/auriga', component: () => import('pages/Auriga.vue') }
    ]
  },
  {
    path: '/aurigabasic',
    component: () => import('layouts/EmptyLayout.vue'),
    children: [
      { path: '', component: () => import('pages/AurigaBasic.vue') }
    ]
  },
  {
    path: '/map',
    component: () => import('layouts/AurigaLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Map.vue') }
    ]
  },
  {
    path: '/graphs',
    component: () => import('layouts/AurigaLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Graphs.vue') }

    path: '/aurigabaterias',
    component: () => import('layouts/AurigaLayout.vue'),
    children: [
      { path: '/aurigabaterias', component: () => import('pages/AurigaBaterias.vue') }

    ]
  }
]

// Always leave this as last one
if (process.env.MODE !== 'ssr') {
  routes.push({
    path: '*',
    component: () => import('pages/Error404.vue')
  })
}

export default routes

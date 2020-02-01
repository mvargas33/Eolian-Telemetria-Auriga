
import HomePage from '../pages/home.vue';
import DetailsPage from '../pages/details.vue';
import ModulosPage from '../pages/modulos.vue';
import GraphsPage from '../pages/graphs.vue';
import MapPage from '../pages/map.vue';
import NotFoundPage from '../pages/404.vue';

/* Nuevas páginas */

import VelocimetroPage from '../pages/velocimetro.vue';

var routes = [
  {
    path: '/velocimetro/',
    component: VelocimetroPage,
  },
  {
    path: '/',
    component: HomePage,
  },
  {
    path: '/details/',
    component: DetailsPage,
  },
  {
    path: '/modulos/',
    component: ModulosPage,
  },
  {
    path: '/graphs/',
    component: GraphsPage,
  },
  {
    path: '/map/',
    component: MapPage,
  },
  {
    path: '(.*)',
    component: NotFoundPage,
  },
];

export default routes;

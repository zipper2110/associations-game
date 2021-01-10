import Vue from 'vue'
import VueRouter from 'vue-router'
import Lobby from '../views/Lobby'
import Login from '../components/Login'
import PageNotFound from '../components/PageNotFound'
import store from '../store/index';
import auth from '../services/auth';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'lobby',
    component: Lobby,
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: { guest: true }
  },
  {
    path: '*',
    component: PageNotFound
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
  scrollBehavior () {
    return { x: 0, y: 0 }
  }
});

router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (auth.authCookiesExist() || store.getters.isAuthenticated) {
      next();
      return;
    }
    next({ name: 'login' });
  } else {
    next();
  }
});

router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.guest)) {
    if (auth.authCookiesExist() || store.getters.isAuthenticated) {
      next({ name: 'lobby' });
      return;
    }
    next();
  } else {
    next();
  }
});

export default router

import Vue from 'vue'
import VueI18n from 'vue-i18n'
import VueMeta from 'vue-meta'
import VueCookies from 'vue-cookies'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';
import en from './translations/en'
import ru from './translations/ru'

Vue.config.productionTip = false;

Vue.use(VueI18n);
Vue.use(VueMeta);
Vue.use(VueCookies);

const i18n = new VueI18n({
  locale: 'ru',
  messages: { en, ru }
});

new Vue({
  router,
  store,
  i18n,
  vuetify,
  render: h => h(App)
}).$mount('#app');

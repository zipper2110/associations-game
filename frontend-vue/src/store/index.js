import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    locale: 'en',
    dark_mode: true
  },
  mutations: {
    UPDATE_DARK_MODE (state, value) {
      state.dark_mode = value
    },
    SET_LOCALE (state, value) {
      state.locale = value
    },
  },
  actions: {
  },
  modules: {
  }
})

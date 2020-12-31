// noinspection NpmUsedModulesInstalled
import Api from '@/api/api'

const state = {
  user: null,
  token: null,
  expires_in: null,
  expire_ts: null
};
const getters = {
  isAuthenticated: state => !!state.user
};
const actions = {
  async LogIn ({ commit }, UserForm) {
    try {
      const result = await Api.HTTP.post('user/login', UserForm)
      if (result.data.access_token !== undefined) {
        await commit('setUser', UserForm.username);
        await commit('setToken', result.data.access_token);
        await commit('setExpireIn', result.data.expires_in);
        await commit('setExpireTs', (new Date().getTime() / 1000) + result.data.expires_in);
      }
    } catch (error) {
      console.log(error)
      await commit('setUser', null)
      await commit('setToken', null)
      await commit('setExpireIn', null)
      await commit('setExpireTs', null)
    }
  },
  async checkTokenExpiration ({ state }) {
    const time_diff = Math.floor(state.expire_ts - (new Date().getTime() / 1000));
    console.log(time_diff)
  },
  async updateUserInfoFromCookies ({ commit }, { token, user, expires_in, expire_ts }) {
    try {
      await commit('setUser', user)
      await commit('setToken', token)
      await commit('setExpireIn', expires_in)
      await commit('setExpireTs', expire_ts)
    } catch (error) {
      console.log(error)
    }
  },
  async LogOut ({ commit }) {
    commit('logOut')
  }
};
const mutations = {
  setUser (state, username) {
    state.user = username
  },
  setToken (state, token) {
    state.token = token
  },
  setExpireTs (state, expire_ts) {
    state.expire_ts = expire_ts
  },
  setExpireIn (state, expires_in) {
    state.expires_in = expires_in
  },
  logOut (state) {
    state.user = null
    state.token = null
  }
};
export default {
  state,
  getters,
  actions,
  mutations
}

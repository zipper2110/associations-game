// noinspection NpmUsedModulesInstalled
import Api from '@/api/api'

const state = {
  user: null,
  token: null
};
const getters = {
  isAuthenticated: state => !!state.user
};
const actions = {
  async LogIn ({ commit }, UserForm) {
    try {
      const result = await Api.HTTP.post('user/login', UserForm)
      if (result.data.access_token !== undefined) {
        await commit('setUser', UserForm.username)
        await commit('setToken', result.data.access_token)
      }
    } catch (error) {
      console.log(error)
      await commit('setUser', null)
      await commit('setToken', null)
    }
  },
  async updateUserInfoFromCookies ({ commit }, { token, user }) {
    try {
      await commit('setToken', token)
      await commit('setUser', user)
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

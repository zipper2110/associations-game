import Vue from 'vue'

export default {
  authCookiesExist () {
    return !!(
        Vue.$cookies.isKey('token') &&
        Vue.$cookies.isKey('user') &&
        Vue.$cookies.get('token') !== null &&
        Vue.$cookies.get('user') !== null
    )
  },
  getAuthToken () {
    return Vue.$cookies.get('token')
  },
  getAuthName () {
    return Vue.$cookies.get('user')
  },
  removeAuthData () {
    Vue.$cookies.remove('token')
    Vue.$cookies.remove('user')
  }
}

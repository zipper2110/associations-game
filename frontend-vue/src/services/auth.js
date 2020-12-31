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
  getTokenTimeout () {
    return Vue.$cookies.get('expire_ts')
  },
  getTokenExpiresIn () {
    return Vue.$cookies.get('expires_in')
  },
  removeAuthData () {
    Vue.$cookies.remove('token')
    Vue.$cookies.remove('user')
    Vue.$cookies.remove('expire_ts')
    Vue.$cookies.remove('expires_in')
  }
}

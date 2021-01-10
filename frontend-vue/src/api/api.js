import Axios from 'axios'
import auth from '../services/auth';
import router from '../router'
import store from '../store'

// Create HTTP object
const HTTP = Axios.create({
  baseURL: process.env.NODE_ENV === 'production' ? '/' : 'https://api.pangur.ru/v1',
  // withCredentials: true
})

// Add JWT token to the request is it's found
HTTP.interceptors.request.use(function (config) {
  if (auth.authCookiesExist()) {
    config.headers.Authorization = 'Bearer ' + auth.getAuthToken()
  }
  return config;
});

// Handle expired auth
HTTP.interceptors.response.use(undefined, async function (error) {
  if (error) {
    const originalRequest = error.config;
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      auth.removeAuthData()
      await store.dispatch('LogOut')
      await router.push({ name: 'login' });
    }
  }
})

export default {
  HTTP
}

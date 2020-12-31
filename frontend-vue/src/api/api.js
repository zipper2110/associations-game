import Axios from 'axios'
import auth from '../services/auth';
import router from '../router'
import store from '../store'

// Create HTTP object
const HTTP = Axios.create({
  baseURL: process.env.NODE_ENV === 'production' ? '/' : 'http://a-game-api',
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
  getAllSongs () {
    return HTTP.get('songs')
      .then(response => {
        return response.data.data
      })
      .catch(error => {
        console.log(error)
      })
  },
  getAllAuthors () {
    return HTTP.get('authors')
      .then(response => {
        return response.data.data
      })
      .catch(error => {
        console.log(error)
      })
  },
  getOneAuthorSongs (authorId) {
    return HTTP.get('author/' + authorId)
      .then(response => {
        return response.data.data
      })
      .catch(error => {
        console.log(error)
      })
  },
  getOneSong (id, chordsShift = 0) {
    return HTTP.get('song/' + id + '/' + chordsShift)
      .then(response => {
        return response.data.data
      })
      .catch(error => {
        console.log(error)
      })
  },
  getOneSongEdit (id) {
    return HTTP.get('edit-song/' + id)
      .then(response => {
        return response.data.data
      })
      .catch(error => {
        console.log(error)
      })
  },
  getUserSongs () {
    return HTTP.get('user-songs')
      .then(response => {
        return response.data.data
      })
      .catch(error => {
        console.log(error)
      })
  },
  getProfile () {
    return HTTP.get('profile')
      .then(response => {
        return response.data
      })
      .catch(error => {
        console.log(error)
      })
  },
  HTTP
}

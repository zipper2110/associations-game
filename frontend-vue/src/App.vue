<template>
  <v-app>
    <v-app-bar app>
      <div class="d-flex align-center">
        <h1 class="headline logo">
          <router-link :to="{ name: 'lobby' }" class="text-decoration-none">{{ $t('app_title') }}</router-link>
        </h1>
      </div>

      <v-spacer></v-spacer>

      <v-menu bottom left offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon slot="activator" v-bind="attrs" v-on="on">
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>

        <v-list>

          <v-list-item>
            <v-list-item-content>
              <v-select
                v-model="locale"
                :items="langs"
                dense
                solo
                @click.stop
                hide-details
              ></v-select>
            </v-list-item-content>
          </v-list-item>

          <v-list-item>
            <v-list-item-content>
              <v-switch
                v-model="dark_mode"
                flat
                prepend-icon="mdi-brightness-6"
                :label="$t('dark_mode')"
                @click.stop
                class="mt-0 v-input--reverse"
                hide-details
              ></v-switch>
            </v-list-item-content>
          </v-list-item>

          <v-list-item v-if="isLoggedIn">
            <v-list-item-content>
              <v-btn small :to="{ name: 'lobby' }" exact class="mr-2" >
                <v-icon left>mdi-account</v-icon>
                {{ $t('lobby') }}
              </v-btn>
            </v-list-item-content>
          </v-list-item>

          <v-list-item v-if="isLoggedIn">
            <v-list-item-content>
              <v-btn small @click="logout" exact class="mr-2">
                <v-icon left>mdi-logout</v-icon>
                {{ $t('logout') }}
              </v-btn>
            </v-list-item-content>
          </v-list-item>

        </v-list>
      </v-menu>

    </v-app-bar>

    <v-main>
      <transition name="fade" mode="out-in">
        <router-view
            :key="$route.fullPath"
            :username="username"
        ></router-view>
      </transition>
    </v-main>
  </v-app>
</template>

<script>
import auth from './services/auth'

export default {
  name: 'App',

  components: {
  },

  data () {
    return {
      langs: [
        {value: 'ru', text: 'Русский'},
        {value: 'en', text: 'English'}
      ],
      menuOpacity: 1,
      isMobile: false
    }
  },

  computed: {
    username: {
      get() {
        return this.$store.state.auth.user;
      }
    },
    dark_mode: {
      get() {
        return this.$store.state.dark_mode
      },
      set(value) {
        this.$vuetify.theme.dark = value;
        this.$store.commit('UPDATE_DARK_MODE', value);
        this.$cookies.set('dark', value);
      }
    },
    locale: {
      get() {
        return this.$store.state.locale
      },
      set(value) {
        this.$root.$i18n.locale = value;
        this.$store.commit('SET_LOCALE', value);
        this.$cookies.set('locale', value);
      }
    },
    isLoggedIn: function () {
      return this.$store.getters.isAuthenticated
    }
  },

  methods: {
    async logout () {
      auth.removeAuthData()
      await this.$store.dispatch('LogOut')
      await this.$router.push({ name: 'login' })
    }
  },

  mounted() {
    // Update user info from cookies
    if (auth.authCookiesExist()) {
      this.$store.dispatch('updateUserInfoFromCookies', {
        token: auth.getAuthToken(),
        user: auth.getAuthName(),
        expires_in: auth.getTokenExpiresIn(),
        expire_ts: auth.getTokenTimeout(),
      })

      // Set interval for updating token
      // const check_once_in_seconds = 1;
      // this.check_token_interval_id = setInterval(() => {
      //   this.$store.dispatch('checkTokenExpiration');
      // }, check_once_in_seconds * 1000)
    }
    else {
      auth.removeAuthData()
      this.$store.dispatch('LogOut');
    }

    // Get the theme from cookies
    if (this.$cookies.isKey('dark')) {
      const darkMode = this.$cookies.get('dark') === 'true';
      this.$vuetify.theme.dark = darkMode;
      this.$store.commit('UPDATE_DARK_MODE', darkMode);
    }

    // Get locale from cookies
    if (this.$cookies.isKey('locale')) {
      this.$root.$i18n.locale = this.$cookies.get('locale');
      this.$store.commit('SET_LOCALE', this.$cookies.get('locale'));
    }

    // Check if it's a mobile device
    this.isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
  },

  metaInfo: {
    title: 'Associations Game',
    titleTemplate: '%s | Associations Game'
  }
};
</script>

<style lang="scss">
  @import './styles/app.scss';
</style>

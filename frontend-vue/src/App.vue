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

        </v-list>
      </v-menu>

    </v-app-bar>

    <v-main>
      <HelloWorld/>
    </v-main>
  </v-app>
</template>

<script>
import HelloWorld from './components/HelloWorld';

export default {
  name: 'App',

  components: {
    HelloWorld,
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
    }
  },

  mounted() {
    // Get the theme from cookies
    if (this.$cookies.isKey('dark')) {
      const darkMode = this.$cookies.get('dark') === 'true';
      this.$vuetify.theme.dark = darkMode;
      this.$store.commit('UPDATE_DARK_MODE', darkMode);
    }

    // Get locale from cookies
    if (this.$cookies.isKey('locale')) {
      this.$root.$i18n.locale = this.$cookies.get('locale');
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

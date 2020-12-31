<template>
  <v-container>
    <v-row class="text-center">
      <v-col lg="2" md="4" sm="10" offset-lg="5" offset-md="4" offset-sm="1">
        <h1 class="mb-4 mt-8 headline">{{ $t('login') }}</h1>

        <div class="login">
          <div>
            <v-form v-model="form.valid" ref="form" @submit.prevent="submit">
              <v-text-field
                :label="$t('login')"
                v-model="form.login"
                :rules="loginRules"
                required
              ></v-text-field>
              <v-text-field
                :label="$t('password')"
                v-model="form.password"
                min="8"
                :type="'password'"
                :rules="passwordRules"
                required
              ></v-text-field>
              <p v-if="showError" class="error--text">{{ $t('login_error') }}</p>
              <br>
              <v-layout>
                <v-btn @click="submit">{{ $t('login_btn') }}</v-btn>
              </v-layout>
            </v-form>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'Login',
  computed: {
    t () {
      return this.$i18n.messages[this.$i18n.locale]
    },
    pageTitle () {
      return this.$i18n.messages[this.$i18n.locale].login
    },
    loginRules() {
      return [
        v => !!v || this.t.login_is_required,
        v => /^[a-zA-Z]+$/.test(v) || this.t.login_must_be_valid
      ];
    },
    passwordRules() {
      return [
        v => !!v || this.t.password_is_required
      ];
    }
  },
  data () {
    return {
      form: {
        valid: false,
        login: '',
        password: '',
      },
      showError: false
    };
  },
  methods: {
    ...mapActions(['LogIn']),
    async submit () {
      if (this.$refs.form.validate()) {
        try {
          await this.$store.dispatch('LogIn', {
            username: this.form.login,
            password: this.form.password,
          })
          if (this.$store.state.auth.token && this.$store.state.auth.user) {
            const token_timeout = this.$store.state.auth.expires_in;
            this.$cookies.set('token', this.$store.state.auth.token, token_timeout)
            this.$cookies.set('user', this.$store.state.auth.user, token_timeout)
            this.$cookies.set('expires_in', token_timeout, token_timeout)
            this.$cookies.set('expire_ts', (new Date().getTime() / 1000) + token_timeout)
            console.log(new Date().getTime() / 1000)
            console.log((new Date().getTime() / 1000) + token_timeout)

            await this.$router.push({ name: 'lobby' });
            this.showError = false
          } else {
            this.showError = true
          }
        } catch (error) {
          this.showError = true
        }
      }
    }
  },
  metaInfo () {
    return {
      title: this.pageTitle
    }
  },
  async created () {
    if (this.$cookies.isKey('token') && this.$cookies.isKey('user')) {
      try {
        await this.$router.push({ name: 'lobby' });
      } catch (error) {
        // console.log(error)
      }
    }
  }
}
</script>

<style scoped>
  * {
    box-sizing: border-box;
  }
  label {
    padding: 12px 12px 12px 0;
    display: inline-block;
  }
  button[type=submit] {
    background-color: #4CAF50;
    color: white;
    padding: 12px 20px;
    cursor: pointer;
    border-radius:30px;
  }
  button[type=submit]:hover {
    background-color: #45a049;
  }
  input {
    margin: 5px;
    box-shadow:0 0 15px 4px rgba(0,0,0,0.06);
    padding:10px;
    border-radius:30px;
  }
</style>

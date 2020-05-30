<template>
  <v-row
    align="center"
    justify="center"
    style="height: 90vh"
  >
    <v-col
      cols="12"
      sm="8"
      md="4"
    >
      <v-card class="elevation-12">
        <v-toolbar
          color="amber"
          flat
        >
          <v-toolbar-title>Login form</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form>
            <v-text-field
              label="Login or Email"
              name="login"
              prepend-icon="mdi-account"
              type="text"
              v-model="payload.login"
            ></v-text-field>

            <v-text-field
              id="password"
              label="Password"
              name="password"
              prepend-icon="mdi-lock"
              type="password"
              v-model="payload.password"
            ></v-text-field>
          </v-form>
        </v-card-text>
        <v-card-actions style="padding-right: 20px; padding-bottom: 20px;">
          <v-spacer></v-spacer>
          <v-btn @click="submit()" :disabled="check" color="amber">Sign in</v-btn>
        </v-card-actions>
      </v-card>
      <v-dialog
        v-model="dialog"
        width="500"
      >
        <v-card>
          <v-card-title
            class="headline amber lighten-2"
            primary-title
          >
            Error
          </v-card-title>

          <v-card-text style="padding-top: 15px">
            Login or Password is incorrect
          </v-card-text>

          <v-divider></v-divider>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="primary"
              text
              @click="dialog = false"
            >
              Ok
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-col>
  </v-row>
</template>

<script>
  export default {
    props: {
      source: String,
    },
    data: function () {
      return {
        payload: {
          login: "",
          password: ""
        },
        dialog: false
      }
    },
    methods: {
      submit() {
        this.$axios.get('http://api.rugrisser.ru/user/login', {
          params: {
            login: this.payload.login,
            password: this.payload.password,
          }
        }).then((response) => {
          console.log('test', response)
          this.$auth.$storage.setCookie('token', response.data)
          this.$router.push('/')
        }).catch((err) => {
          console.log('error while registering', err)
          this.dialog = true
        })
      }
    },
    computed: {
      check() {
        if(this.payload.login == "" || this.payload.password == "")
          return true
        else
          return false
      }
    }
  }
</script>
<template>
  <v-app id="keep">
    <v-app-bar
      app
      clipped-left
      color="amber"
    >
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      <nuxt-link style="text-decoration: none; color: black" to="/"><span class="title ml-3 mr-5">ITEM&nbsp;<span class="font-weight-light">Store</span></span></nuxt-link>
      <v-spacer></v-spacer>
    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      app
      clipped
      color="grey lighten-4"
    >
      <v-list
        dense
        class="grey lighten-4"
      >
        <template v-for="(item, i) in items">
          <v-row
            v-if="item.heading"
            :key="i"
            align="center"
          >
            <v-col cols="6">
              <v-subheader v-if="item.heading && item.log == (token != '')">
                {{ item.heading }}
              </v-subheader>
            </v-col>
          </v-row>
          <v-divider
            v-else-if="item.divider && item.log == (token != '')"
            :key="i"
            dark
            class="my-4"
          ></v-divider>
          <v-list-item
            v-else-if="item.log == (token != '') && item.link"
            :key="i"
            link
            :to="item.link"
          >
            <v-list-item-action>
              <v-icon>mdi-{{ item.icon }}</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="grey--text">
                {{ item.text }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-list-item
            v-else-if="item.log == (token != '') && item.action"
            :key="i"
            @click="act"
          >
            <v-list-item-action>
              <v-icon>mdi-{{ item.icon }}</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="grey--text">
                {{ item.text }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-list-item
            v-else-if="item.log == (token != '')"
            :key="i"
            @click="exit"
          >
            <v-list-item-action>
              <v-icon>mdi-{{ item.icon }}</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="grey--text">
                {{ item.text }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </template>
      </v-list>
      <v-row justify="center">
        <v-dialog v-model="dialog" persistent max-width="600px">
          <v-card>
            <v-card-title>
              <span class="headline">User Profile</span>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="8">
                    <v-text-field label="Title" v-model="title" required></v-text-field>
                  </v-col>
                  <v-col cols="4">
                    <v-text-field label="Price" v-model="price" required></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    <v-textarea label="Body" v-model="body" required></v-textarea>
                  </v-col>
                </v-row>
              </v-container>
              <small>*indicates required field</small>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="dialog = false">Close</v-btn>
              <v-btn color="blue darken-1" text @click="submit">Save</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-row>
    </v-navigation-drawer>

    <v-content>
      <v-container
        fluid
        class="grey lighten-4 fill-height"
        style="display: flex; justify-content: center; align-items: start"
      >
        <nuxt/>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
  export default {
    props: {
      source: String,
    },
    data: function() {
      return {
        drawer: null,
        items: [
          { icon: 'account-plus-outline', text: 'Sign Up', log: false, link: '/register' },
          { icon: 'account-arrow-right', text: 'Sign In', log: false, link: '/login' },
          { icon: 'alien-outline', text: 'Main page', log: true, link: '/'},
          { divider: true, log: true },
          { icon: 'plus-box-multiple-outline', action: true, text: 'Create new label', log: true },
          { divider: true, log: true },
          { icon: 'exit-run', action: false, text: 'Exit', log: true },
        ],
        token: "",
        dialog: false,
        title: "",
        price: "",
        body: ""
      }
    },
    mounted() {
      if(this.$auth.$storage.getCookie('token'))
        this.token = this.$auth.$storage.getCookie('token').token
    },
    methods: {
      act() {
        this.dialog = true
      },
      submit() {
        this.$axios.post('http://api.rugrisser.ru/post/create', {
          title: this.title,
          sub: this.price,
          body: this.body,
        },
        {
          headers: {
            'Content-Type': 'application/form-data',
            'Authorization': 'Bearer ' + this.token
          },
        }).then(()=>{
          dialog = false
        })
      },
      exit() {
        this.$auth.$storage.setCookie('token', "")
        this.token = ""
      }
    },
    watch: {
      '$route': function(){
        if(this.$auth.$storage.getCookie('token'))
          this.token = this.$auth.$storage.getCookie('token').token
      }
    }
  }
</script>

<style>
#keep .v-navigation-drawer__border {
  display: none
}
</style>
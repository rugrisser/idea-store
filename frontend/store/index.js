import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = () => new Vuex.Store({
  state: {
    posts: [],
    users: [],
    login: ""
  },
  mutations: {
    postsLoaded: (state, payload) => {
      state.posts = payload;
    },
    setUser: (state, payload) => {
      state.login = payload;
    },
    userLoaded: (state, payload) => {
      if(state.users.find(user => user.id === payload.id)){
        console.log('already loaded user')
      }
      else {
        console.log('user loaded')
        state.users.push(payload)
      }
    },
  },
  actions: {
    nuxtServerInit(ctx) {
      return ctx.dispatch('loadPosts')
    },
    loadPosts({ commit }) {
      return this.$axios.get('http://api.rugrisser.ru/post/get').then((response) => {
        commit('postsLoaded', response.data)
      })
    },
    loadUser({ commit }, payload) {
      return this.$axios.get('http://api.rugrisser.ru/user/' + payload.id).then((response) => {
        commit('userLoaded', response.data)
      })
    }
  },
  getters: {
    posts: (state) => {return state.posts},
    users: (state) => (id) => {return state.users.find(user => user.id == id)}
  }
})

export default store
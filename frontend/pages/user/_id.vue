<template>
  <div class="container" v-if="profile">
    <v-row style="margin-left: 2.8vw" class="text-left">
        <v-col cols="2">
            <v-img src="/pics/profile.png" style="max-width: 100%" />
        </v-col>
        <v-col cols="10" class="text-left">
            <p style="font-size: 20px">
                Известен как: <span style="text-decoration: underline #FF0000;">{{profile.login}}</span>
            </p>
            <p style="font-size: 20px">
                E-mail: <a :href="`mailto:${profile.email}`">{{profile.email}}</a>
            </p>
        </v-col>
    </v-row>

    <v-divider></v-divider>

    <v-row>
      <v-col v-for="post in userPosts" :key="post.id" cols="6">
        <userpost :title="post.title" :price="post.sub" :body="post.body" :id="post.user.id"/>
      </v-col>
    </v-row>

  </div>
</template>


<script>
import userpost from "@/components/userpost.vue"

export default {
  components: {
    userpost
  },
  data: function () {
    return {
      payload: {
        userId: this.$route.params.id, 
        title: "",
        body: ""
      }
    }
  },
  computed: {
    userPosts() {
      return this.$store.getters.posts.filter(post => post.user.id == this.$route.params.id)
    },
    profile() {
      return this.$store.getters.users(this.$route.params.id)
    },
  },
  mounted() {
    this.$store.dispatch('loadUser', {
      id: this.$route.params.id
    })
  }
};
</script>

<style>
.post {
  box-sizing: border-box;
  padding: 20px;
  margin: 10px 0;
  border: 1px solid #acacac;
  border-radius: 10px;
}
</style>
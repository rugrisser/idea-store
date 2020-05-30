<template>
  <div style="padding: 13px 0; width: 100%">
    <span style="font-size: 30px; font-weight: 800; padding-left: 7%;">“Everything you can imagine is real.”<span style="font-size: 15px; font-weight: 300">Pablo Picasso</span></span>
    <v-row>
      <v-col v-for="post in this.$store.getters['posts']" :key="post.id" cols="6">
        <post :title="post.title" :price="post.sub" :body="post.body" :id="post.user.id"/>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import post from "@/components/post.vue"

export default {
  components: {
    post
  },
  props: {
    source: String,
  },
  data: function() {
    return {}
  }
}
</script>

<style>
</style>
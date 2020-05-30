<template>
  <v-card style="padding: 15px; width: 34vw; margin: auto">
    <v-card-title primary-title>
      <div>
        <div class="headline">{{title}}</div>
        <span class="grey--text">{{price}}</span>
      </div>
    </v-card-title>
    <v-card-actions>
      <v-btn outlined="" :to="`/user/${id}`" color="purple">Explore Seller</v-btn>
      <v-spacer></v-spacer>
      <v-btn icon @click.native="show = !show">
        <v-icon>{{ show ? 'mdi-menu-down' : 'mdi-menu-up' }}</v-icon>
      </v-btn>
    </v-card-actions>
    <v-slide-y-transition>
      <v-card-text v-show="show">
        {{body}}
      </v-card-text>
    </v-slide-y-transition>
  </v-card>
</template>

<script>
export default {
  props: ['title', 'body', 'price', 'id'],
  data: function() {
    return {
      show: false
    }
  }
}
</script>

<style>

</style>

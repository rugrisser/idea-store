<template>
  <v-card style="padding: 15px; width: 34vw; margin: auto">
    <v-card-title primary-title>
      <div>
        <div class="headline">{{title}}</div>
        <span class="grey--text">{{price}}</span>
      </div>
    </v-card-title>
    <v-card-text>
      {{body}}
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  props: ['title', 'body', 'price', 'id'],
  data: function() {
    return {}
  }
}
</script>

<style>

</style>

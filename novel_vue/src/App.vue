<template>
  <div id="app">
    <NovelTable/>
  </div>
</template>

<script>
import NovelTable from './components/NovelTable.vue'

export default {
  name: 'App',
  components: {
    NovelTable
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

* {
  padding: 0;
  margin: 0;
}

body {
  background-color: #ddd;
}

</style>

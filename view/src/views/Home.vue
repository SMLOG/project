<template>
  <div>
    <Header />
    <div class="page-wrapper">
      <router-view></router-view>
    </div>

    <footer class="page-footer">
      <p class="mb-0">Copyright &copy; 2022.</p>
    </footer>
  </div>
</template>

<script>
import Header from "../components/Header.vue";
//import SideBar from "../components/SideBar.vue";
//import Switcher from "./components/Switcher.vue";
export default {
  name: "App",
  components: { Header },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>

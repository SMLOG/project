<template>
  <div>
    <Header />
    <div class="page-wrapper">
      <router-view></router-view>
    </div>
    <!--start overlay-->
    <div class="overlay toggle-icon"></div>
    <!--end overlay-->
    <!--Start Back To Top Button-->
    <a href="javaScript:;" class="back-to-top"
      ><i class="bx bxs-up-arrow-alt"></i
    ></a>
    <!--End Back To Top Button-->
    <footer class="page-footer">
      <p class="mb-0">Copyright &copy; 2021.</p>
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

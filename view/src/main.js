import Vue from "vue";
import App from "./App.vue";
import store from "./store"; //引入 Vuex 状态管理
import router from "./router";
import "./webfont";
import request from "@/utils/request";

// app.js
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";

//import "bootstrap/dist/css/bootstrap.css";
//import "bootstrap-vue/dist/bootstrap-vue.css";

// Install BootstrapVue
Vue.use(BootstrapVue);
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin);

Vue.config.productionTip = false;
Vue.prototype.$axios = request;

let vue = new Vue({
  router,
  store,
  render: (h) => h(App),
});
vue.$mount("#app");

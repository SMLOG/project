import Vue from "vue";
import App from "./App.vue";
import store from "./store"; //引入 Vuex 状态管理
import router from "./router";
import "./webfont";
import request from "@/utils/request";

//import "bootstrap/dist/css/bootstrap.css";
//import "bootstrap-vue/dist/bootstrap-vue.css";

Vue.config.productionTip = false;
Vue.prototype.$axios = request;

let vue = new Vue({
  router,
  store,
  render: (h) => h(App),
});
vue.$mount("#app");

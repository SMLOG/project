import Vue from "vue";
import VueRouter from "vue-router";
import store from "../store";
Vue.use(VueRouter);

const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return routerPush.call(this, location).catch((err) => err);
};
const routes = [
  {
    path: "/",
    component: () => import("@/views/Home"),
    name: "",
    redirect: "/index/1",
    children: [
      {
        path: "/profile",
        name: "用户资料",
        component: () => import("@/views/UserProfile"),
      },
      { path: "/admin/users", component: () => import("@/views/admin/Users") },
      {
        path: "/index/:pageId",
        name: "",
        component: () => import("@/views/Index"),
      },

      {
        path: "/newVote",
        name: "newVote",
        meta: { name: "发起投票" },

        component: () => import("@/views/NewVote"),
      },
      {
        path: "/vote/:voteId",
        name: "voteDetail",
        meta: { name: "投票详情" },
        component: () => import("@/views/VoteDetail"),
      },
      {
        path: "/houses",
        name: "houses",
        meta: { name: "房屋信息" },
        component: () => import("@/views/Houses"),
      },
    ],
  },
  { path: "/login", component: () => import("@/views/user/Login") },
  { path: "/reg", component: () => import("@/views/user/Register") },
  { path: "/forget", component: () => import("@/views/user/ForgetPass") },
];

const router = new VueRouter({
  routes,
  //mode: "history",
});

export default router;

router.afterEach((to) => {
  store.commit("updateNavName", to.meta.name || to.name);

  //next();
});

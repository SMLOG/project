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
        component: () => import("@/views/user/UserProfile"),
      },
      {
        path: "/unRegist",
        name: "注销账号",
        component: () => import("@/views/user/unRegist"),
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
        path: "/newActivity",
        name: "newActivity",
        meta: { name: "发起活动" },

        component: () => import("@/views/NewActivity"),
      },
      {
        path: "/houses",
        name: "houses",
        meta: { name: "房屋信息" },
        component: () => import("@/views/Houses"),
      },

      {
        path: "/costs/:period",
        name: "costs",
        meta: { name: "费用分摊" },
        component: () => import("@/views/Costs"),
      },
      {
        path: "/editCost/:period",
        name: "editCost",
        meta: { name: "费用分摊" },
        component: () => import("@/views/EditCost"),
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

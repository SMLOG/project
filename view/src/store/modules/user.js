import { login, regist } from "@/api/login"; //引入登录 api 接口
const user = {
  state: {
    loginUser: { userName: "no login", userId: "" },
    navName: "",
  },
  mutations: {
    updateUser(state, user) {
      state.loginUser = user;
    },
    userId(state, userId) {
      state.userId = userId;
    },
    updateNavName(state, navName) {
      state.navName = navName;
      console.log(navName);
    },
  },
  getters: {},
  actions: {
    // 登录
    login({ commit }, userInfo) {
      const userName = userInfo.userName.trim();
      return new Promise((resolve, reject) => {
        login(userName, userInfo.pwd, userInfo.spaceId)
          .then((response) => {
            commit("updateUser", { userName });
            resolve(response);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    regist({ commit }, userInfo) {
      const userName = userInfo.userName.trim();
      return new Promise((resolve, reject) => {
        regist(userName, userInfo.pwd, userInfo.spaceId)
          .then((response) => {
            commit("updateUser", { userName });
            resolve(response);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
  },
};
export default user;

import request from "@/utils/request"; //引入封装好的 axios 请求

export function login(userName, pwd, spaceId) {
  //登录接口
  return request({
    //使用封装好的 axios 进行网络请求
    url: "/public/login",
    method: "post",
    data: {
      //提交的数据
      userName,
      pwd,
      spaceId,
    },
  });
}

export function regist(userName, pwd, spaceId) {
  return request({
    url: "/public/reg",
    method: "post",
    data: {
      userName,
      pwd,
      spaceId,
    },
  });
}

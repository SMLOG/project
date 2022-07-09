<template>
  <div class="is-welcome">
    <div class="container-fluid">
      <div class="row justify-content-center" style="height: 100vh">
        <div class="align-self-center">
          <div class="p-0 container-fluid">
            <div class="row">
              <div class="col-sm-12 col-md-8 col-lg-6 mx-auto">
                <div class="card">
                  <div class="card-body">
                    <div class="card-title text-center h4">
                      <div>
                        <span v-if="welcome">{{ welcome }}</span
                        >业主在线投票
                      </div>
                      <div class="mt-3">注册</div>
                    </div>
                    <form
                      class="px-4 py-3 needs-validation"
                      novalidate
                      ref="form"
                    >
                      <div class="form-group pb-3">
                        <label for="userName">手机号码</label>
                        <input
                          type="number"
                          class="form-control"
                          id="userName"
                          placeholder="手机号码"
                          v-model="loginUser.userName"
                          required
                        />
                        <div class="invalid-feedback">
                          请输入正确的手机号码.
                        </div>
                      </div>
                      <div class="form-group pb-3">
                        <label for="nickName">你的称呼</label>
                        <input
                          type="text"
                          class="form-control"
                          id="nickName"
                          placeholder="比如,李先生，王女士"
                          v-model="loginUser.nickName"
                          required
                        />
                      </div>
                      <div class="form-group pb-3">
                        <label for="inviteCode">邀请码</label>
                        <input
                          type="text"
                          class="form-control"
                          id="inviteCode"
                          placeholder="请输入邀请码"
                          v-model="loginUser.inviteCode"
                          required
                        />
                      </div>
                      <div class="form-group pb-3">
                        <label for="pwd">设置密码</label>
                        <input
                          type="password"
                          class="form-control"
                          id="pwd"
                          v-model="loginUser.pwd"
                          placeholder="6~8位密码"
                          required
                        />
                        <div class="invalid-feedback">请输入登入密码.</div>
                      </div>
                      <div class="form-group pb-3">
                        <label for="pwd2">重复密码</label>
                        <input
                          type="password"
                          class="form-control"
                          id="pwd2"
                          placeholder="6~8位重复密码"
                          v-model="loginUser.pwd2"
                          required
                        />
                        <div class="invalid-feedback">请输入重复密码.</div>
                      </div>

                      <div class="form-group pb-3">
                        <label for="pwd2">图片验证码</label>
                        <input
                          class="form-control"
                          id="captcha"
                          placeholder="图片验证码"
                          v-model="loginUser.captcha"
                          required
                        />
                        <div class="invalid-feedback">请输入图片验证码.</div>

                        <img ref="captch" @click="getCaptch()" />
                      </div>

                      <div class="form-group">
                        <div class="container">
                          <button
                            @click="processForm()"
                            type="button"
                            class="btn btn-primary"
                          >
                            注册
                          </button>
                          <span
                            ><router-link to="/login">
                              已有账号?</router-link
                            ></span
                          >
                        </div>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      welcome: "",
      loginUser: {
        userName: "",
        pwd: "",
        pw2: "",
        nickName: "",
      },
      checkMail: 0,
    };
  },
  mounted() {
    this.getCaptch();
    this.welcome = this.$route.query.welcome;
  },
  methods: {
    getCaptch() {
      this.$refs.captch.src = "/api/public/captcha?t=" + +new Date();
    },
    processForm() {
      this.loading = true;
      this.$refs.form.classList.add("was-validated");

      if (this.$refs.form.checkValidity())
        this.$axios
          .post("/public/reg", this.loginUser)
          .then((response) => {
            this.loading = false;
            let code = response.data.code;
            if (code == 0) {
              this.$router.push({
                path: "/profile",
              });
            } else {
              alert(response.data.msg);
              this.getCaptch();
            }
          })
          .catch(() => {
            this.loading = false;
            this.getCaptch();
          });
    },
  },
};
</script>
<style scoped>
body .is-welcome {
  background: linear-gradient(to right, #ffdde1, #ee9ca7, #a7bfe8, #6190e8);
  background-size: 500% 500%;
  transition: background 3s ease;
  background-position: 50% 50%;
}

.is-welcome .btn {
  margin-top: 0.25rem;
  margin-bottom: 0.25rem;
}
</style>

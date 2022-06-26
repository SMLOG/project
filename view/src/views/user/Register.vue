<template>
  <div class="is-welcome">
    <div class="container-fluid">
      <div class="row justify-content-center" style="height: 100vh">
        <div class="align-self-center">
          <b-container fluid class="p-0" v-if="!checkMail">
            <div class="row">
              <div class="col-sm-12 col-md-8 col-lg-6 mx-auto">
                <b-card>
                  <div class="card-title text-center h4">注册</div>
                  <form
                    class="px-4 py-3 needs-validation"
                    novalidate
                    ref="form"
                  >
                    <div class="form-group pb-3">
                      <label for="userName">用户名</label>
                      <input
                        type="email"
                        class="form-control"
                        id="userName"
                        placeholder="邮件地址,比如 test@163.com"
                        v-model="loginUser.userName"
                        required
                      />
                      <div class="invalid-feedback">请输入正确的邮件地址.</div>
                    </div>
                    <div class="form-group pb-3">
                      <label for="userName">邀请码</label>
                      <input
                        type="text"
                        class="form-control"
                        id="nickName"
                        placeholder="请输入邀请码"
                        v-model="loginUser.inviteCode"
                        required
                      />
                    </div>
                    <div class="form-group pb-3">
                      <label for="pwd">密码</label>
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
                      <label for="pwd2">验证码</label>
                      <input
                        class="form-control"
                        id="captcha"
                        placeholder="验证码"
                        v-model="loginUser.captcha"
                        required
                      />
                      <div class="invalid-feedback">请输入验证码.</div>

                      <img ref="captch" @click="getCaptch()" />
                    </div>

                    <div class="form-group">
                      <b-container>
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
                      </b-container>
                    </div>
                  </form>
                </b-card>
              </div>
            </div>
          </b-container>
          <div class="container" v-if="checkMail">
            <div class="card">
              <p class="card-body">
                验证信息已经发送到你的邮箱地址:{{
                  loginUser.userName
                }},请前往激活。<a @click="checkMail = 0">返回登录</a>
              </p>
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
              this.checkMail = 1;
            } else {
              alert(response.data.msg);
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

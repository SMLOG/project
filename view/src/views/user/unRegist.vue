<template>
  <div class="page-content">
    <div class="container">
      <div class="main-body">
        <div class="row">
          <div class="col-lg-8">
            <div class="card mb-3">
              <div class="card-body">
                <form ref="form">
                  <div class="row mb-3">
                    <div class="col-sm-3">
                      <h6 class="mb-0">请输入你的登录密码</h6>
                    </div>
                    <div class="col-sm-9 text-secondary">
                      <input
                        type="password"
                        v-model="loginUser.pwd"
                        class="form-control"
                        required
                      />
                      <div class="invalid-feedback">请输入你的登录密码.</div>
                    </div>
                  </div>

                  <div class="row text-center">
                    <div class="col text-secondary">
                      <input
                        type="button"
                        class="btn btn-primary btn-sm px-4"
                        value="确定注销"
                        @click="unRegist()"
                      />
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
</template>

<script>
export default {
  components: {},
  data() {
    return {
      loginUser: {},
    };
  },

  mounted() {
    this.loadUserInfo();
  },
  methods: {
    unRegist() {
      this.$refs.form.classList.add("was-validated");

      this.$refs.form.checkValidity() &&
        this.$axios
          .post("/unRegist", this.loginUser)
          .then((r) => {
            if (r.data.code == 0) {
              alert(r.data.msg);
              this.$router.push("/login");
            } else alert(r.data.msg);
          })
          .catch(() => {
            this.$router.push("/login");
          });
    },
    loadUserInfo() {
      this.$axios
        .get("/loginUser")
        .then((r) => {
          if (r.data.code == 20001) {
            this.$store.commit("loginUser", {});
            this.$router.push("/login");
          } else if (r.data.code == 0)
            Object.assign(this.loginUser, r.data.data);
        })
        .catch(() => {
          this.$router.push("/login");
        });
    },
  },
};
</script>

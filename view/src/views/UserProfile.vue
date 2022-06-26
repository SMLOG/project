<template>
  <div class="page-content">
    <div class="container">
      <div class="main-body">
        <div class="row">
          <div class="col-lg-4">
            <div class="card mb-3">
              <div class="card-body">
                <div class="d-flex flex-column align-items-center text-center">
                  <font-awesome-icon
                    icon="fa-solid fa-user"
                    class="rounded-circle p-1 bg-primary"
                  />

                  <div class="mt-3">
                    <h4>{{ loginUser.userName }}</h4>
                    <p class="text-secondary mb-1">注册时间:</p>
                    <p class="text-muted font-size-sm">上次登录时间:</p>
                    <p class="text-muted font-size-sm">
                      <a class="btn btn-warning btn-sm">注销账户</a>
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="col-lg-8">
            <div class="card mb-3">
              <div class="card-body">
                <ul class="nav nav-tabs nav-primary" role="tablist">
                  <li class="nav-item" role="presentation">
                    <a
                      class="nav-link active"
                      data-bs-toggle="tab"
                      href="#baseInfo"
                      role="tab"
                      aria-selected="true"
                      >你的信息</a
                    >
                  </li>
                  <li class="nav-item" role="presentation">
                    <a
                      class="nav-link"
                      data-bs-toggle="tab"
                      href="#changePss"
                      role="tab"
                      >修改密码</a
                    >
                  </li>
                </ul>
                <div class="tab-content py-3">
                  <div
                    class="tab-pane fade active show"
                    id="baseInfo"
                    role="tabpanel"
                  >
                    <div class="row mb-3">
                      <div class="col-sm-3">
                        <h6 class="mb-0">你的称呼</h6>
                      </div>
                      <div class="col-sm-9 text-secondary">
                        <input
                          type="text"
                          v-model="loginUser.nickName"
                          class="form-control"
                        />
                      </div>
                    </div>

                    <div class="row mb-3">
                      <div class="col-sm-3">
                        <h6 class="mb-0">电子邮件地址</h6>
                      </div>
                      <div class="col-sm-9 text-secondary">
                        <input
                          type="email"
                          v-model="loginUser.email"
                          class="form-control"
                        />
                      </div>
                    </div>
                    <div class="row text-centered">
                      <div class="col text-secondary">
                        <input
                          type="button"
                          class="btn btn-primary px-4"
                          value="保存"
                        />
                      </div>
                    </div>
                  </div>
                  <div id="changePss" class="tab-pane fade" role="tabpanel">
                    <div class="row mb-3">
                      <div class="col-sm-3">
                        <h6 class="mb-0">旧密码</h6>
                      </div>
                      <div class="col-sm-9 text-secondary">
                        <input
                          type="password"
                          v-model="loginUser.oldPwd"
                          class="form-control"
                        />
                      </div>
                    </div>
                    <div class="row mb-3">
                      <div class="col-sm-3">
                        <h6 class="mb-0">新密码</h6>
                      </div>
                      <div class="col-sm-9 text-secondary">
                        <input
                          type="password"
                          v-model="loginUser.pwd"
                          class="form-control"
                        />
                      </div>
                    </div>
                    <div class="row mb-3">
                      <div class="col-sm-3">
                        <h6 class="mb-0">重复密码</h6>
                      </div>
                      <div class="col-sm-9 text-secondary">
                        <input
                          type="password"
                          v-model="loginUser.pwd2"
                          class="form-control"
                        />
                      </div>
                    </div>
                    <div class="row text-centered">
                      <div class="col text-secondary">
                        <input
                          type="button"
                          class="btn btn-primary px-4"
                          value="保存"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-sm-12">
                <div class="card mb-3">
                  <div class="card-body">
                    <div class="d-flex align-items-center mb-3">
                      <div class="pe-3 h5">房产</div>
                      <div class="ms-auto">
                        <div class="btn-group">
                          <button
                            type="button"
                            class="btn btn-primary btn-sm"
                            data-bs-toggle="modal"
                            data-bs-target="#houseModal"
                            @click="loadHouseList()"
                          >
                            添加房产
                          </button>
                          <div
                            class="modal fade"
                            id="houseModal"
                            tabindex="-1"
                            aria-labelledby="houseModalLabel"
                            aria-hidden="true"
                          >
                            <div class="modal-dialog modal-dialog-scrollable">
                              <div class="modal-content">
                                <div class="modal-header">
                                  <h5 class="modal-title" id="houseModalLabel">
                                    选择你的房子
                                  </h5>
                                  <button
                                    type="button"
                                    class="btn-close"
                                    data-bs-dismiss="modal"
                                    aria-label="Close"
                                  ></button>
                                </div>
                                <div class="modal-body">
                                  <div>
                                    <div>
                                      <ul class="list-group">
                                        <li
                                          v-for="h in houses"
                                          class="list-group-item"
                                          :key="h.houseId"
                                        >
                                          <input
                                            type="radio"
                                            name="selectHouse"
                                            :value="h.houseId"
                                            v-model="selectHouseId"
                                          />
                                          栋:{{ h.buildNo }} 楼层:{{
                                            h.floorNo
                                          }}
                                          单元号:{{ h.roomNo }}
                                        </li>
                                      </ul>
                                    </div>
                                  </div>
                                </div>
                                <div class="modal-footer">
                                  <button
                                    type="button"
                                    class="btn btn-secondary"
                                    data-bs-dismiss="modal"
                                  >
                                    关闭</button
                                  ><button
                                    type="button"
                                    class="btn btn-primary"
                                    @click="addHouse()"
                                  >
                                    添加
                                  </button>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="list-group list-group-flush">
                      <div
                        class="list-group-item px-0"
                        v-for="h in myHouses"
                        :key="h.id"
                      >
                        <div>
                          栋:{{ h.buildNo }} 楼层:{{ h.floorNo }} 单元号:{{
                            h.roomNo
                          }}
                          状态:{{
                            h.authStatu == 1
                              ? "待认证(" +
                                ((h.appvUserIds &&
                                  h.appvUserIds.split(",").length) ||
                                  0) +
                                "用户已通过)"
                              : h.authStatu == 2
                              ? "已认证"
                              : "未申请"
                          }}
                          <span v-if="h.authStatu == 1">
                            认证码:{{ h.authCode }}</span
                          >
                          <a
                            v-if="h.authStatu == 0"
                            class="btn btn-primary btn-sm"
                            @click="requestAuth(h)"
                            >申请认证</a
                          >
                          <a
                            class="btn btn-primary btn-sm ms-3 col"
                            @click="removeHouse(h)"
                            >删除</a
                          >
                        </div>
                      </div>
                    </div>
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
  components: {},
  data() {
    return {
      selectHouseId: null,
      myHouses: [],
      houses: [],
      loginUser: {},
    };
  },
  mounted() {
    this.$axios
      .get("/security/loginUser")
      .then((r) => {
        if (r.data.code == 20001) this.$router.push("/login");
        else if (r.data.code == 0) Object.assign(this.loginUser, r.data.data);
      })
      .catch(() => {
        this.$router.push("/login");
      });
    this.loadMyHouseList();
  },
  methods: {
    loadHouseList() {
      this.$axios.get("/houses").then((r) => {
        this.houses.length = 0;
        this.houses.push(...r.data.data);
      });
    },
    loadMyHouseList() {
      this.$axios.get("/myHouses").then((r) => {
        this.myHouses.length = 0;
        this.myHouses.push(...r.data.data);
      });
    },
    requestAuth(h) {
      this.$axios.post("/reqAuthMyHouse.json", h).then((r) => {
        if (r.data.code == 0) {
          alert(
            "申请成功,请发送你的申请码给其他用户(微信群或者你认识的验证通过业主)验证审核你的申请。"
          );
          this.loadMyHouseList();
        }
      });
    },
    removeHouse(h) {
      if (confirm("确定删除？"))
        this.$axios.post("/removeMyHouse", h).then((r) => {
          if (r.data.code == 0) {
            this.loadMyHouseList();
          }
        });
    },
    addHouse() {
      this.$axios
        .post("/addMyHouse", { houseId: this.selectHouseId })
        .then((r) => {
          if (r.data.code == 0) {
            $("#houseModal").modal("hide");
            this.loadMyHouseList();
          }
        });
    },
    onSubmit(evt) {
      evt.preventDefault();
      alert(JSON.stringify(this.form));
    },
    onReset(evt) {
      evt.preventDefault();
      // Reset our form values
      this.form.email = "";
      this.form.name = "";
      this.form.food = null;
      this.form.checked = [];
      // Trick to reset/clear native browser form validation state
      this.show = false;
      this.$nextTick(() => {
        this.show = true;
      });
    },
  },
};
</script>

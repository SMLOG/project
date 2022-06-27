<template>
  <div class="page-content">
    <div class="container">
      <div class="main-body">
        <div class="row">
          <div class="col-lg-4">
            <div class="card mb-3">
              <div class="card-body">
                <div class="">
                  <font-awesome-icon
                    icon="fa-solid fa-user"
                    class="rounded-circle p-1 bg-primary"
                  />

                  <div class="mt-3">
                    <p
                      class="text-secondary mb-1 d-flex justify-content-between"
                    >
                      <span>注册时间:</span
                      ><span>{{ loginUser.createDate | fmtDate }}</span>
                    </p>
                    <p
                      class="
                        text-muted
                        font-size-sm
                        d-flex
                        justify-content-between
                      "
                    >
                      <span>上次登录时间:</span
                      ><span>{{ loginUser.lastLoginTime | fmtTime }}</span>
                    </p>
                    <div class="d-flex justify-content-between">
                      <span>
                        业主房产:<em v-if="myHouses.length > 0"
                          >已绑定 认证码:{{ loginUser.authNum }}</em
                        ><em v-else>未绑定</em></span
                      >
                      <button
                        v-if="myHouses.length == 0"
                        type="button"
                        class="btn btn-primary btn-sm"
                        data-bs-toggle="modal"
                        data-bs-target="#houseModal"
                        @click="loadHouseList()"
                      >
                        绑定房产
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
                                选择你要绑定的房子
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
                                  <table
                                    class="
                                      table
                                      table-responsive
                                      table-striped
                                      table-hover
                                      table-sm
                                    "
                                  >
                                    <thead>
                                      <th>栋号</th>
                                      <th>楼层号</th>
                                      <th>房号</th>
                                      <th>选中</th>
                                    </thead>
                                    <tbody>
                                      <tr v-for="(h, i) in houses" :key="i">
                                        <td>栋号:{{ h.buildNo }}</td>
                                        <td>楼层:{{ h.floorNo }}</td>
                                        <td>
                                          单元号:{{ h.roomNo }}
                                          <span v-if="h.binderIds">*</span>
                                        </td>

                                        <td>
                                          <input
                                            :value="i"
                                            v-model="selectHouseIds"
                                            class="form-check"
                                            type="checkbox"
                                          />
                                        </td>
                                      </tr>
                                    </tbody>
                                  </table>
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
                                @click="bindHouses()"
                                :disabled="selectHouseIds.length == 0"
                              >
                                绑定选中的{{ selectHouseIds.length }}个房号
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <p class="text-muted font-size-sm text-start mt-3">
                      <a class="btn btn-warning btn-sm">注销账号</a>
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
                    <div class="row text-center">
                      <div class="col text-secondary">
                        <input
                          type="button"
                          class="btn btn-primary btn-sm px-4"
                          value="修改密码"
                          @click="changePass()"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row" v-if="myHouses.length > 0">
              <div class="col-sm-12">
                <div class="card mb-3">
                  <div class="card-body">
                    <div class="d-flex align-items-center mb-3">
                      <div class="pe-3 h5">已绑定房产</div>
                      <div class="ms-auto">
                        <div class="btn-group">
                          <a
                            class="bt btn-sm btn-primariy"
                            @click="unBindHouses()"
                            >解绑定</a
                          >
                        </div>
                      </div>
                    </div>
                    <div class="list-group list-group-flush">
                      <div>已绑定下面房产,但还没认证。</div>
                      <div
                        class="list-group-item px-0"
                        v-for="h in myHouses"
                        :key="h.id"
                      >
                        <div>
                          栋号:{{ h.buildNo }} 楼层:{{ h.floorNo }} 单元号:{{
                            h.roomNo
                          }}
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
      selectHouseIds: [],
      myHouses: [],
      houses: [],
      loginUser: {},
    };
  },
  filters: {
    fmtDate(date) {
      if (date) {
        let d = new Date();
        return d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
      }
      return "";
    },
    fmtTime(date) {
      if (date) {
        let d = new Date();
        return (
          d.getFullYear() +
          "-" +
          (d.getMonth() + 1) +
          "-" +
          d.getDate() +
          " " +
          d.getHours() +
          ":" +
          d.getMinutes()
        );
      }
      return "";
    },
  },
  mounted() {
    this.loadUserInfo();
    this.loadMyHouseList();
  },
  methods: {
    loadUserInfo() {
      this.$axios
        .get("/loginUser")
        .then((r) => {
          if (r.data.code == 20001) this.$router.push("/login");
          else if (r.data.code == 0) Object.assign(this.loginUser, r.data.data);
        })
        .catch(() => {
          this.$router.push("/login");
        });
    },
    changePass() {
      this.$axios
        .post("/changePass", this.loginUser)
        .then((r) => {
          if (r.data.code == 0) alert("修改成功");
          else alert(r.data.msg);
        })
        .catch(() => {
          alert("发生异常");
        });
    },
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
    unBindHouses() {
      if (confirm("确定解绑所有房子？"))
        this.$axios.post("/unBindHouses").then((r) => {
          if (r.data.code == 0) {
            this.loadMyHouseList();
            this.loadUserInfo();
          }
        });
    },
    bindHouses() {
      let bhs = this.houses.filter(
        (e, i) => this.selectHouseIds.indexOf(i) > -1
      );

      console.log(bhs);
      let checks = bhs.filter(
        (e) =>
          e.binderIds &&
          e.binderIds.split(",").filter((ee) => ee.trim()).length > 0
      );
      if (checks.length > 0) {
        if (
          !confirm(
            checks.map((e) => e.roomNo).join(",") +
              " 已经有人绑定,确定要继续吗？"
          )
        )
          return;
      }
      this.$axios.post("/bindHouses", bhs).then((r) => {
        if (r.data.code == 0) {
          $("#houseModal").modal("hide");
          this.loadMyHouseList();
          this.loadUserInfo();
        }
      });
    },
  },
};
</script>

<template>
  <div>
    <div class="card mb-3" v-if="authReqs.length > 0">
      <div class="card-body">
        <div class="h5 card-title">待验证绑定的房产</div>
        <div class="row">
          <ol class="list-group list-group-flush">
            <li
              class="list-group-item"
              v-for="auth in authReqs"
              :key="auth.userId"
            >
              <div class="">
                <div class="d-flex justify-content-between align-items-center">
                  <span
                    >{{ auth.user.nickName }}(***{{ auth.user.userName }})
                    绑定了以下房产:</span
                  >
                  <a
                    class="btn btn-primary btn-sm flex-shrink-0"
                    data-bs-toggle="modal"
                    data-bs-target="#houseApproveModal"
                    @click="showAuthInfo(auth)"
                    >验证</a
                  >
                </div>
              </div>
              <div class="d-flex justify-content-between align-items-center">
                <div>
                  <div v-for="uh in auth.list" :key="uh.id">
                    栋 {{ uh.buildNo }} 楼层 {{ uh.floorNo }} 单元号
                    {{ uh.roomNo }}
                    <div v-if="uh.binderUsers.length > 1" class="text-danger">
                      (
                      {{
                        uh.binderUsers
                          .filter((e) => e.id != auth.user.id)
                          .map((e) => "***" + e.userName)
                          .join(",")
                      }}
                      {{ uh.binderUsers.length - 1 }}人也绑定了此房产)
                    </div>
                  </div>
                </div>
              </div>
            </li>
          </ol>
        </div>
      </div>
    </div>
    <div
      class="modal fade"
      id="houseApproveModal"
      tabindex="-1"
      aria-labelledby="houseApproveModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="houseApproveModalLabel">
              业主房产绑定验证
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
                {{ auth.id }} 栋 {{ auth.buildNo }} 楼{{ auth.floorNo }} 单元号
                {{ auth.roomNo }} 绑定状态:{{
                  auth.authStatu == 2 ? "已验证" : "待验证"
                }}
              </div>
              <div class="mt-3">
                <label>请输入该业主提供的认证码:</label>
                <input class="form-control" v-model="authCode" />
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
              v-if="authCode"
              @click="approve(auth)"
              type="button"
              class="btn btn-primary"
            >
              确定是该业主的房产
            </button>
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
      authReqs: [
        {
          user: { nickName: "a" },
          list: [],
        },
      ],
      auth: { id: 0 },
      authCode: "",
      user: {},
    };
  },

  mounted() {
    this.loadAuthReqList();
  },
  methods: {
    hasApprove(auth) {
      console.log(this.userId);
      return (
        auth.appvUserIds &&
        auth.appvUserIds
          .split(",")
          .map((e) => parseInt(e))
          .indexOf(this.userId) > -1
      );
    },
    showAuthInfo(auth) {
      Object.assign(this.auth, auth);
    },
    loadAuthReqList() {
      this.$axios.get("/authReq.json").then((r) => {
        if (r.data.code == 0) {
          this.authReqs.length = 0;
          this.authReqs.push(...r.data.data.contents);
          console.log(this.authReqs);
          Object.assign(this.user, r.data.data.user);
        }
      });
    },
    approve(auth) {
      auth.authCode = this.authCode;
      this.$axios.post("/approveAuthReq.json", auth).then((r) => {
        if (r.data.code == 0) {
          this.loadAuthReqList();
          $("#houseApproveModal").modal("hide");
        } else alert(r.data.msg);
      });
    },
  },
};
</script>

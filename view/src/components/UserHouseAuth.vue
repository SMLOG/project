<template>
  <div>
    <div class="card mb-3" v-if="authReqs.length > 0">
      <div class="card-body">
        <div class="h5 card-title">正在申请认证的业主</div>
        <div class="list-group list-group-flush">
          <div class="list-group-item" v-for="auth in authReqs" :key="auth.id">
            {{ auth.id }}
            栋:{{ auth.buildNo }} 楼层:{{ auth.floorNo }} 单元号:{{
              auth.roomNo
            }}
            状态:{{
              auth.authStatu == 1
                ? "认证中"
                : auth.authStatu == 2
                ? "已认证"
                : "未认证"
            }}
            <a
              class="btn btn-primary btn-sm disabled"
              data-bs-toggle="modal"
              v-if="hasApprove(auth)"
              data-bs-target="#houseApproveModal"
              >你已验证</a
            >
            <a
              v-else
              class="btn btn-primary btn-sm"
              data-bs-toggle="modal"
              data-bs-target="#houseApproveModal"
              @click="showAuthInfo(auth)"
              >通过</a
            >
          </div>
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
            <h5 class="modal-title" id="houseApproveModalLabel">业主验证</h5>
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
                {{ auth.id }} 栋:{{ auth.buildNo }} 楼层:{{
                  auth.floorNo
                }}
                单元号:{{ auth.roomNo }} 状态:{{
                  auth.authStatu == 1
                    ? "认证中"
                    : auth.authStatu == 2
                    ? "已认证"
                    : "未认证"
                }}
              </div>
              <div>
                <label>请输入该业主提供的验证码:</label>
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
              通过
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
      authReqs: [],
      auth: { id: 0 },
      authCode: "",
      userId: "",
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
          this.userId = r.data.data.userId;
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

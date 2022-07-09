<template>
  <div class="page-content">
    <div class="card">
      <div class="card-body">
        <div class="mb-3">
          <div
            style="border-bottom: 1px dashed #ccc"
            class="col-form-label"
            for="name"
          >
            <b>主题:</b> {{ activity.subject }}({{
              typeFmt(activity.userType)
            }})
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-sm-12 form-label" style="font-weight: bold"
            >内容</label
          >
          <div class="col-sm-12 ms-3">
            {{ activity.descr }}
          </div>
        </div>
        <div class="py-3">
          <div class="float-end text-primary">
            <div v-if="status === 0">
              <span>进行中...</span
              ><span>截止时间 {{ activity.canEndDate }}</span>
            </div>
            <div v-else-if="status > 0">
              <span> 候选人报名已截止 截止时间 {{ activity.canEndDate }}</span>
            </div>
          </div>
          <div class="card-title text-primary">
            候选人报名(需至少{{ activity.atleaseCans }} 人报名)
          </div>

          <div class="col-sm-12">
            <div>
              <div class="row row-cols-1 row-cols-md-4 row-cols-xl-6">
                <div class="col" v-for="(c, ii) in candidates" :key="c.userId">
                  <div class="p-2 border">
                    <div class="d-flex justify-content-between">
                      <div class="flex-shrink-0">
                        <span class="ml-3" style="vertical-align: middle"
                          >#{{ ii + 1 }}
                        </span>
                        <input
                          style="vertical-align: middle"
                          type="checkbox"
                          v-model="c.selected"
                        />
                      </div>
                      <span
                        class="text-truncate ms-3"
                        v-if="userIdMap[c.userId]"
                        >{{ getUserHouse(c.userId) }}{{ c.nickName }}</span
                      >
                      <span class="text-truncate ms-3" v-else>已注销</span>
                    </div>
                    <div class="img-box">
                      <img
                        v-if="c.pic"
                        :src="'/api/pic/' + c.pic"
                        style="max-width: 100%"
                      />
                      <font-awesome-icon v-else icon="fa-solid fa-user" />
                    </div>
                    <div class="d-flex justify-content-between">
                      <div>
                        <a
                          data-bs-toggle="collapse"
                          :href="'#' + 'd' + ii"
                          aria-expanded="false"
                          >介绍</a
                        >
                      </div>
                      <div>
                        <span class="badge bg-success">{{ c.tikets }} 票 </span>
                      </div>
                    </div>
                    <div class="collapse" :id="'d' + ii">
                      {{ c.descr }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="d-flex justify-content-between">
          <div></div>
          <div>
            <div v-if="status === 0">
              <span v-if="candidates.some((e) => e.userId == userId)"
                >你已报名候选人</span
              >
              <button
                :disabled="candidates.some((e) => e.userId == userId)"
                class="btn btn-sm btn-primary"
                data-bs-toggle="modal"
                data-bs-target="#candidateModal"
                v-if="!candidates.some((e) => e.userId == userId)"
              >
                报名候选人
              </button>
              <button
                @click="unRegCandidate()"
                v-else
                class="btn btn-sm btn-warning"
              >
                取消报名
              </button>
              <ActivityRegCandidateModal @ok="getActivityDetail()" />
            </div>
          </div>
        </div>

        <div class="d-flex justify-content-between mt-2">
          <div v-if="status < 1">
            <span
              >开始时间:{{ activity.voteStartDate }} 截止时间:{{
                activity.voteEndDate
              }}</span
            >
          </div>
          <div v-else-if="status === 1">
            <span>进行中...</span
            ><span> 截止时间:{{ activity.voteEndDate }}</span>
          </div>
          <div v-else>
            <span>投票结束</span
            ><span> 截止时间:{{ activity.voteEndDate }}</span>
          </div>
          <div class="text-end">
            <button
              v-if="status < 1"
              :disabled="1"
              class="btn btn-sm btn-primary"
            >
              投票还没开始
            </button>
            <button
              v-else-if="status === 1"
              :disabled="logs.some((e) => e.userId == userId)"
              class="btn btn-sm btn-primary"
              @click="voteCandidate()"
            >
              <span v-if="logs.some((e) => e.userId == userId)">已</span>投票
            </button>
            <button v-else :disabled="1">已结束</button>
            <a
              class="btn btn-primary btn-sm ms-3"
              data-bs-toggle="modal"
              @click="viewVoteHis('#viewAllLogs')"
            >
              查看投票记录
            </a>
          </div>
        </div>
        <div>
          <div class="modal" tabindex="-1" id="viewAllLogs">
            <div
              class="modal-dialog modal-dialog-scrollable modal-dialog-centered"
            >
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">投票记录</h5>
                  <button
                    type="button"
                    class="btn-close btn-sm"
                    data-bs-dismiss="modal"
                    aria-label="Close"
                  ></button>
                </div>
                <div class="modal-body">
                  <table class="table">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th>投票人</th>
                        <th>日期</th>
                        <th>候选人</th>
                      </tr>
                    </thead>
                    <tr v-for="(log, i) in logs" :key="log.userId">
                      <td>{{ 1 + i }}</td>
                      <td>
                        {{ getUserHouse(log.userId) }}({{
                          getIdUserName(log.userId)
                        }})
                      </td>
                      <td>{{ log.createDate.split("T")[0] }}</td>
                      <td>
                        {{
                          log.canIds
                            .split(",")
                            .map((canId) =>
                              getIdUserName(canIdMap[canId].userId)
                            )
                            .join(",")
                        }}
                      </td>
                    </tr>
                  </table>
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
import ActivityRegCandidateModal from "@/components/ActivityRegCandidateModal";
export default {
  data() {
    return {
      userIdMap: {},
      canIdMap: {},
      userIdHouseMap: {},
      houseIdMap: {},
      activity: {
        subject: "",
        descr: "",
        canStartDate: "",
        canEndDate: "",
        voteStartDate: "",
        voteEndDate: "",
        atleaseCans: 0,
      },
      candidates: [],
      logs: [],
      userId: "",
      status: "",
    };
  },

  mounted() {
    this.getActivityDetail();
  },
  activated() {},
  methods: {
    viewVoteHis(id) {
      if (this.logs.some((u) => u.userId == this.userId)) {
        $(id).modal("show");
      } else alert("你还没投票,只有投票的用户才能查看.");
    },
    typeFmt(typeId) {
      return typeId == 1 || typeId == 3 ? "一房一票" : "一人一票";
    },
    getIdUserName(id) {
      if (this.userIdMap[id]) return this.userIdMap[id].userName;
      else return "已注销";
    },
    getUserHouse(userId) {
      if (this.userIdHouseMap[userId])
        return this.userIdHouseMap[userId]
          .map((houseId) => this.houseIdMap[houseId])
          .map((h) => h.roomNo)
          .join(",");
      return "";
    },

    unRegCandidate() {
      if (confirm("确定要取消报名吗？")) {
        let id = this.$route.params.activityId;
        this.$axios.post("/activityUnRegCandidate/" + id).then((r) => {
          if (r.data.code == 0) {
            this.getActivityDetail();
            alert("取消成功！");
          } else alert(r.data.msg);
        });
      }
    },
    getActivityDetail() {
      let id = this.$route.params.activityId;
      this.$axios.get("/activity/" + id).then((r) => {
        if (r.data.code == 0) {
          Object.keys(r.data.data).forEach((k) => {
            if (r.data.data[k] instanceof Array) {
              this[k].length = 0;
              this[k].push(...r.data.data[k]);
            } else if (r.data.data[k] instanceof Object) {
              Object.assign(this[k], r.data.data[k]);
            } else this[k] = r.data.data[k];
          });
          this.candidates.forEach((e) => {
            this.canIdMap[e.canId] = e;
          });
          for (var prop in this.userIdHouseMap) {
            delete this.userIdHouseMap[prop];
          }
          for (let log of this.logs.concat(this.candidates)) {
            if (!this.userIdHouseMap[log.userId]) {
              this.userIdHouseMap[log.userId] = log.houseIds
                .split(",")
                .filter((e) => e);
            }
          }
        }
      });
    },
    voteCandidate() {
      console.log(this.candidates);
      let selects = this.candidates.filter((e) => e.selected).length;
      if (selects == 0) {
        alert("请选择候选人");
        return;
      }
      if (selects > this.activity.atleaseCans) {
        alert("候选人数不能超过" + this.activity.atleaseCans + "ren");
        return;
      }
      if (selects > 0 && selects < this.activity.atleaseCans) {
        if (
          !confirm(
            "你还可以选择" +
              (this.activity.atleaseCans - selects) +
              "候选人" +
              ",继续提交点击‘确定’，重新选点击’取消‘."
          )
        )
          return;
      }
      this.$axios
        .post(
          "/activity/" + this.activity.activityId + "/voteCandidate/",
          this.candidates.filter((e) => e.selected)
        )
        .then((r) => {
          if (r.data.code == 0) {
            alert("投票成功！");
            this.getActivityDetail();
          } else alert(r.data.msg);
        });
    },
  },
  watch: {},
  components: { ActivityRegCandidateModal },
};
</script>

<style scoped>
.img-box {
  position: relative;
  width: 100%;
  height: 0;
  padding-bottom: 100%;
}
.img-box > * {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>

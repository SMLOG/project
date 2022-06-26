<template>
  <div class="page-content">
    <div class="card">
      <div class="card-body">
        <div class="mb-3">
          <div class="h3">
            {{ vote.subject }}
          </div>
        </div>
        <div class="row mb-3">
          <div class="col-sm-12 text-muted">
            开始时间:{{ vote.startDate }} 结束时间:{{ vote.endDate }}
          </div>
        </div>
        <div class="row mb-3">
          <div class="col-sm-12">
            {{ vote.descr }}
          </div>
        </div>
        <div class="row">
          <div class="mb-3">
            <div>
              投票选项
              <span v-if="vote.minSelect == vote.maxSelect"
                >需选择{{ vote.minSelect }}项</span
              >
              <span v-if="vote.minSelect != vote.maxSelect"
                >需选择{{ vote.minSelect }}~{{ vote.maxSelect }}项</span
              >
              (截止到目前,已有{{ vote.joinUserCount }} 人参与投票)
            </div>
            <ol class="list-group list-group-numbered">
              <li
                class="list-group-item d-flex gap-3"
                v-for="(opt, i) in voteOpts"
                :key="i"
              >
                <input
                  v-model="opt.checked"
                  :disabled="
                    voteLogs.length ||
                    (!opt.checked &&
                      vote.maxSelect <=
                        voteOpts.filter((e) => e.checked).length)
                  "
                  :checked="opt.checked"
                  type="checkbox"
                  class="form-check-input flex-shrink-0"
                />
                <div
                  class="d-flex w-100 justify-content-between align-self-center"
                >
                  <a>{{ voteOpts[i].name }} </a>

                  <span class="badge bg-success"
                    >{{ voteOpts[i].curCount }} 票
                  </span>
                </div>
              </li>
            </ol>
          </div>
        </div>
        <div class="row">
          <div class="col-12 mb-3 d-flex justify-content-between">
            <a
              @click="voteIt()"
              class="btn btn-primary btn-sm"
              :class="{
                disabled: voteLogs.length > 0,
                'btn-secondary': voteLogs.length > 0,
              }"
            >
              <span
                v-if="voting"
                class="spinner-border spinner-border-sm"
              ></span>
              {{ voteLogs.length == 0 ? "投票" : "你已投票" }}
            </a>

            <a
              class="btn btn-primary btn-sm"
              data-bs-toggle="modal"
              data-bs-target="#viewAllLogs"
            >
              查看全部投票记录
            </a>
          </div>
        </div>
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
                      <th scope="col">#</th>
                      <th scope="col">已选择</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr
                      v-for="(userLogs, i) in getUserLogs()"
                      :key="userLogs[0].id"
                    >
                      <th scope="row">{{ i + 1 }}</th>
                      <td>
                        <div v-for="log in userLogs" :key="log.id">
                          {{ optsMap[log.voteOpdId].name }}
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
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
      voting: 0,
      items: [],
      vote: {
        subject: "",
        descr: "",

        startDate: "",
        endDate: "",
        minSelect: 1,
        maxSelect: 1,
        voterRole: 1,
        canHideName: 0,
      },
      voteOpts: [],
      voteLogs: [],
      logs: [],
      optsMap: {},
    };
  },

  mounted() {
    this.loadVoteDetail();
  },
  activated() {},
  methods: {
    getUserLogs() {
      let ret = [];
      let logs = this.logs.slice().sort((a, b) => {
        return a.voteUserId - b.voteUserId;
      });
      let last;
      for (let i = 0; i < logs.length; i++) {
        if (!last || logs[i].voteUserId != last[0].voteUserId) {
          last = [];
          last.push(logs[i]);
          ret.push(last);
        } else last.push(logs[i]);
      }
      return ret;
    },
    voteIt() {
      (async () => {
        if (
          this.voteOpts.filter((e) => e.checked).length < this.vote.minSelect
        ) {
          alert("最少选择" + this.vote.minSelect + "项");
          return;
        } else if (
          this.voteOpts.filter((e) => e.checked).length > this.vote.maxSelect
        ) {
          alert("最多选择" + this.vote.maxSelect + "项");
          return;
        }
        this.voting = 1;

        await this.$axios
          .post(
            "/vote/" + this.$route.params.voteId + ".json",
            this.voteOpts.filter((e) => e.checked)
          )
          .then(() => {
            this.loadVoteDetail();

            alert("投票成功");
          })
          .catch(() => {
            alert("投票失败");
          });
        this.voting = 0;
      })();
    },
    loadVoteDetail() {
      this.$axios
        .get("/vote/" + this.$route.params.voteId + ".json")
        .then((r) => {
          Object.assign(this.vote, r.data.data.vote);
          this.voteLogs.length =
            this.voteLogs.length =
            this.voteOpts.length =
              0;
          this.voteOpts.push(...r.data.data.voteOpts);

          Object.assign(
            this.optsMap,
            this.voteOpts.reduce((map, item) => {
              map[item.optId] = item;
              return map;
            }, {})
          );

          console.log(this.optsMap);

          if (r.data.data.voteLogs.length > 0) {
            r.data.data.voteOpts.map((e) => {
              r.data.data.voteLogs.map((ee) => {
                if (ee.voteOpdId == e.optId) e.checked = 1;
              });
            });
          }
          this.voteLogs.push(...r.data.data.voteLogs);
          this.logs.length = 0;
          this.logs.push(...r.data.data.logs);
        });
    },
  },
  watch: {},
};
</script>
<style scoped>
</style>

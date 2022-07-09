<template>
  <div class="page-content">
    <form class="form needs-validation" ref="form">
      <div class="card">
        <div class="card-body">
          <div class="mb-3">
            <label class="col-form-label" for="name">主题:</label>
            <div>
              <input class="form-control" v-model="act.subject" required />
            </div>
          </div>
          <div class="row mb-3">
            <label class="col-sm-12">介绍内容</label>
            <div class="col-sm-12">
              <textarea
                maxlength="225"
                class="form-control"
                v-model="act.descr"
                required
              />
            </div>
          </div>
          <div class="row mb-3">
            <label class="col-sm-12">至少参选人名额数</label>
            <div class="col-sm-12">
              <input
                type="text"
                class="form-control"
                v-model="act.atleaseCans"
                required
              />
            </div>
          </div>

          <div>
            <div>参选人报名</div>

            <div class="row mb-3">
              <div class="col">
                报名截止日期:<input
                  class="form-control"
                  type="date"
                  v-model="act.canEndDate"
                  required
                />
              </div>
            </div>
          </div>
          <div>
            <div>投票</div>
            <div class="row mb-3">
              <div class="col">
                开始日期:<input
                  class="form-control"
                  type="date"
                  v-model="act.voteStartDate"
                  required
                />
              </div>
            </div>
            <div class="row mb-3">
              <div class="col">
                截止日期:<input
                  class="form-control"
                  type="date"
                  v-model="act.voteEndDate"
                  required
                />
              </div>
            </div>
            <div class="row mb-3">
              <div class="col">
                参与主体选项:<select
                  class="form-select"
                  type="date"
                  v-model="act.userType"
                  required
                >
                  <option value="0">全部用户(包括非业主，一人一票)</option>
                  <option value="1">已绑定房产用户(一房一票)</option>
                  <option value="2">已绑定房产用户(一人一票)</option>
                  <option value="3">已验证房产用户(一房一票)</option>
                  <option value="4">已验证房产用户(一人一票)</option>
                </select>
              </div>
            </div>
          </div>
          <div class="text-end">
            <button class="btn btn-primary" @click="publish()">发布</button>
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      items: [],
      act: {
        subject: "",
        descr: "",

        startDate: "",
        endDate: "",
        userType: 1,
      },
    };
  },

  mounted() {},
  activated() {},
  methods: {
    publish() {
      this.$refs.form.classList.add("was-validated");
      if (this.$refs.form.checkValidity())
        this.$axios.post("/activity", this.act).then((r) => {
          if (r.data.code == 0) {
            this.$router.push("/activity/" + r.data.data.activityId);
          } else alert(r.data.msg);
        });
    },
  },
  watch: {},
};
</script>
<style scoped></style>

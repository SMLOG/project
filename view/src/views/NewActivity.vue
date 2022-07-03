<template>
  <div class="page-content">
    <div class="card">
      <div class="card-body">
        <div class="mb-3">
          <label class="col-form-label" for="name">主题:</label>
          <div>
            <input class="form-control" v-model="vote.subject" />
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-sm-12">介绍内容</label>
          <div class="col-sm-12">
            <textarea
              maxlength="225"
              class="form-control"
              v-model="vote.descr"
            />
          </div>
        </div>

        <div class="row mb-3">
          <div class="col">
            开始时间:<input
              class="form-control"
              type="date"
              v-model="vote.startDate"
            />
          </div>
        </div>
        <div class="row mb-3">
          <div class="col">
            结束时间:<input
              class="form-control"
              type="date"
              v-model="vote.endDate"
            />
          </div>
        </div>
        <div class="text-end">
          <button class="btn btn-primary" @click="publish()">发布</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
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
      voteOpts: [
        {
          name: "",
        },
      ],
    };
  },

  mounted() {},
  activated() {},
  methods: {
    publish() {
      this.$axios
        .post("/vote", { vote: this.vote, voteOpts: this.voteOpts })
        .then((r) => {
          if (r.data.code == 0) {
            this.$router.push("/index/1");
          }
        });
    },
  },
  watch: {},
};
</script>
<style scoped>
</style>

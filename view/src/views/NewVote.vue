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
          <label class="col-sm-12">简介内容</label>
          <div class="col-sm-12">
            <textarea
              maxlength="225"
              class="form-control"
              v-model="vote.descr"
            />
          </div>
        </div>
        <div class="row">
          <div class="mb-3">
            <div>投票选项</div>
            <div class="input-group mb-3" v-for="(opt, i) in voteOpts" :key="i">
              选项{{ i + 1 }}:
              <input class="form-control" v-model="voteOpts[i].name" />
              <button
                type="button"
                class="btn-close ms-1"
                aria-label="Close"
                @click="voteOpts.length > 1 && voteOpts.splice(i, 1)"
              ></button>
            </div>
          </div>
          <div class="mb-3">
            <button class="btn-sm" @click="voteOpts.push({ name: '' })">
              添加选项
            </button>
          </div>
        </div>
        <div class="row mb-3">
          开始时间:<input
            class="form-control"
            type="date"
            v-model="vote.startDate"
          />
        </div>
        <div class="row mb-3">
          结束时间:<input
            class="form-control"
            type="date"
            v-model="vote.endDate"
          />
        </div>

        <div class="row mb-3">
          <label>最少选择:</label>
          <select v-model="vote.minSelect" class="form-select">
            <option v-for="i in voteOpts.length" :key="i">{{ i }}</option>
          </select>
        </div>
        <div class="row mb-3">
          <label>最多选择:</label>
          <select v-model="vote.maxSelect" class="form-select">
            <template v-for="i in voteOpts.length">
              <option v-if="i >= vote.minSelect" :key="i">{{ i }}</option>
            </template>
          </select>
        </div>
        <div class="row mb-3">
          <label>选择可投票人:</label>
          <select class="form-select" v-model="vote.voterRole">
            <option selected value="1">全体业主</option>
            <option value="2">物管委员会</option>
            <option value="3">业主委员会</option>
          </select>
        </div>
        <div class="row mb-3">
          <div class="checkbox">
            可匿名投票:<input v-model="vote.canHideName" type="checkbox" />
          </div>
        </div>
        <button class="btn btn-primary" @click="publish()">发布</button>
        <button class="btn btn-primary ms-3">匿名发布</button>
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

<template>
  <div class="page-content">
    <form class="form needs-validation" ref="form">
      <div class="card">
        <div class="card-body">
          <div class="mb-3">
            <label class="col-form-label" for="name">主题:</label>
            <div>
              <input
                required
                class="form-control"
                v-model="vote.subject"
                minlength="10"
                maxlength="200"
                type="text"
                placeholder="主题"
              />
              <div class="invalid-feedback">最少10个字符,最多200个字符.</div>
            </div>
          </div>
          <div class="row mb-3">
            <label class="col-sm-12">简介内容</label>
            <div class="col-sm-12">
              <textarea
                class="form-control"
                v-model="vote.descr"
                minlength="10"
                maxlength="200"
                required
              />
              <div class="invalid-feedback">最少10个字符,最多200个字符.</div>
            </div>
          </div>
          <div class="row">
            <div class="col mb-3">
              <div class="mb-1">投票选项</div>
              <div
                class="input-group mb-3"
                v-for="(opt, i) in voteOpts"
                :key="i"
              >
                选项{{ i + 1 }}:
                <input
                  required
                  class="form-control"
                  v-model="voteOpts[i].name"
                />
                <button
                  type="button"
                  class="btn-close ms-1"
                  aria-label="Close"
                  @click="voteOpts.length > 1 && voteOpts.splice(i, 1)"
                ></button>
              </div>
              <div>
                <button class="btn-sm" @click="voteOpts.push({ name: '' })">
                  添加选项
                </button>
              </div>
            </div>
          </div>

          <div class="row mb-3">
            <div class="col">
              截止日期:<input
                class="form-control"
                type="date"
                v-model="vote.endDate"
                required
              />
            </div>
          </div>

          <div class="row mb-3">
            <div class="col">
              <label>最少选择:</label>
              <select v-model="vote.minSelect" class="form-select" required>
                <option v-for="i in voteOpts.length" :key="i">{{ i }}</option>
              </select>
            </div>
          </div>
          <div class="row mb-3">
            <div class="col">
              <label>最多选择:</label>
              <select v-model="vote.maxSelect" class="form-select" required>
                <template v-for="i in voteOpts.length">
                  <option v-if="i >= vote.minSelect" :key="i">{{ i }}</option>
                </template>
              </select>
            </div>
          </div>
          <div class="row mb-3">
            <div class="col">
              参与主体选项:<select
                class="form-select"
                type="date"
                v-model="vote.userType"
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
      vote: {
        subject: "",
        descr: "",

        startDate: "",
        endDate: "",
        minSelect: 1,
        maxSelect: 1,
        voterRole: 1,
        userType: 0,
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
      this.$refs.form.classList.add("was-validated");
      if (this.$refs.form.checkValidity())
        this.$axios
          .post("/vote", { vote: this.vote, voteOpts: this.voteOpts })
          .then((r) => {
            if (r.data.code == 0) {
              this.$router.push("/index/1");
            } else alert(r.data.msg);
          });
    },
  },
  watch: {},
};
</script>
<style scoped></style>

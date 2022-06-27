<template>
  <div class="card my-3">
    <div class="card-body">
      <div class="row">
        <div class="col">
          <router-link to="/houses">房产绑定情况</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tabs: [
        { name: "投票讨论" },
        { name: "房产信息" },
        { name: "业主大会会议筹备组" },
        { name: "选举业主委员会" },
        { name: "首次成立业主大会" },
      ],
    };
  },

  mounted() {},
  activated() {},
  methods: {},
  watch: {},
};
</script>

<template>
  <div class="card my-3">
    <div class="card-body">
      <div class="row">
        <router-link to="/houses" class="col">小区房产绑定情况</router-link>
        <router-link to="/costs/index" class="col">费用分摊</router-link>
        <a
          class="col"
          v-for="link in links"
          :key="link.href"
          :href="link.href"
          target="_blank"
          >{{ link.title }}</a
        >
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      links: [
        {
          title: "湛江市物业管理暂行办法的通知",
          href: "https://www.zhanjiang.gov.cn/attachment/0/34/34912/1110749.pdf",
        },
      ],
    };
  },

  mounted() {},
  activated() {},
  methods: {},
  watch: {},
};
</script>

<template>
  <div class="card my-3">
    <div class="card-body">
      <div class="row">
        <router-link to="/houses" class="col">房产绑定情况</router-link>
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
      tabs: [
        { name: "投票讨论" },
        { name: "房产信息" },
        { name: "业主大会会议筹备组" },
        { name: "选举业主委员会" },
        { name: "首次成立业主大会" },
      ],
      links: [
        {
          title: "业主大会召开及业主委员会成立表格汇总",
          href: "https://page.om.qq.com/page/Op2IqaFNmc1YTAHcGHD6FemQ0",
        },
        {
          title: "湛江市物业管理条例（草案)",
          href: "https://www.zhanjiang.gov.cn/zjsfw/bmdh/sfj/zwgk/flfg/content/post_1284185.html",
        },
        {
          title: "物业管理暂行办法的通知",
          href: "https://www.zhanjiang.gov.cn/attachment/0/34/34912/1110749.pdf",
        },
        {
          title: "成立业主大会筹款倡议书",
          href: "https://max.book118.com/html/2021/0308/8056064043003056.shtm",
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

<template>
  <div class="d-flex">
    <ul class="nav">
      <li class="nav-item">
        <router-link :to="'' + prevPeriod()" class="nav-link"
          ><font-awesome-icon icon="fa-solid fa-backward"
        /></router-link>
      </li>
      <li class="nav-item">
        <span class="nav-link active">{{ period }}期</span>
      </li>
      <li class="nav-item">
        <router-link :to="'' + nextPeriod()" class="nav-link"
          ><font-awesome-icon icon="fa-solid fa-forward"
        /></router-link>
      </li>
    </ul>
    <div class="ms-auto">
      <router-link
        v-if="!isEdit"
        :to="'/editCost/' + period"
        class="btn btn-sm btn-primary mb-3 nav-item"
        >编辑</router-link
      >
      <router-link
        v-if="isEdit"
        :to="'/costs/' + period"
        class="btn btn-sm btn-primary mb-3 nav-item"
        >返回</router-link
      >
    </div>
  </div>
</template>

<script>
export default {
  props: ["period", "isEdit"],

  methods: {
    prevPeriod() {
      return (this.period % 100) - 1 > 0
        ? this.period - 1
        : parseInt((this.period / 100 - 1) * 100) + 11;
    },
    nextPeriod() {
      return (this.period % 100) + 1 < 13
        ? this.period - 0 + 1
        : (parseInt(this.period / 100) + 1) * 100 + 1;
    },
  },
};
</script>
<style scoped></style>

<template>
  <div class="page-content">
    <div class="card mb-3">
      <div class="card-body">
        <CostsNavBar :period="period" :isEdit="0" />
        <div>
          <table
            class="table table-responsive table-bordered table-striped table-hover table-sm"
          >
            <thead class="text-center">
              <tr>
                <th rowspan="2" class="align-middle">序号</th>
                <th rowspan="2" class="align-middle">表名</th>
                <th rowspan="2" class="align-middle">主体</th>
                <th colspan="2" class="text-center">本期</th>
                <th colspan="2" class="text-center">上期</th>
                <th rowspan="2" class="align-middle">用量</th>
                <th rowspan="2" class="align-middle">损耗</th>
                <th rowspan="2" class="align-middle">单价</th>
                <th rowspan="2" class="align-middle">合计</th>
              </tr>
              <tr>
                <th>日期</th>
                <th>读数</th>
                <th>日期</th>
                <th>读数</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(h, i) in costs" :key="i">
                <td>{{ i + 1 }}</td>
                <td>{{ h.code }}({{ h.type }})</td>
                <td>{{ h.buildNo ? h.buildNo : "全体" }}</td>

                <td>
                  {{ h.cutDate }}
                </td>

                <td>{{ h.val1 }}</td>
                <td></td>
                <td>{{ h.pval1 }}</td>
                <td>{{ (h.val1 - h.pval1).toFixed(2) }}</td>
                <td>{{ h.val2 }}</td>
                <td>{{ h.val3 }}</td>
                <td>{{ ((h.val1 - h.pval1) * h.val3).toFixed(2) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <HouseInit v-if="showBatch" />
  </div>
</template>

<script>
import HouseInit from "@/components/HouseInit";
import CostsNavBar from "@/components/CostsNavBar";
export default {
  data() {
    return {
      period: 0,
      showBatch: 0,
      costs: [{}],
      selectHouseIds: [],
      userMap: { 0: "" },
    };
  },

  mounted() {
    this.getCost();
  },
  components: { HouseInit, CostsNavBar },
  activated() {},
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
    getCost() {
      try {
        this.period = 0;
        this.$route.params.period != "index" &&
          (this.period = this.$route.params.period);
      } catch (e) {
        console.error(e);
      }
      this.$axios.get("/cost/" + this.period || 0).then((r) => {
        this.costs.length = 0;
        if (r.data.code == 0) {
          this.costs.push(...r.data.data);
          if (r.data.data.length > 0) this.period = this.costs[0].period;
        }
      });
    },
  },
  watch: {
    $route() {
      this.getCost();
    },
  },
};
</script>
<style scoped></style>

<template>
  <div class="page-content">
    <div class="card">
      <div class="card-body">
        <CostsNavBar :period="period" :isEdit="1" />
        <ul class="nav nav-tabs">
          <li
            class="nav-item"
            v-for="(config, i) in configs"
            :key="config.name"
          >
            <a
              class="nav-link"
              :class="{ active: configIndex == i }"
              aria-current="page"
              @click="configIndex = i"
              >{{ config.name }}</a
            >
          </li>
        </ul>

        <div>
          <ul class="nav nav-pills my-3">
            <li
              class="nav-item"
              v-for="(cat, i) in configs[configIndex].filter"
              :key="cat"
            >
              <a
                class="text-sm-center nav-link"
                :class="{ active: filterIndex == i }"
                @click="filterIndex = i"
                >{{ cat }}</a
              >
            </li>
          </ul>
          <div id="costs">
            <div
              class="mb-3 item"
              v-for="(cost, i) in configs[configIndex].items.filter(
                (e) =>
                  filterIndex == 0 ||
                  e.type == configs[configIndex].filter[filterIndex] ||
                  e.buildNo == configs[configIndex].filter[filterIndex]
              )"
              :key="cost.buildNo + '_' + cost.name"
            >
              <div>{{ cost.buildNo || cost.type }}</div>
              <label class="col-form-label" for="name">
                表名{{ cost.code }}:<b>{{ cost.name }}</b></label
              >
              <div class="ps-3">
                <div class="d-flex justify-content-between">
                  <span class="form-label">抄表日期</span
                  ><span
                    v-if="i == 0"
                    class="btn btn-sm"
                    @click="allValSame(cost.cutDate, 'cutDate')"
                    >全部相同</span
                  >
                </div>
                <input
                  class="form-control"
                  type="date"
                  v-model="cost.cutDate"
                />
                <div>
                  <label class="form-label">本期读数</label>
                  <input class="form-control" v-model="cost.val1" />
                </div>
                <div>
                  <div class="d-flex justify-content-between">
                    <span class="form-label">损耗</span
                    ><span
                      v-if="i == 0"
                      class="btn btn-sm"
                      @click="allValSame(cost.val2, 'val2')"
                      >全部相同</span
                    >
                  </div>
                  <input class="form-control" v-model="cost.val2" />
                </div>
                <div class="form-label">
                  <div class="d-flex justify-content-between">
                    <span class="form-label">单价</span
                    ><span
                      v-if="i == 0"
                      class="btn btn-sm"
                      @click="allValSame(cost.val3, 'val3')"
                      >全部相同</span
                    >
                  </div>

                  <input class="form-control" v-model="cost.val3" />
                </div>
                <div>
                  <label class="form-label">上期读数</label>
                  <input class="form-control" v-model="cost.pval1" />
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="text-end sticky-bottom">
          <button class="btn btn-primary" @click="save()">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
let builds = ["1栋", "2栋", "3栋", "4栋", "5栋", "6栋", "7栋", "8栋"];
import CostsNavBar from "@/components/CostsNavBar";
export default {
  data() {
    return {
      configIndex: 0,
      filterIndex: 0,
      selectBuild: 0,
      type: 1,
      period: "",
      curConfig: {},
      configs: [
        {
          name: "公共分摊",
          filter: ["全部", "电", "水"],
          items: [
            {
              name: "公共照明",
              code: "公共照明",
              type: "电",
              val1: "",
              val2: "",
              val3: "",
              buildNo: "",
              cutDate: "",
              pval1: "",
            },
            {
              name: "应急公共照明",
              code: "应急公共照明",
              type: "电",
              val1: "",
              val2: "",
              val3: "",
              buildNo: "",
              cutDate: "",
            },
            {
              name: "屋面泛光照明",
              code: "屋面泛光照明",
              type: "电",
              val1: "",
              val2: "",
              val3: "",
              buildNo: "",
            },
            {
              name: "备用(4116)园林",
              code: "备用(4116)园林",
              type: "电",
              val1: "",
              val2: "",
              val3: "",
              buildNo: "",
            },
            {
              name: "生活水泵房",
              code: "生活水泵房",
              type: "电",
              val1: "",
              val2: "",
              val3: "",
              buildNo: "",
            },
            {
              name: "消防泵房",
              code: "消防泵房",
              type: "电",
              val1: "",
              val2: "",
              val3: "",
              buildNo: "",
            },
            {
              name: "专变电房",
              code: "专变电房",
              type: "电",
              val1: "",
              val2: "",
              val3: "",
              buildNo: "",
            },
            {
              name: "消防中心",
              code: "消防中心",
              type: "电",
              val1: "",
              val2: "",
              val3: "",
              buildNo: "",
            },
            {
              name: "发电机房",
              code: "发电机房",
              type: "电",
              val1: "",
              val2: "",
              val3: "",
              buildNo: "",
            },
            {
              name: "消防水池用水",
              code: "消防水池用水",
              type: "水",
              val1: "",
              val2: "",
              val3: "",
              buildNo: "",
            },
          ],
        },
        {
          name: "各栋分摊",
          isBuildNo: 1,
          filter: ["全部", ...builds],
          items: builds
            .map((e) => [
              {
                buildNo: e,
                code: "消防电梯",
                name: "消防电梯",
                type: "电",
                cutDate: "",
              },
              {
                buildNo: e,
                code: "客梯",
                name: "客梯",
                type: "电",
                cutDate: "",
              },
            ])
            .reduce((arr, sub) => {
              arr.push(...sub);
              return arr;
            }, []),
        },
      ],
    };
  },

  mounted() {
    this.period = this.$route.params.period;

    this.loadCost();
  },
  activated() {},
  components: { CostsNavBar },
  methods: {
    loadCost() {
      this.period = 0;
      this.$route.params.period != "index" &&
        (this.period = this.$route.params.period);

      let items = this.configs[0].items.concat(this.configs[1].items);
      this.$axios.get("/cost/" + this.period).then((r) => {
        items.forEach((e) => {
          Object.assign(e, {
            cutDate: "",
            val1: "",
            val2: "",
            val3: "",
            pval1: "",
          });
        });
        if (r.data.code == 0) {
          let costs = r.data.data;
          items.forEach((e) => {
            let c = costs.filter(
              (d) =>
                e.buildNo == d.buildNo &&
                e.code == d.code &&
                d.period == this.period
            );
            if (c.length) Object.assign(e, c[0]);
          });
        }
        console.log(this.configs);
      });
    },
    save() {
      let items = this.configs[this.configIndex].items;
      this.$axios.post("/cost/" + this.period, items).then((r) => {
        if (r.data.code == 0) {
          this.$router.push("/costs/" + this.period);
        }
      });
    },
    allValSame(val, p) {
      console.log(val);
      let items = this.configs[this.configIndex].items;
      let filter = this.configs[this.configIndex].filter;
      items
        .filter(
          (e) =>
            this.filterIndex == 0 ||
            (!e.buildNo && e.type == filter[this.filterIndex]) ||
            e.buildNo == filter[this.filterIndex]
        )
        .forEach((e) => (e[p] = val));
      console.log(items);
    },
  },
  watch: {
    $route() {
      this.loadCost();
    },
  },
};
</script>
<style scoped>
#costs .item:not(:last-child) {
  border-bottom: 1px dashed #ccc;
  padding-bottom: 1rem;
}
</style>

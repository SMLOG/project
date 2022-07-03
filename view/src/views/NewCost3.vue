<template>
  <div class="page-content">
    <div class="card mb-3">
      <div class="card-body">
        <div class="text-end">
          <router-link to="newCost" class="btn btn-sm btn-primary mb-3"
            >添加</router-link
          >
        </div>
        <div>
          <table
            class="
              table
              table-responsive
              table-bordered
              table-striped
              table-hover
              table-sm
            "
          >
            <thead class="text-center">
              <tr>
                <th rowspan="2" class="align-middle">序号</th>
                <th rowspan="2" class="align-middle">表名</th>
                <th rowspan="2" class="align-middle">归属</th>
                <th rowspan="2" class="align-middle">主体</th>
                <th colspan="2" class="text-center">本期</th>
                <th colspan="2" class="text-center">上期</th>
                <th rowspan="2" class="align-middle">用量</th>
                <th rowspan="2" class="align-middle">损耗</th>
                <th rowspan="2" class="align-middle">损耗单价</th>
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
                <td>栋号:{{ h.buildNo }}</td>
                <td>楼层:{{ h.floorNo }}</td>
                <td>{{ h.roomNo }}</td>
                <td>
                  <span v-if="h.binderIds">{{
                    h.binderIds.split(",").length
                  }}</span>
                  <span v-else>无</span>
                </td>
                <td>
                  <div v-if="h.binderIds">
                    <div v-for="id in h.binderIds.split(',')" :key="id">
                      {{ userMap[id] }}
                    </div>
                  </div>
                </td>

                <td>
                  <span v-if="h.ownerId">是</span>
                  <span v-else>否</span>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
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
export default {
  data() {
    return {
      showBatch: 0,
      costs: [{}],
      selectHouseIds: [],
      userMap: { 0: "" },
    };
  },

  mounted() {},
  components: { HouseInit },
  activated() {},
  methods: {},
  watch: {},
};
</script>
<style scoped>
</style>

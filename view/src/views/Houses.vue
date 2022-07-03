<template>
  <div class="page-content">
    <div class="card mb-3">
      <div class="card-body">
        <a
          class="btn btn-sm btn-primary"
          v-if="houses.length == 0"
          @click="showBatch = !showBatch"
          >模版批量生成房屋信息</a
        >
        <div>
          <table
            class="table table-responsive table-striped table-hover table-sm"
          >
            <thead>
              <th>栋号</th>
              <th>楼层号</th>
              <th>房号</th>
              <th>绑定人数</th>
              <th>尾号</th>
              <th>认证</th>
            </thead>
            <tbody>
              <tr v-for="(h, i) in houses" :key="i">
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
              </tr>
            </tbody>
          </table>
          <div>
            共有{{ houses.length }}户,已有{{
              houses.filter((e) => e.binderIds).length
            }}绑定用户.
          </div>
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
    return { showBatch: 0, houses: [], selectHouseIds: [], userMap: { 0: "" } };
  },

  mounted() {
    this.loadHouseList();
  },
  components: { HouseInit },
  activated() {},
  methods: {
    loadHouseList() {
      this.$axios.post("/userMap").then((r) => {
        if (!r.data.code) Object.assign(this.userMap, r.data.data);
      });
      this.$axios.get("/houses").then((r) => {
        this.houses.length = 0;
        this.houses.push(...r.data.data);
      });
    },
  },
  watch: {},
};
</script>
<style scoped>
</style>

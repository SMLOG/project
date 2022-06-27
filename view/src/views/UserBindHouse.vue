<template>
  <div>
    <button
      type="button"
      class="btn btn-primary btn-sm"
      data-bs-toggle="modal"
      data-bs-target="#houseModal"
      @click="loadHouseList()"
    >
      添加房产
    </button>
    <div
      class="modal fade"
      id="houseModal"
      tabindex="-1"
      aria-labelledby="houseModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="houseModalLabel">选择你的房子</h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <div>
              <div>
                <ul class="list-group">
                  <li
                    v-for="h in houses"
                    class="list-group-item"
                    :key="h.houseId"
                  >
                    <input
                      type="radio"
                      name="selectHouse"
                      :value="h.houseId"
                      v-model="selectHouseId"
                    />
                    栋:{{ h.buildNo }} 楼层:{{ h.floorNo }} 单元号:{{
                      h.roomNo
                    }}
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              data-bs-dismiss="modal"
            >
              关闭</button
            ><button type="button" class="btn btn-primary" @click="addHouse()">
              添加
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>



<script>
export default {
  components: {},
  data() {
    return {
      selectHouseId: null,
      myHouses: [],
      houses: [],
      loginUser: {},
    };
  },
  mounted() {},
  methods: {
    loadHouseList() {
      this.$axios.get("/houses").then((r) => {
        this.houses.length = 0;
        this.houses.push(...r.data.data);
      });
    },

    requestAuth(h) {
      this.$axios.post("/reqAuthMyHouse.json", h).then((r) => {
        if (r.data.code == 0) {
          alert(
            "申请成功,请发送你的申请码给其他用户(微信群或者你认识的验证通过业主)验证审核你的申请。"
          );
          this.loadMyHouseList();
        }
      });
    },
    removeHouse(h) {
      if (confirm("确定删除？"))
        this.$axios.post("/removeMyHouse", h).then((r) => {
          if (r.data.code == 0) {
            this.loadMyHouseList();
          }
        });
    },
    addHouse() {
      this.$axios
        .post("/addMyHouse", { houseId: this.selectHouseId })
        .then((r) => {
          if (r.data.code == 0) {
            $("#houseModal").modal("hide");
            this.loadMyHouseList();
          }
        });
    },
  },
};
</script>

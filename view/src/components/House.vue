<template>
  <div>
    <div>
      <div class="card mt-3">
        <div class="car-dbody" v-for="(info, i) in templates" :key="i">
          <div>
            #{{ i + 1 }} 大楼栋号<input v-model="info.buildNo" /> 层数
            <input v-model="info.floors" /> 每层/单元号数
            <input v-model="info.units" />
            <a
              class="btn btn-warning btn-sm mx-3"
              @click="templates.splice(i, 1)"
              >删除</a
            >
          </div>
          <div
            v-if="info.units"
            class="d-flex table-responsive mb-0 fixed-solution"
          >
            <table class="table">
              <thead>
                <tr>
                  <th>单元号</th>
                  <th v-for="i in parseInt(info.units)" :key="'h' + i">
                    {{ i }}
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>面积</td>
                  <td v-for="i in parseInt(info.units)" :key="i">
                    <input
                      @change="(event) => (info[i] = event.target.value)"
                    />
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div></div>
      </div>

      <div>
        <a class="btn btn-primary" @click="templates.push({})">添加行模版</a>
        <a class="btn btn-primary mx-3" @click="buildResult()">预览</a>
        <a class="btn btn-primary mx-3" @click="save()">保存</a>
      </div>
    </div>
    <div id="next" v-if="result.length > 0">
      <div class="card mt-3">
        <div class="card-body">
          <table class="table table-sm mb-0">
            <thead>
              <tr>
                <th scope="col">栋号/单号</th>
                <th scope="col">楼层</th>
                <th scope="col" v-for="c in maxCells" :key="c">{{ c }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="floor in result" :key="floor.id">
                <th scope="row">{{ floor.buildNo }}</th>
                <td>{{ floor.floorNo }}</td>
                <td v-for="cell in floor.cells" :key="cell.no">
                  {{ cell.m2 }}
                </td>
                <td v-for="c in maxCells - floor.cells.length" :key="c"></td>
              </tr>
            </tbody>
          </table>
          <div>
            总单元数:{{
              result.reduce((total, row) => {
                return row.cells.length + total;
              }, 0)
            }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      templates: [{ buildNo: "1,2", floors: 7, units: 4 }],
      result: [],
      form: {
        email: "",
        name: "",
        food: null,
        checked: [],
      },
      foods: [
        { text: "Select One", value: null },
        "Carrots",
        "Beans",
        "Tomatoes",
        "Corn",
      ],
      show: true,
    };
  },
  methods: {
    buildResult() {
      let d = this.templates;
      let result = this.result;

      this.result.length = 0;
      let map = {};
      for (let i = 0; i < d.length; i++) {
        let buildNoList = d[i].buildNo.split(",");
        buildNoList.forEach((buildNo) => {
          let floorNos;
          if (typeof d[i].floors == "string" && d[i].floors.indexOf(",") > -1) {
            floorNos = d[i].floors.split(",");
          } else {
            floorNos = Array.from({ length: parseInt(d[i].floors) }).map(
              (ii, e) => e + 1
            );
          }
          floorNos.forEach((floor) => {
            let row = {
              buildNo: buildNo,
              floorNo: floor,
              cells: Array.from({ length: d[i].units }).map((ii, e) => {
                return { no: buildNo + floor + e, m2: d[i][e + 1] };
              }),
            };

            if (map[buildNo + "_" + floor] != undefined) {
              result[map[buildNo + "_" + floor]] = row;
            } else {
              map[buildNo + "_" + floor] = result.length - 1;

              result.push(row);
            }
          });
        });
      }
      this.result.sort((a, b) => {
        let k = a.buildNo.localeCompare(b.buildNo);
        return k == 0 ? ("" + a.floorNo).localeCompare("" + b.floorNo) : k;
      });
      this.maxCells = Math.max(...this.result.map((e) => e.cells.length));
      console.log(this.result);
    },
    save() {
      if (this.result.length == 0) return;
      let rooms = [];
      for (let i = 0; i < this.result.length; i++) {
        let floor = this.result[i];
        rooms.push(
          ...floor.cells.map((e) => {
            return {
              buildNo: floor.buildNo,
              floorNo: floor.floorNo,
              roomNo: e.no,
              m2: e.m2,
            };
          })
        );
      }
      console.log(rooms);
      this.$axios.post("/initHouses", rooms).then((r) => {
        alert("saved");
      });
    },
  },
};
</script>

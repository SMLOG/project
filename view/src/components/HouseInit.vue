<template>
  <div>
    <div class="card">
      <div class="card-body" v-for="(info, i) in templates" :key="i">
        <div class="">
          <div>
            <div class="d-flex justify-content-between">
              <div>#{{ i + 1 }}</div>
              <a class="btn-close" @click="templates.splice(i, 1)"></a>
            </div>
            <div class="row">
              <div class="col">
                <div class="row">
                  <div class="col">
                    <label class="col-form-label">栋号</label
                    ><input class="form-control" v-model="info.buildNo" />
                  </div>
                </div>
              </div>
              <div class="col">
                <label class="col-form-label">层数</label>
                <input class="form-control" v-model="info.floors" />
              </div>
              <div class="col">
                <label class="col-form-label"> 单元数</label>
                <input class="form-control" v-model="info.units" />
              </div>
            </div>
          </div>
          <div v-if="info.units" class="table-responsive">
            <table class="table">
              <thead>
                <tr>
                  <th><div class="text-nowrap">单元号</div></th>
                  <th v-for="i in parseInt(info.units)" :key="'h' + i">
                    {{ i }}
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td><div>面积</div></td>
                  <td v-for="i in parseInt(info.units)" :key="i">
                    <input
                      class="form-control"
                      @change="(event) => (info[i] = event.target.value)"
                    />
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div class="card-footer text-end">
        <a class="btn btn-primary" @click="templates.push({})">添加行</a>
        <a class="btn btn-primary mx-3" @click="buildResult()">生成预览</a>
        <a class="btn btn-primary mx-3" @click="save()">保存</a>
      </div>
    </div>

    <div id="next" v-if="result.length > 0">
      <div class="card mt-3">
        <div class="card-body">
          <table class="table table-sm mb-0 table-responsive">
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
      templates: [
        { buildNo: "1,2,3,4,5,6", floors: 17, units: 4 },
        {
          buildNo: "7,8",
          floors: Array.from({ length: parseInt(16) })
            .map((ii, e) => e + 2)
            .join(","),
          units: 4,
        },
      ],
      result: [],
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
            floorNos = d[i].floors.split(",").filter((e) => e.trim());
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
                return {
                  no: buildNo + "-" + floor + (e < 9 ? "0" + (e + 1) : e + 1),
                  m2: d[i][e + 1],
                };
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
        let k = a.buildNo - b.buildNo;
        return k == 0 ? a.floorNo - b.floorNo : k;
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
      this.$axios.post("/initHouses", rooms).then(() => {
        alert("saved");
      });
    },
  },
};
</script>

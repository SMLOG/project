<template>
  <div
    id="candidateModal"
    tabindex="-1"
    class="modal fade"
    style="display: none"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">报名候选人资料填写</h5>
          <button
            type="button"
            data-bs-dismiss="modal"
            aria-label="Close"
            class="btn-close"
          ></button>
        </div>
        <div class="modal-body">
          <div class="mb-3">
            <label>你的名称</label>
            <input class="form-control" v-model="can.nickName" />
          </div>
          <div class="mb-3">
            <label>简介</label>
            <textarea class="form-control" v-model="can.descr" />
          </div>
          <div class="mb-3">
            <label>照片</label>
            <input
              ref="previewImgFile"
              type="file"
              name="file"
              accept=".jpg, .png, image/jpeg, image/png"
              @change="previewImg($event)"
            />
            <img
              v-show="previewImgUrl"
              ref="previewImg"
              style="width: 100%; height: 200px"
            />
          </div>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            data-bs-dismiss="modal"
            class="btn btn-secondary"
          >
            关闭取消</button
          ><button
            type="button"
            class="btn btn-primary"
            @click="regCandidate(can)"
          >
            提交
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      can: { nickName: "", descr: "", tel: "" },
      previewImgUrl: "",
    };
  },
  mounted() {},
  methods: {
    previewImg(event) {
      var file = event.target.files[0];

      if (!file) return;
      console.log(file);
      console.log("file.size = " + file.size);

      var reader = new FileReader();

      let previewImg = this.$refs.previewImg;
      let self = this;
      reader.onload = function (e) {
        console.log("成功读取....");

        previewImg.src = e.target.result;
        self.previewImgUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    },
    regCandidate(info) {
      let id = this.$route.params.activityId;

      let file = this.$refs.previewImgFile.files[0];
      let param = new FormData(); // 创建form对象
      param.append("file", file); // 通过append向form对象添加数据
      param.append("json", JSON.stringify(info)); // 添加form表单中其他数据
      let config = {
        headers: { "Content-Type": "multipart/form-data" },
      };
      this.$axios
        .post("/activityCandidate/" + id, param, config)
        .then((r) => {
          if (r.data.code == 0) {
            this.$emit("ok");
            alert("报名成功！");
            $("#candidateModal").modal("hide");
          } else alert(r.data.msg);
        })
        .catch((err) => {
          alert(err);
        });
    },
  },
};
</script>

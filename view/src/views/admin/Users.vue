<template>
  <div class="page-content">
    <!--breadcrumb-->
    <div class="page-breadcrumb d-none d-sm-flex align-items-center mb-3">
      <div class="breadcrumb-title pe-3">Admin</div>
      <div class="ps-3">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb mb-0 p-0">
            <li class="breadcrumb-item">
              <a href="javascript:;"><i class="bx bx-home-alt"></i></a>
            </li>
            <li class="breadcrumb-item active" aria-current="page">Users</li>
          </ol>
        </nav>
      </div>
    </div>
    <!--end breadcrumb-->
    <h6 class="mb-0 text-uppercase">User List</h6>
    <hr />
    <div class="card">
      <div class="card-body">
        <div class="table-responsive">
          <table class="table mb-0">
            <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">User Id</th>
                <th scope="col">User Name</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, i) in items" :key="item.id">
                <th scope="row">{{ i + 1 }}</th>
                <td>{{ item.id }}</td>
                <td>{{ item.userName }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    return { items: [] };
  },
  mounted() {
    this.loadUsers();
  },
  methods: {
    loadUsers() {
      request.get("/system/admin/users").then((r) => {
        let data = r.data;
        this.items.length = 0;
        console.log(r);
        console.log(data);
        if (data.code == 0) this.items.push(...data.data);
      });
    },
  },
};
</script>
<style scoped>
</style>

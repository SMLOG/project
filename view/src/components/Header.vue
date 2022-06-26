<template>
  <!--start header -->
  <header>
    <div class="topbar d-flex align-items-center">
      <nav class="navbar navbar-expand">
        <div>
          <ol class="breadcrumb">
            <li class="breadcrumb-item">
              <router-link :to="'/index/1'">
                <i class="bx bx-home">首页</i></router-link
              >
            </li>
            <li
              v-if="navName"
              class="breadcrumb-item active"
              aria-current="page"
            >
              {{ navName }}
            </li>
          </ol>
        </div>
        <div class="ms-auto">
          <ul class="navbar-nav">
            <li class="nav-item dropdown" v-if="navName == ''">
              <button
                class="btn btn-primary btn-sm dropdown-toggle"
                type="button"
                id="dropdownMenuButton1"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                发起
              </button>

              <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                <li>
                  <a
                    @click="$router.push({ path: '/newVote' })"
                    class="dropdown-item"
                  >
                    <i class="bx bx-group" style="margin-right: 10px"></i>投票
                  </a>
                </li>
              </ul>
            </li>
          </ul>
        </div>
        <div class="user-box">
          <div class="dropdown">
            <a
              class="d-flex align-items-center nav-link dropdown-toggle"
              href="#"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              <div class="user-info">
                <p class="user-name mb-0">
                  <font-awesome-icon icon="fa-solid fa-user" size="xs" />
                  {{ userName }}
                </p>
              </div>
            </a>
            <ul class="dropdown-menu dropdown-menu-end">
              <li>
                <a
                  class="dropdown-item"
                  @click="
                    $router.push({
                      path: '/profile',
                    })
                  "
                  ><i class="bx bx-user"></i><span>我的资料</span></a
                >
              </li>
              <li>
                <a
                  class="dropdown-item"
                  @click="
                    $router.push({
                      path: '/profile',
                    })
                  "
                  ><i class="bx bx-user"></i><span>申请成为管理员</span></a
                >
              </li>
              <li>
                <div class="dropdown-divider mb-0"></div>
              </li>
              <li>
                <a class="dropdown-item" @click="logout()"
                  ><i class="bx bx-log-out-circle"></i><span>退出</span></a
                >
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </div>
  </header>
  <!--end header -->
</template>
<script>
import { mapState } from "vuex";
export default {
  data() {
    return {
      userName: "no login",
    };
  },
  mounted() {
    this.$axios
      .get("/loginUser")
      .then((r) => {
        if (r.data.code == 20001) this.$router.push("/login");
        else if (r.data.code == 0) {
          this.userName = r.data.data.userName;
          this.$store.commit("userId", r.data.data.userId);
        }
      })
      .catch(() => {
        this.$router.push("/login");
      });
  },
  methods: {
    logout() {
      this.$axios.get("/public/logout").then(() => {
        this.$router.push({ path: "/login" });
      });
    },
  },
  computed: {
    ...mapState({
      loginUser: (state) => state.user.loginUser,
      navName: (state) => state.user.navName,
    }),
  },
};
</script>


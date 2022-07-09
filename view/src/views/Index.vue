<template>
  <div class="page-content">
    <UserHouseAuth v-if="loginUser.status > 1" />
    <div class="card">
      <div class="card-body">
        <div
          class="h5 card-title d-flex justify-content-between align-items-center"
        >
          <span>最近在进行</span>

          <div class="ms-auto">
            <ul class="navbar-nav">
              <li class="nav-item dropdown">
                <span
                  class="btn"
                  id="dropdownMenuButton1"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  <font-awesome-icon icon="fa-solid fa-circle-plus" />发布
                </span>

                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                  <li>
                    <a
                      @click="$router.push({ path: '/newVote' })"
                      class="dropdown-item"
                    >
                      <i class="bx bx-group" style="margin-right: 10px"></i
                      >选项投票
                    </a>
                  </li>

                  <li>
                    <a
                      @click="$router.push({ path: '/NewActivity' })"
                      class="dropdown-item"
                    >
                      <i class="bx bx-group" style="margin-right: 10px"></i
                      >候选投票
                    </a>
                  </li>
                </ul>
              </li>
            </ul>
          </div>
        </div>
        <div v-for="(post, i) in posts" :key="i">
          <div class="d-flex justify-content-between align-items-center">
            <div class="d-flex" style="min-width: 0">
              <span class="flex-shrink-0">[{{ map[post.name] }}]</span>
              <router-link
                class="text-truncate ms-2"
                :to="'/' + post.name + '/' + post.refId"
              >
                {{ post.subject }}</router-link
              >
            </div>
            <span class="text-muted flex-shrink-0 ms-3">
              {{ post.createDate | fmtDate }}
            </span>
          </div>
          <p class="border-bottom mb-3 pb-3 text-truncate">
            {{ post.descr }}
          </p>
        </div>
      </div>
      <section class="section pt-0">
        <nav aria-label="Page navigation">
          <ul class="pagination justify-content-center">
            <li class="page-item" :class="{ disabled: !hasPrev }">
              <a
                class="page-link"
                @click="gotoPage(page - 1)"
                aria-label="Previous"
              >
                <span aria-hidden="true">&laquo;</span>
              </a>
            </li>
            <li
              class="page-item"
              v-for="p in Math.min(startPage + 3, totalPages)"
              :key="p"
              :class="{ active: p == page }"
            >
              <a class="page-link" @click="gotoPage(p)">{{ p }}</a>
            </li>
            <li class="page-item" :class="{ disabled: !hasNext }">
              <a
                class="page-link"
                @click="gotoPage(page + 1)"
                aria-label="Next"
              >
                <span aria-hidden="true">&raquo;</span>
              </a>
            </li>
          </ul>
        </nav>
      </section>
    </div>
    <DashBoard />
  </div>
</template>

<script>
import UserHouseAuth from "@/components/UserHouseAuth";
import DashBoard from "@/components/DashBoard";
import { mapState } from "vuex";
export default {
  data() {
    return {
      map: { vote: "选项投票", activity: "报名投票" },
      posts: [],
      page: 1,
      totalPages: 0,
      startPage: 0,
      hasNext: 0,
      hasPrev: 0,
    };
  },
  components: { UserHouseAuth, DashBoard },
  filters: {
    fmtDate(dateTime) {
      const d = new Date(dateTime);
      const now = Date.now();

      const diff = (now - d) / 1000;

      if (diff < 30) {
        return "刚刚";
      } else if (diff < 3600) {
        // less 1 hour
        return Math.ceil(diff / 60) + "分钟前";
      } else if (diff < 3600 * 24) {
        return Math.ceil(diff / 3600) + "小时前";
      } else if (diff < 3600 * 24 * 2) {
        return "1天前";
      }
      return d.getMonth() + 1 + "-" + d.getDate();
    },
  },
  mounted() {
    this.gotoPage(1);
  },
  activated() {},
  computed: {
    ...mapState({
      loginUser: (state) => state.user.loginUser,
    }),
  },
  methods: {
    gotoPage(p) {
      this.loadPostList(p - 1);
      this.$router.push({ params: { pageId: p } });
    },
    loadPostList(p) {
      this.$axios.get("/posts/?page=" + p).then((r) => {
        if (r.data.code == 0) {
          this.posts.length = 0;
          this.posts.push(...r.data.data.content);
          this.totalPages = r.data.data.totalPages;
          this.page = r.data.data.number + 1;
          this.startPage = r.data.data.number;
          this.hasNext = 1;
          this.hasPrev = 1;
          if (this.startPage > this.totalPages - 3) {
            this.startPage = this.totalPages - 3;
            this.hasNext = 0;
          }

          if (this.startPage <= 0) {
            this.startPage = this.totalPages - 3;
            this.hasPrev = 0;
          }
        }
      });
    },
  },
  watch: {
    $route() {
      this.gotoPage(this.$route.params.pageId || 1);
    },
  },
};
</script>

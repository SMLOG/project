<template>
  <div class="page-content">
    <UserHouseAuth />
    <div class="card">
      <div class="card-body">
        <div class="h5 card-title d-flex justify-content-between">
          <span>最近发生</span>

          <div class="ms-auto">
            <ul class="navbar-nav">
              <li class="nav-item dropdown">
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
                  <li>
                    <a
                      @click="$router.push({ path: '/newVote' })"
                      class="dropdown-item"
                    >
                      <i class="bx bx-group" style="margin-right: 10px"></i>报名
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
              <span class="flex-shrink-0">[投票]</span>
              <router-link
                class="h5 text-truncate"
                :to="{ name: 'voteDetail', params: { voteId: post.voteId } }"
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

export default {
  data() {
    return {
      posts: [],
      page: 1,
      totalPages: 0,
      startPage: 0,
      hasNext: 0,
      hasPrev: 0,
      tabs: [
        { name: "投票讨论" },
        { name: "房产信息" },
        { name: "业主大会会议筹备组" },
        { name: "选举业主委员会" },
        { name: "首次成立业主大会" },
      ],
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
  methods: {
    gotoPage(p) {
      this.loadSpace(p - 1);
      this.$router.push({ params: { pageId: p } });
    },
    loadSpace(p) {
      this.$axios.get("/votes/?page=" + p).then((r) => {
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

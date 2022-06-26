<template>
  <div class="page-content">
    <UserHouseAuth />
    <div class="card">
      <div class="card-body">
        <div class="h5 card-title">最近发生</div>
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
              {{ post.createdDate | fmtDate }}
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
    <div class="card my-3">
      <div class="card-body">
        <ul class="nav nav-tabs nav-primary" role="tablist">
          <li
            v-for="(tab, i) in tabs"
            class="nav-item"
            role="presentation"
            :key="i"
          >
            <a
              class="nav-link"
              data-bs-toggle="tab"
              href="#primaryhome"
              role="tab"
              aria-selected="true"
              ><div class="d-flex align-items-center">
                <div class="tab-icon">
                  <i class="bx bx-home font-18 me-1"></i>
                </div>
                <div class="tab-title">{{ tab.name }}</div>
              </div></a
            >
          </li>
        </ul>
        <div class="tab-content py-3">
          <div
            class="tab-pane fade active show"
            id="primaryhome"
            role="tabpanel"
          >
            <p>
              Raw denim you probably haven't heard of them jean shorts Austin.
              Nesciunt tofu stumptown aliqua, retro synth master cleanse.
              Mustache cliche tempor, williamsburg carles vegan helvetica.
              Reprehenderit butcher retro keffiyeh dreamcatcher synth. Cosby
              sweater eu banh mi, qui irure terry richardson ex squid. Aliquip
              placeat salvia cillum iphone. Seitan aliquip quis cardigan
              american apparel, butcher voluptate nisi.
            </p>
          </div>
          <div class="tab-pane fade" id="primaryprofile" role="tabpanel">
            <p>
              Food truck fixie locavore, accusamus mcsweeney's marfa nulla
              single-origin coffee squid. Exercitation+1 labore velit,blog
              sartorial PBR leggings next level wes anderson artisan four loko
              farm-to-table craft beer twee. Qui photo booth letterpress,commodo
              enim craft beer mlkshk aliquip jean shorts ullamco ad vinyl cillum
              PBR. Homo nostrud organic,assumenda labore aesthetic magna
              delectus mollit. Keytar helvetica VHS salvia yr,vero magna velit
              sapiente labore stumptown. Vegan fanny pack odio cillum wes
              anderson 8-bit,sustainable jean shorts beard ut DIY ethical culpa
              terry richardson biodiesel. Art party scenester stumptown,tumblr
              butcher vero sint qui sapiente accusamus tattooed echo park.
            </p>
          </div>
          <div class="tab-pane fade" id="primarycontact" role="tabpanel">
            <p>
              Etsy mixtape wayfarers,ethical wes anderson tofu before they sold
              out mcsweeney's organic lomo retro fanny pack lo-fi farm-to-table
              readymade. Messenger bag gentrify pitchfork tattooed craft beer,
              iphone skateboard locavore carles etsy salvia banksy hoodie
              helvetica. DIY synth PBR banksy irony. Leggings gentrify squid
              8-bit cred pitchfork. Williamsburg banh mi whatever gluten-free,
              carles pitchfork biodiesel fixie etsy retro mlkshk vice blog.
              Scenester cred you probably haven't heard of them,vinyl craft beer
              blog stumptown. Pitchfork sustainable tofu synth chambray yr.
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserHouseAuth from "@/components/UserHouseAuth";

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
        { name: "电子投票" },
      ],
    };
  },
  components: { UserHouseAuth },
  filters: {
    fmtDate(date) {
      return date.substr(5);
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

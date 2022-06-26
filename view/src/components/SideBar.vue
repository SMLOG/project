<template>
  <!--sidebar wrapper -->
  <div class="sidebar-wrapper" data-simplebar="true">
    <div class="sidebar-header">
      <div>
        <img
          src="static/picture/logo-icon.png"
          class="logo-icon"
          alt="logo icon"
        />
      </div>
      <div>
        <h4 class="logo-text">Project</h4>
      </div>
      <div class="toggle-icon ms-auto">
        <i class="bx bx-arrow-to-left"></i>
      </div>
    </div>
    <!--navigation-->
    <ul class="metismenu" id="menu">
      <li v-for="menu in menus" :key="menu.name">
        <a @click="goto(menu)" :class="{ 'has-arrow': menu.subMenus }">
          <div class="parent-icon">
            <i class="bx" :class="menu.icon"></i>
          </div>
          <div class="menu-title">{{ menu.name }}</div>
        </a>
        <ul v-if="menu.subMenus" class="mm-show">
          <li v-for="sub in menu.subMenus" :key="sub.name">
            <a @click="goto(sub)"
              ><i class="bx bx-right-arrow-alt" :class="sub.icon"></i
              >{{ sub.name }}</a
            >

            <ul v-if="sub.subMenus" class="mm-show">
              <li v-for="sub2 in sub.subMenus" :key="sub2.name">
                <a @click="goto(sub2)"
                  ><i class="bx bx-right-arrow-alt" :class="sub2.icon"></i
                  >{{ sub2.name }}</a
                >

                <ul v-if="sub2.subMenus">
                  <li v-for="sub3 in sub2.subMenus" :key="sub3.name">
                    <a @click="goto(sub3)"
                      ><i class="bx bx-right-arrow-alt" :class="sub3.icon"></i
                      >{{ sub3.name }}</a
                    >
                  </li>
                </ul>
              </li>
            </ul>
          </li>
        </ul>
      </li>
      <li class="menu-label">Others</li>
      <li>
        <a href="javascript:;" target="_blank">
          <div class="parent-icon">
            <i class="bx bx-folder"></i>
          </div>
          <div class="menu-title">Documentation</div>
        </a>
      </li>
      <li>
        <a href="javascript:;" target="_blank">
          <div class="parent-icon">
            <i class="bx bx-support"></i>
          </div>
          <div class="menu-title">Support</div>
        </a>
      </li>
    </ul>
    <!--end navigation-->
  </div>
  <!--end sidebar wrapper -->
</template>
<script>
export default {
  data() {
    return {
      menus: [
        { name: "Dashboard", path: "/", icon: "bx-home-circle" },
        {
          name: "Admin",
          icon: "bx-category",
          subMenus: [{ name: "Users", path: "/admin/users" }],
        },
        {
          name: "Workspace",
          path: "/workspace",
          icon: "bx-home-circle",
          subMenus: [
            {
              name: "Workspace1",
              path: "/space/1/summary",
              subMenus: [
                { name: "Vote", path: "/space/1/vote" },
                { name: "People", path: "/space/1/people" },
                {
                  name: "Form",
                  subMenus: [
                    { name: "FeedBack", path: "/login" },
                    { name: "房产信息", path: "/login" },
                    { name: "论坛", path: "/login" },
                  ],
                },
                { name: "Report", path: "/space/1/reports" },
                { name: "Files", path: "/space/1/files" },
              ],
            },
            { name: "Workspace2", path: "/space/2/summary" },
          ],
        },

        {
          name: "Application",
          icon: "bx-category",
          subMenus: [{ name: "Email", path: "/login" }],
        },
        {
          name: "Domain",
          icon: "bx-category",
          subMenus: [{ name: "Email", path: "/login" }],
        },
        {
          name: "Resource",
          icon: "bx-category",
          subMenus: [{ name: "Email", path: "/login" }],
        },
      ],
    };
  },
  mounted() {
    $(function () {
      $(".mobile-toggle-menu").on("click", function () {
        $(".wrapper").addClass("toggled");
      }),
        $(".toggle-icon").click(function () {
          $(".wrapper").hasClass("toggled")
            ? ($(".wrapper").removeClass("toggled"),
              $(".sidebar-wrapper").unbind("hover"))
            : ($(".wrapper").addClass("toggled"),
              $(".sidebar-wrapper").hover(
                function () {
                  $(".wrapper").addClass("sidebar-hovered");
                },
                function () {
                  $(".wrapper").removeClass("sidebar-hovered");
                }
              ));
        }),
        $("#menu").metisMenu();
    });
  },
  methods: {
    goto(menu) {
      if (menu.path)
        this.$router.push({
          path: menu.path,
        });
    },
    onReset(evt) {
      evt.preventDefault();
      // Reset our form values
      this.form.email = "";
      this.form.name = "";
      this.form.food = null;
      this.form.checked = [];
      // Trick to reset/clear native browser form validation state
      this.show = false;
      this.$nextTick(() => {
        this.show = true;
      });
    },
  },
};
</script>

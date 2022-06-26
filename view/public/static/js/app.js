$(function () {
  "use strict";
  // new PerfectScrollbar(".header-message-list"),
  //   new PerfectScrollbar(".header-notifications-list"),
  $(".mobile-search-icon").on("click", function () {
    $(".search-bar").addClass("full-search-bar");
  }),
    $(".search-close").on("click", function () {
      $(".search-bar").removeClass("full-search-bar");
    }),
    $(document).ready(function () {
      $(window).on("scroll", function () {
        $(this).scrollTop() > 300
          ? $(".back-to-top").fadeIn()
          : $(".back-to-top").fadeOut();
      }),
        $(".back-to-top").on("click", function () {
          return (
            $("html, body").animate(
              {
                scrollTop: 0,
              },
              600
            ),
            !1
          );
        });
    }),
    $(function () {
      for (
        var e = window.location,
          o = $(".metismenu li a")
            .filter(function () {
              return this.href == e;
            })
            .addClass("")
            .parent()
            .addClass("mm-active");
        o.is("li");

      )
        o = o.parent("").addClass("mm-show").parent("").addClass("mm-active");
    });
});

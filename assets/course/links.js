/* ===========================================================================
   Keeps links to Markdown docs working wherever a course page is opened.

   Course HTML pages link docs by their source name (GUIDE.md, ../GLOSSARY.md,
   assets/lab/README.md) so they work when browsing the repo locally (file://).
   On GitHub Pages every *.md is published only as rendered HTML, so on http(s)
   this rewrites relative "x.md" links to "x.html" and "README.md" to the folder
   "index.html" (jekyll-readme-index).

   An <a href="x.html" data-md="x.md"> link points at the rendered page and is
   switched to its Markdown source on file://, where no renderer exists.
   =========================================================================== */
(function () {
  "use strict";

  var ABSOLUTE = /^(?:[a-z][a-z0-9+.-]*:|\/\/|#)/i;

  function rewrite() {
    var local = location.protocol === "file:";
    document.querySelectorAll("a[href]").forEach(function (a) {
      if (local) {
        var md = a.getAttribute("data-md");
        if (md) a.setAttribute("href", md);
        return;
      }
      var href = a.getAttribute("href");
      if (ABSOLUTE.test(href)) return;
      var m = href.match(/^([^?#]*)\.md([?#].*)?$/i);
      if (!m) return;
      var path = /(^|\/)README$/i.test(m[1]) ? m[1].replace(/README$/i, "index") : m[1];
      a.setAttribute("href", path + ".html" + (m[2] || ""));
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", rewrite);
  } else {
    rewrite();
  }
})();

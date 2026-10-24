/* ===========================================================================
   Reusable step-through animation for lessons.

   Markup: a `.stepflow` container holding an <svg> scene, a `.sf-caption`
   (with `.sf-title` + `.sf-desc`), `.sf-controls` buttons
   (`.sf-prev`/`.sf-next`/`.sf-reset`/`.sf-play`, `.sf-counter`), and a
   <script class="sf-steps" type="application/json"> array of steps.

   Each step: {
     title, desc(html), token:{x,y}|null, tokenLabel,
     visible:[svg element ids to show], highlight:[svg element ids to mark active]
   }
   Elements named anywhere in `visible` are display-toggled each step; elements
   named in `highlight` get the `.sf-active` class. The `#token` group (id set
   via data-token) is moved with a CSS transform transition.
   =========================================================================== */
(function () {
  "use strict";

  function initOne(root) {
    var stepsEl = root.querySelector(".sf-steps");
    var svg = root.querySelector("svg");
    if (!stepsEl || !svg) return;

    var steps;
    try { steps = JSON.parse(stepsEl.textContent); }
    catch (e) { console.error("stepflow: invalid steps JSON", e); return; }
    if (!steps.length) return;

    var tokenId = root.getAttribute("data-token") || "token";
    var token = svg.querySelector("#" + tokenId);
    var tokenLabel = token && token.querySelector(".sf-token-label");
    var titleEl = root.querySelector(".sf-title");
    var descEl = root.querySelector(".sf-desc");
    var counterEl = root.querySelector(".sf-counter");
    var btnPrev = root.querySelector(".sf-prev");
    var btnNext = root.querySelector(".sf-next");
    var btnReset = root.querySelector(".sf-reset");
    var btnPlay = root.querySelector(".sf-play");
    var captionEl = root.querySelector(".sf-caption");
    if (captionEl) {
      captionEl.setAttribute("aria-live", "polite");
      captionEl.setAttribute("aria-atomic", "true");
    }

    var toggleSet = {}, actorSet = {};
    steps.forEach(function (s) {
      (s.visible || []).forEach(function (id) { toggleSet[id] = 1; });
      (s.highlight || []).forEach(function (id) { actorSet[id] = 1; });
    });
    var toggleIds = Object.keys(toggleSet);
    var actorIds = Object.keys(actorSet);

    var i = 0, timer = null;
    function el(id) { return svg.querySelector("#" + id); }

    function render(n) {
      i = Math.max(0, Math.min(steps.length - 1, n));
      var s = steps[i];

      toggleIds.forEach(function (id) {
        var e = el(id);
        if (e) e.style.display = (s.visible && s.visible.indexOf(id) >= 0) ? "" : "none";
      });
      actorIds.forEach(function (id) {
        var e = el(id);
        if (e) e.classList.toggle("sf-active", !!(s.highlight && s.highlight.indexOf(id) >= 0));
      });
      if (token) {
        if (s.token) {
          token.style.display = "";
          token.style.transform = "translate(" + s.token.x + "px," + s.token.y + "px)";
          if (tokenLabel && s.tokenLabel != null) tokenLabel.textContent = s.tokenLabel;
        } else {
          token.style.display = "none";
        }
      }
      if (titleEl) titleEl.textContent = (i + 1) + ". " + (s.title || "");
      if (descEl) descEl.innerHTML = s.desc || "";
      if (counterEl) counterEl.textContent = "Step " + (i + 1) + " / " + steps.length;
      if (btnPrev) btnPrev.disabled = (i === 0);
      if (btnNext) btnNext.disabled = (i === steps.length - 1);
    }

    function stopPlay() {
      if (timer) { clearInterval(timer); timer = null; if (btnPlay) { btnPlay.textContent = "▶ Play"; btnPlay.setAttribute("aria-pressed", "false"); } }
    }
    function play() {
      if (timer) { stopPlay(); return; }
      if (i === steps.length - 1) render(0);
      if (btnPlay) btnPlay.textContent = "❚❚ Pause";
      if (btnPlay) btnPlay.setAttribute("aria-pressed", "true");
      timer = setInterval(function () {
        if (i >= steps.length - 1) stopPlay(); else render(i + 1);
      }, 2200);
    }

    if (btnNext) btnNext.addEventListener("click", function () { stopPlay(); render(i + 1); });
    if (btnPrev) btnPrev.addEventListener("click", function () { stopPlay(); render(i - 1); });
    if (btnReset) btnReset.addEventListener("click", function () { stopPlay(); render(0); });
    if (btnPlay) { btnPlay.setAttribute("aria-pressed", "false"); btnPlay.addEventListener("click", play); }

    root.setAttribute("tabindex", "0");
    root.addEventListener("keydown", function (ev) {
      if (ev.key === "ArrowRight") { ev.preventDefault(); stopPlay(); render(i + 1); }
      else if (ev.key === "ArrowLeft") { ev.preventDefault(); stopPlay(); render(i - 1); }
    });

    render(0);
  }

  function init() { document.querySelectorAll(".stepflow").forEach(initOne); }
  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", init);
  else init();
})();

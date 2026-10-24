/* ===========================================================================
   Reusable step-by-step animation engine.
   Turns a static diagram into a "click-through" explainer: elements reveal
   cumulatively, one caption per step, with Back / Play / Next / Replay + dots.

   Markup convention (the engine injects the controls itself):

   <figure class="anim" data-anim>
     <div class="anim-stage">
       <svg ...>
         <g class="anim-el" data-step="1">always-on base</g>
         <g class="anim-el" data-step="2">appears at step 2 and stays</g>
         <text class="anim-el" data-only="3">shown ONLY at step 3</text>
         <g class="anim-el" data-from="2" data-to="4">shown steps 2..4</g>
       </svg>
     </div>
     <div class="anim-caps">
       <p class="anim-cap" data-step="1"><span class="n">1</span>Explanation…</p>
       <p class="anim-cap" data-step="2"><span class="n">2</span>…</p>
     </div>
     <figcaption>Optional static caption.</figcaption>
   </figure>

   Reveal rules per .anim-el:
     data-only="k"            → visible only when current === k
     data-from / data-to      → visible when from <= current <= to
     data-step="k" (default)  → visible when current >= k  (cumulative)
   The element is "active" (highlighted) on the step it first appears.
   =========================================================================== */
(function () {
  "use strict";

  function num(el, attr) {
    var v = el.getAttribute(attr);
    if (v === null) return null;
    var n = parseInt(v, 10);
    return isNaN(n) ? null : n;
  }

  function bounds(el) {
    var only = num(el, "data-only");
    if (only !== null) return { from: only, to: only };
    var from = num(el, "data-from");
    if (from === null) from = num(el, "data-step");
    if (from === null) from = 1;
    var to = num(el, "data-to");
    if (to === null) to = Infinity;
    return { from: from, to: to };
  }

  function setup(root) {
    var els = [].slice.call(root.querySelectorAll(".anim-el"));
    var caps = [].slice.call(root.querySelectorAll(".anim-cap"));
    var captions = root.querySelector(".anim-caps");
    if (captions) {
      captions.setAttribute("aria-live", "polite");
      captions.setAttribute("aria-atomic", "true");
    }

    var maxStep = 1;
    els.forEach(function (el) {
      var b = bounds(el);
      if (b.from > maxStep) maxStep = b.from;
      if (b.to !== Infinity && b.to > maxStep) maxStep = b.to;
    });
    caps.forEach(function (c) {
      var s = num(c, "data-step") || 1;
      if (s > maxStep) maxStep = s;
    });

    var current = 1;
    var timer = null;

    var controls = document.createElement("div");
    controls.className = "anim-controls";
    controls.innerHTML =
      '<button type="button" class="anim-btn" data-prev aria-label="Previous step">&lsaquo; Back</button>' +
      '<button type="button" class="anim-btn anim-btn-play" data-play aria-label="Play">&#9654;&nbsp;Play</button>' +
      '<button type="button" class="anim-btn" data-next aria-label="Next step">Next&nbsp;&rsaquo;</button>' +
      '<button type="button" class="anim-btn" data-replay aria-label="Replay">&#8635;</button>' +
      '<span class="anim-dots" data-dots></span>' +
      '<span class="anim-count" data-count></span>';
    root.appendChild(controls);

    var btnPrev = controls.querySelector("[data-prev]");
    var btnNext = controls.querySelector("[data-next]");
    var btnPlay = controls.querySelector("[data-play]");
    var btnReplay = controls.querySelector("[data-replay]");
    var dotsWrap = controls.querySelector("[data-dots]");
    var count = controls.querySelector("[data-count]");

    var dots = [];
    for (var i = 1; i <= maxStep; i++) {
      var d = document.createElement("button");
      d.type = "button";
      d.className = "anim-dot";
      d.setAttribute("data-goto", i);
      d.setAttribute("aria-label", "Go to step " + i);
      dotsWrap.appendChild(d);
      dots.push(d);
    }

    function render() {
      els.forEach(function (el) {
        var b = bounds(el);
        var shown = current >= b.from && current <= b.to;
        el.classList.toggle("is-shown", shown);
        el.classList.toggle("is-active", shown && current === b.from);
        el.setAttribute("aria-hidden", String(!shown));
      });
      caps.forEach(function (c) {
        c.classList.toggle("is-active", (num(c, "data-step") || 1) === current);
      });
      dots.forEach(function (dot, idx) {
        dot.classList.toggle("is-active", idx + 1 === current);
        dot.classList.toggle("is-past", idx + 1 < current);
        dot.setAttribute("aria-current", idx + 1 === current ? "step" : "false");
      });
      if (count) count.textContent = current + " / " + maxStep;
      if (btnPrev) btnPrev.disabled = current === 1;
      if (btnNext) btnNext.disabled = current === maxStep;
    }

    function go(n) {
      current = Math.max(1, Math.min(maxStep, n));
      render();
    }

    function label(playing) {
      btnPlay.innerHTML = playing ? "&#10073;&#10073;&nbsp;Pause" : "&#9654;&nbsp;Play";
      btnPlay.setAttribute("aria-label", playing ? "Pause" : "Play");
      btnPlay.setAttribute("aria-pressed", String(playing));
      btnPlay.classList.toggle("is-playing", playing);
    }

    function stop() {
      if (timer) { clearInterval(timer); timer = null; }
      label(false);
    }

    function play() {
      if (timer) { stop(); return; }
      if (current >= maxStep) go(1);
      label(true);
      timer = setInterval(function () {
        if (current >= maxStep) { stop(); return; }
        go(current + 1);
      }, 2200);
    }

    btnPrev.addEventListener("click", function () { stop(); go(current - 1); });
    btnNext.addEventListener("click", function () { stop(); go(current + 1); });
    btnReplay.addEventListener("click", function () { stop(); go(1); });
    btnPlay.addEventListener("click", play);
    dots.forEach(function (dot) {
      dot.addEventListener("click", function () {
        stop();
        go(parseInt(dot.getAttribute("data-goto"), 10));
      });
    });

    root.setAttribute("tabindex", "0");
    root.addEventListener("keydown", function (e) {
      if (e.key === "ArrowRight") { stop(); go(current + 1); e.preventDefault(); }
      else if (e.key === "ArrowLeft") { stop(); go(current - 1); e.preventDefault(); }
    });

    render();
  }

  function init() {
    document.querySelectorAll(".anim[data-anim]").forEach(setup);
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();

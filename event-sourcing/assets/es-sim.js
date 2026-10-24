/* ===========================================================================
   Event Sourcing — interactive simulators.
   Drop-in widgets; each renders itself into an empty container:

     <div class="sim" data-sim="replay"></div>        time-travel / fold over events vs CRUD row
     <div class="sim" data-sim="concurrency"></div>   two writers, expectedVersion on/off
     <div class="sim" data-sim="gap"></div>           global-position gaps: naive vs visibility-safe poller
     <div class="sim" data-sim="projection"></div>    crash between update & checkpoint; idempotency guard
     <div class="sim" data-sim="upcast"></div>        V1 → V2 event upcasting on read

   No dependencies. Everything is in-memory and resettable.
   =========================================================================== */
(function () {
  "use strict";

  function h(tag, attrs, html) {
    var el = document.createElement(tag);
    if (attrs) Object.keys(attrs).forEach(function (k) { el.setAttribute(k, attrs[k]); });
    if (html !== undefined) el.innerHTML = html;
    return el;
  }
  function esc(s) { return String(s).replace(/[&<>"]/g, function (c) { return { "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;" }[c]; }); }
  function logger(box) {
    box.setAttribute("role", "log");
    box.setAttribute("aria-live", "polite");
    box.setAttribute("aria-label", "Simulator activity");
    return function (msg, cls) {
      var d = h("div", { "class": cls || "" }, esc(msg));
      box.appendChild(d);
      box.scrollTop = box.scrollHeight;
    };
  }

  /* ---------------------------------------------------------------- replay */
  function replay(root) {
    var seed = [
      { type: "AccountOpened", data: { owner: "Alice" } },
      { type: "MoneyDeposited", data: { amount: 100 } },
      { type: "MoneyWithdrawn", data: { amount: 50 } },
      { type: "MoneyDeposited", data: { amount: 20 } }
    ];
    var events, pos;

    root.innerHTML =
      '<p class="sim-title">Simulator · replay &amp; time travel</p>' +
      '<p class="sim-intro">Drag the slider to replay the stream up to any version. Issue commands &mdash; <code>decide()</code> may reject them; accepted ones are appended as new events. Compare with the CRUD row, which only ever knows &ldquo;now&rdquo;.</p>' +
      '<div class="sim-grid">' +
        '<div class="sim-panel"><h4>Event stream <span class="sim-badge" data-ver></span></h4><ul class="sim-events" data-list></ul>' +
          '<input type="range" min="0" value="0" data-slider aria-label="Replay up to version"></div>' +
        '<div class="sim-panel"><h4>State = fold(evolve) up to v<span data-at></span></h4>' +
          '<div class="sim-big" data-bal></div><div class="sim-state" data-state></div>' +
          '<h4 style="margin-top:.8rem">CRUD row (no history)</h4><div class="sim-state" data-crud></div></div>' +
      '</div>' +
      '<div class="sim-btns" style="margin-top:.7rem">Amount <input type="number" value="30" min="1" data-amt aria-label="Transaction amount">' +
        '<button class="primary" data-dep>Deposit</button><button class="primary" data-wd>Withdraw</button>' +
        '<button data-reset>Reset</button></div>' +
      '<div class="sim-log" data-log></div>';

    var $ = function (s) { return root.querySelector(s); };
    var list = $("[data-list]"), slider = $("[data-slider]"), log = logger($("[data-log]"));

    function fold(n) {
      var s = { status: "NotOpened", balance: 0 };
      for (var i = 0; i < n; i++) {
        var e = events[i];
        if (e.type === "AccountOpened") s = { status: "Open", owner: e.data.owner, balance: 0 };
        else if (e.type === "MoneyDeposited") s.balance += e.data.amount;
        else if (e.type === "MoneyWithdrawn") s.balance -= e.data.amount;
      }
      return s;
    }
    function label(e) {
      if (e.type === "AccountOpened") return "AccountOpened { owner: " + e.data.owner + " }";
      return e.type + " { amount: " + e.data.amount + " }";
    }
    function render() {
      list.innerHTML = "";
      events.forEach(function (e, i) {
        var cls = i < pos ? "applied" : "future";
        if (i === pos - 1) cls += " current";
        list.appendChild(h("li", { "class": cls }, '<span class="pos">v' + (i + 1) + '</span>' + esc(label(e))));
      });
      slider.max = events.length; slider.value = pos;
      var s = fold(pos), head = fold(events.length);
      $("[data-at]").textContent = pos;
      $("[data-ver]").textContent = "head v" + events.length;
      $("[data-bal]").textContent = s.status === "Open" ? "€" + s.balance : "—";
      $("[data-state]").textContent = JSON.stringify(s, null, 1);
      $("[data-crud]").textContent = "accounts\n  id=ACC-1  owner=Alice  balance=" + head.balance + "\n\n(\"What was it at v" + pos + "?\" → unknowable)";
    }
    function command(kind) {
      var amt = parseInt($("[data-amt]").value, 10);
      var state = fold(events.length);
      if (!(amt > 0)) { log("✗ decide → rejected: amount must be positive", "err"); return; }
      if (kind === "wd" && state.balance - amt < 0) {
        log("✗ Withdraw(" + amt + ") → decide rejected: Insufficient funds (balance " + state.balance + "). Nothing appended.", "err");
        return;
      }
      var ev = { type: kind === "dep" ? "MoneyDeposited" : "MoneyWithdrawn", data: { amount: amt } };
      events.push(ev); pos = events.length;
      log("✓ " + (kind === "dep" ? "Deposit" : "Withdraw") + "(" + amt + ") → append " + ev.type + " @ expectedVersion " + (events.length - 1) + " → v" + events.length, "ok");
      render();
    }
    function reset() {
      events = seed.map(function (e) { return { type: e.type, data: Object.assign({}, e.data) }; });
      pos = events.length; $("[data-log]").innerHTML = "";
      log("Loaded stream account-ACC-1 (4 events). Slide left to time-travel.", "dim");
      render();
    }
    slider.addEventListener("input", function () { pos = parseInt(slider.value, 10); render(); });
    $("[data-dep]").addEventListener("click", function () { command("dep"); });
    $("[data-wd]").addEventListener("click", function () { command("wd"); });
    $("[data-reset]").addEventListener("click", reset);
    reset();
  }

  /* ----------------------------------------------------------- concurrency */
  function concurrency(root) {
    var store, clients;
    root.innerHTML =
      '<p class="sim-title">Simulator · optimistic concurrency</p>' +
      '<p class="sim-intro">Two tellers load the same account and each try to withdraw €80 from €100. Try it with the expected-version check on, then off. Suggested order: Load A → Load B → Withdraw A → Withdraw B.</p>' +
      '<label class="sim-toggle"><input type="checkbox" checked data-occ> enforce <code>expectedVersion</code> (UNIQUE(stream_id, stream_version))</label>' +
      '<div class="sim-grid three" style="margin-top:.6rem">' +
        '<div class="sim-panel" data-client="A"><h4>Teller A</h4><div class="sim-state" data-view></div><div class="sim-btns"><button data-load>Load</button><button class="primary" data-wd>Withdraw 80</button></div></div>' +
        '<div class="sim-panel"><h4>Event store · account-ACC-1</h4><ul class="sim-events" data-list></ul><div class="sim-big" data-bal></div></div>' +
        '<div class="sim-panel" data-client="B"><h4>Teller B</h4><div class="sim-state" data-view></div><div class="sim-btns"><button data-load>Load</button><button class="primary" data-wd>Withdraw 80</button></div></div>' +
      '</div><div class="sim-btns"><button data-reset>Reset</button></div><div class="sim-log" data-log></div>';
    var $ = function (s) { return root.querySelector(s); };
    var log = logger($("[data-log]"));

    function balance(evts) { return evts.reduce(function (b, e) { return b + (e.type === "MoneyDeposited" ? e.amount : e.type === "MoneyWithdrawn" ? -e.amount : 0); }, 0); }
    function render() {
      var list = $("[data-list]"); list.innerHTML = "";
      store.forEach(function (e, i) {
        list.appendChild(h("li", { "class": "applied" + (e.dup ? " missed" : "") }, '<span class="pos">v' + (i + 1) + '</span>' + esc(e.type + (e.amount ? " " + e.amount : "")) + (e.by ? ' <span class="sim-badge">' + e.by + '</span>' : "")));
      });
      var b = balance(store);
      $("[data-bal]").innerHTML = "€" + b + (b < 0 ? ' <span class="sim-badge bad">invariant broken</span>' : "");
      ["A", "B"].forEach(function (n) {
        var c = clients[n];
        root.querySelector('[data-client="' + n + '"] [data-view]').textContent =
          c.version === null ? "(not loaded)" : "loaded version: " + c.version + "\nbalance seen: €" + c.balance;
      });
    }
    function load(n) {
      clients[n] = { version: store.length, balance: balance(store) };
      log("Teller " + n + " → readStream → v" + store.length + ", balance €" + clients[n].balance, "dim");
      render();
    }
    function withdraw(n) {
      var c = clients[n];
      if (c.version === null) { log("Teller " + n + ": load first", "warn"); return; }
      if (c.balance - 80 < 0) { log("Teller " + n + ": decide → Insufficient funds (state @v" + c.version + ")", "err"); return; }
      var occ = $("[data-occ]").checked;
      if (occ && c.version !== store.length) {
        log("Teller " + n + ": INSERT stream_version=" + (c.version + 1) + " → 23505 unique_violation → ConcurrencyException (expected v" + c.version + ", actual v" + store.length + ")", "err");
        log("  ↳ handler retries: reload v" + store.length + " (balance €" + balance(store) + ") and re-decide…", "warn");
        clients[n] = { version: store.length, balance: balance(store) };
        if (clients[n].balance - 80 < 0) log("  ↳ decide → Insufficient funds. Correctly rejected ✓", "ok");
        render(); return;
      }
      var lost = c.version !== store.length;
      store.push({ type: "MoneyWithdrawn", amount: 80, by: n, dup: lost });
      clients[n] = { version: store.length, balance: balance(store) };
      log("Teller " + n + ": append MoneyWithdrawn 80 → v" + store.length + (lost ? "  ⚠ decided on stale state (lost update)" : ""), lost ? "err" : "ok");
      render();
    }
    function reset() {
      store = [{ type: "AccountOpened" }, { type: "MoneyDeposited", amount: 100 }];
      clients = { A: { version: null }, B: { version: null } };
      $("[data-log]").innerHTML = ""; log("Stream at v2, balance €100.", "dim"); render();
    }
    ["A", "B"].forEach(function (n) {
      var p = root.querySelector('[data-client="' + n + '"]');
      p.querySelector("[data-load]").addEventListener("click", function () { load(n); });
      p.querySelector("[data-wd]").addEventListener("click", function () { withdraw(n); });
    });
    $("[data-reset]").addEventListener("click", reset);
    reset();
  }

  /* -------------------------------------------------------------------- gap */
  function gap(root) {
    var rows, seq, checkpoint, processed;
    root.innerHTML =
      '<p class="sim-title">Simulator · the global-position gap</p>' +
      '<p class="sim-intro">Positions come from a sequence at <em>insert</em> time but become visible at <em>commit</em> time. Reproduce the bug: Begin tx → Begin tx → Commit the <strong>second</strong> → Poll → Commit the first → Poll. Then switch to the safe poller and try again.</p>' +
      '<label class="sim-toggle"><input type="radio" name="gapmode" value="naive" checked data-mode> naive: <code>WHERE pos &gt; checkpoint</code></label>' +
      '<label class="sim-toggle"><input type="radio" name="gapmode" value="safe" data-mode> safe: only rows from tx older than oldest in-flight tx (<code>pg_snapshot_xmin</code>)</label>' +
      '<div class="sim-grid" style="margin-top:.6rem">' +
        '<div class="sim-panel"><h4>events table (writer view)</h4><ul class="sim-events" data-rows></ul>' +
          '<div class="sim-btns"><button class="primary" data-begin>Begin tx + INSERT</button></div></div>' +
        '<div class="sim-panel"><h4>Projection <span class="sim-badge" data-cp></span></h4><ul class="sim-events" data-proc></ul>' +
          '<div class="sim-btns"><button class="primary" data-poll>Poll</button><button data-reset>Reset</button></div></div>' +
      '</div><div class="sim-log" data-log></div>';
    var $ = function (s) { return root.querySelector(s); };
    var log = logger($("[data-log]"));
    function mode() { return root.querySelector("[data-mode]:checked").value; }

    function render() {
      var r = $("[data-rows]"); r.innerHTML = "";
      rows.forEach(function (row) {
        var missed = row.state === "committed" && row.pos <= checkpoint && processed.indexOf(row.pos) < 0;
        var cls = row.state === "inflight" ? "invisible" : row.state === "rolledback" ? "future" : "applied";
        if (missed) cls += " missed";
        var li = h("li", { "class": cls },
          '<span class="pos">#' + row.pos + '</span>' + esc(row.name) + ' <span class="sim-badge ' + (row.state === "committed" ? "good" : "") + '">' + row.state + (missed ? " · SKIPPED FOREVER" : "") + "</span>");
        if (row.state === "inflight") {
          var c = h("button", {}, "Commit"), rb = h("button", {}, "Rollback");
          c.style.marginLeft = "auto"; c.addEventListener("click", function () { row.state = "committed"; log("COMMIT tx for #" + row.pos + " → now visible", "ok"); render(); });
          rb.addEventListener("click", function () { row.state = "rolledback"; log("ROLLBACK tx for #" + row.pos + " → permanent hole in sequence", "warn"); render(); });
          li.appendChild(c); li.appendChild(rb);
        }
        r.appendChild(li);
      });
      var p = $("[data-proc]"); p.innerHTML = "";
      processed.forEach(function (n) { p.appendChild(h("li", { "class": "applied" }, '<span class="pos">#' + n + '</span>handled')); });
      $("[data-cp]").textContent = "checkpoint = " + checkpoint;
    }
    function begin() {
      seq++; rows.push({ pos: seq, name: "Event " + String.fromCharCode(64 + seq), state: "inflight" });
      log("BEGIN; INSERT … → nextval() = " + seq + " (uncommitted, invisible to readers)", "dim"); render();
    }
    function poll() {
      var visible = rows.filter(function (r) { return r.state === "committed" && r.pos > checkpoint; });
      if (mode() === "safe") {
        var inflight = rows.filter(function (r) { return r.state === "inflight"; }).map(function (r) { return r.pos; });
        var xmin = inflight.length ? Math.min.apply(null, inflight) : Infinity;
        visible = visible.filter(function (r) { return r.pos < xmin; });
        if (xmin !== Infinity) log("safe poll: oldest in-flight tx owns #" + xmin + " → read only below it", "dim");
      }
      if (!visible.length) { log("poll → nothing new", "dim"); render(); return; }
      visible.forEach(function (r) { processed.push(r.pos); checkpoint = Math.max(checkpoint, r.pos); });
      log("poll → handled [" + visible.map(function (r) { return "#" + r.pos; }).join(", ") + "], checkpoint = " + checkpoint, "ok");
      render();
    }
    function reset() { rows = []; seq = 100; checkpoint = 100; processed = []; $("[data-log]").innerHTML = ""; log("Sequence at 100. Checkpoint 100.", "dim"); render(); }
    $("[data-begin]").addEventListener("click", begin);
    $("[data-poll]").addEventListener("click", poll);
    $("[data-reset]").addEventListener("click", reset);
    root.querySelectorAll("[data-mode]").forEach(function (m) { m.addEventListener("change", reset); });
    reset();
  }

  /* ------------------------------------------------------------- projection */
  function projection(root) {
    var amounts = [100, 40, 25, 60, 15, 80];
    var next, balance, checkpoint, rowPos;
    root.innerHTML =
      '<p class="sim-title">Simulator · projection crash &amp; idempotency</p>' +
      '<p class="sim-intro">Delivery is at-least-once. Crash the projector <em>after</em> it updated the read model but <em>before</em> it saved the checkpoint, then restart. Toggle the two safeguards and see which one(s) save you.</p>' +
      '<label class="sim-toggle"><input type="checkbox" data-sametx> checkpoint in the same DB transaction</label>' +
      '<label class="sim-toggle"><input type="checkbox" data-guard> idempotency guard (<code>WHERE last_position &lt; :pos</code>)</label>' +
      '<div class="sim-grid" style="margin-top:.6rem">' +
        '<div class="sim-panel"><h4>Global log (MoneyDeposited)</h4><ul class="sim-events" data-list></ul></div>' +
        '<div class="sim-panel"><h4>Read model · rm_account_balance</h4><div class="sim-big" data-bal></div><div class="sim-state" data-row></div><div data-verdict></div></div>' +
      '</div>' +
      '<div class="sim-btns"><button class="primary" data-step>Process next</button><button data-crash>Process next, crash before checkpoint 💥</button><button data-restart>Restart projector</button><button data-reset>Reset</button></div>' +
      '<div class="sim-log" data-log></div>';
    var $ = function (s) { return root.querySelector(s); };
    var log = logger($("[data-log]"));
    var crashed = false;

    function expected() { var s = 0; for (var i = 0; i < checkpointOrApplied(); i++) s += amounts[i]; return s; }
    function checkpointOrApplied() { return Math.max(checkpoint, rowPos); }
    function render() {
      var list = $("[data-list]"); list.innerHTML = "";
      amounts.forEach(function (a, i) {
        var p = i + 1, cls = p <= checkpoint ? "applied" : "future";
        if (p === next && !crashed) cls += " current";
        list.appendChild(h("li", { "class": cls }, '<span class="pos">#' + p + '</span>MoneyDeposited ' + a));
      });
      $("[data-bal]").textContent = "€" + balance;
      $("[data-row]").textContent = "balance       = " + balance + "\nlast_position = " + rowPos + "\ncheckpoint    = " + checkpoint + (crashed ? "\n(projector DOWN)" : "");
      var ok = balance === expected();
      $("[data-verdict]").innerHTML = ok ? '<span class="sim-badge good">consistent with log</span>' : '<span class="sim-badge bad">double-counted! expected €' + expected() + "</span>";
    }
    function apply(pos, crash) {
      if (crashed) { log("projector is down — restart it", "warn"); return; }
      if (pos > amounts.length) { log("caught up to head", "dim"); return; }
      var sameTx = $("[data-sametx]").checked, guard = $("[data-guard]").checked;
      if (crash && sameTx) {
        log("#" + pos + ": UPDATE + checkpoint in one tx → crash before COMMIT → whole tx rolled back. Nothing applied.", "warn");
        crashed = true; render(); return;
      }
      if (guard && rowPos >= pos) {
        log("#" + pos + ": UPDATE … WHERE last_position < " + pos + " → 0 rows (already applied). Duplicate ignored ✓", "ok");
      } else {
        balance += amounts[pos - 1]; rowPos = pos;
        log("#" + pos + ": balance += " + amounts[pos - 1] + " → " + balance, "ok");
      }
      if (crash) { crashed = true; log("💥 crash before saving checkpoint (still " + checkpoint + ")", "err"); render(); return; }
      checkpoint = pos; next = pos + 1; render();
    }
    function restart() {
      if (!crashed) { log("projector already running", "dim"); return; }
      crashed = false; next = checkpoint + 1;
      log("restart → resume from checkpoint " + checkpoint + " → redeliver #" + next, "warn");
      apply(next, false);
    }
    function reset() { next = 1; balance = 0; checkpoint = 0; rowPos = 0; crashed = false; $("[data-log]").innerHTML = ""; log("Fresh projection at checkpoint 0.", "dim"); render(); }
    $("[data-step]").addEventListener("click", function () { apply(next, false); });
    $("[data-crash]").addEventListener("click", function () { apply(next, true); });
    $("[data-restart]").addEventListener("click", restart);
    $("[data-reset]").addEventListener("click", reset);
    reset();
  }

  /* ----------------------------------------------------------------- upcast */
  function upcast(root) {
    var stored = [
      { v: 1, type: "MoneyDeposited", data: { amount: 50 } },
      { v: 1, type: "MoneyDeposited", data: { amount: 20 } },
      { v: 2, type: "MoneyDeposited", data: { amount: { value: "75.00", currency: "EUR" } } },
      { v: 3, type: "MoneyDeposited", data: { amount: { value: "10.00", currency: "USD" }, channel: "ATM" } }
    ];
    root.innerHTML =
      '<p class="sim-title">Simulator · upcasting on read</p>' +
      '<p class="sim-intro">Stored events are never rewritten. On read, a chain of upcasters lifts each raw event to the latest schema, so domain code only knows v3. Toggle upcasters off to see what the deserialiser would face.</p>' +
      '<label class="sim-toggle"><input type="checkbox" checked data-u1> v1→v2 (number → {value, currency: EUR})</label>' +
      '<label class="sim-toggle"><input type="checkbox" checked data-u2> v2→v3 (add channel = "UNKNOWN")</label>' +
      '<div class="sim-grid" style="margin-top:.6rem"><div class="sim-panel"><h4>events table (immutable)</h4><div data-raw></div></div>' +
      '<div class="sim-panel"><h4>What evolve() receives</h4><div data-out></div></div></div>';
    var $ = function (s) { return root.querySelector(s); };
    function render() {
      var u1 = $("[data-u1]").checked, u2 = $("[data-u2]").checked;
      $("[data-raw]").innerHTML = ""; $("[data-out]").innerHTML = "";
      stored.forEach(function (e) {
        $("[data-raw]").appendChild(h("div", { "class": "sim-state" }, "schema v" + e.v + "\n" + esc(JSON.stringify(e.data))));
        var cur = { v: e.v, data: JSON.parse(JSON.stringify(e.data)) }, steps = [];
        if (cur.v === 1 && u1) { cur.data.amount = { value: cur.data.amount.toFixed(2), currency: "EUR" }; cur.v = 2; steps.push("v1→v2"); }
        if (cur.v === 2 && u2) { cur.data.channel = "UNKNOWN"; cur.v = 3; steps.push("v2→v3"); }
        var ok = cur.v === 3;
        $("[data-out]").appendChild(h("div", { "class": "sim-state" },
          (ok ? '<span class="sim-badge good">v3 ✓</span> ' : '<span class="sim-badge bad">v' + cur.v + ' — deserialisation fails</span> ') +
          (steps.length ? '<span class="sim-badge">' + steps.join(" · ") + "</span>" : "") + "\n" + esc(JSON.stringify(cur.data))));
      });
    }
    root.querySelectorAll("input").forEach(function (i) { i.addEventListener("change", render); });
    render();
  }

  var registry = { replay: replay, concurrency: concurrency, gap: gap, projection: projection, upcast: upcast };
  function init() {
    document.querySelectorAll(".sim[data-sim]").forEach(function (el) {
      var fn = registry[el.getAttribute("data-sim")];
      if (fn) fn(el);
    });
  }
  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", init); else init();
})();

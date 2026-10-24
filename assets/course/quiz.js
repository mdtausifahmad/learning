/* ===========================================================================
   Reusable quiz widget for retrieval practice.
   Immediate, automatic feedback = tight learning loop (see SKILL philosophy).

   Markup convention (options kept ~equal length so formatting leaks no clues):

   <div class="quiz" data-quiz>
     <p class="quiz-q">Question?</p>
     <ul class="quiz-options">
       <li><button data-correct="false" data-feedback="Why this is wrong.">Option A</button></li>
       <li><button data-correct="true"  data-feedback="Why this is right.">Option B</button></li>
     </ul>
     <div class="quiz-explain" hidden></div>
   </div>

   - data-feedback is optional per option.
   - Add data-retry to the .quiz element to let the learner keep trying until correct.
   =========================================================================== */
(function () {
  "use strict";

  function handleQuiz(quiz) {
    var buttons = Array.prototype.slice.call(quiz.querySelectorAll(".quiz-options button"));
    var explain = quiz.querySelector(".quiz-explain");
    var allowRetry = quiz.hasAttribute("data-retry");
    var answered = false;
    var question = quiz.querySelector(".quiz-q");
    if (question) {
      if (!question.id) question.id = "quiz-question-" + Array.prototype.indexOf.call(document.querySelectorAll(".quiz[data-quiz]"), quiz);
      quiz.setAttribute("role", "group");
      quiz.setAttribute("aria-labelledby", question.id);
    }
    if (explain) {
      explain.setAttribute("role", "status");
      explain.setAttribute("aria-live", "polite");
      explain.setAttribute("aria-atomic", "true");
      // Establish the empty live region before a learner requests feedback.
      explain.hidden = false;
    }

    buttons.forEach(function (btn) {
      btn.addEventListener("click", function () {
        if (btn.getAttribute("aria-disabled") === "true") return;
        if (answered && !allowRetry) return;

        var isCorrect = btn.getAttribute("data-correct") === "true";

        if (allowRetry && !isCorrect) {
          // Gentle retry mode: mark just this wrong choice, keep going.
          btn.classList.add("incorrect");
          btn.setAttribute("aria-disabled", "true");
          revealFeedback(explain, false, btn.getAttribute("data-feedback"));
          return;
        }

        answered = true;
        btn.classList.add(isCorrect ? "correct" : "incorrect");

        // Always reveal the correct option so the learner sees the truth.
        buttons.forEach(function (b) {
          if (b.getAttribute("data-correct") === "true") b.classList.add("correct");
          // Preserve keyboard focus on the chosen answer while announcing feedback.
          b.setAttribute("aria-disabled", "true");
        });

        revealFeedback(explain, isCorrect, btn.getAttribute("data-feedback"));
      });
    });
  }

  function revealFeedback(explain, isCorrect, feedback) {
    if (!explain) return;
    var verdict = isCorrect
      ? '<span class="verdict ok">Correct.</span> '
      : '<span class="verdict no">Not quite.</span> ';
    var base = explain.getAttribute("data-base") || "";
    explain.innerHTML = verdict + (feedback ? feedback + " " : "") + base;
    explain.hidden = false;
  }

  function init() {
    document.querySelectorAll(".quiz[data-quiz]").forEach(handleQuiz);
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();

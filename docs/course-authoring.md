# Course-authoring workflow

Read this when adding a learning topic, creating a course, or extending an existing course. Follow the requested scope without adding an approval gate for routine curriculum or implementation decisions.

## 1. Establish the learning contract

Read the root README and the target course's existing metadata. For a new course, inspect the maps and one representative lesson in both `event-sourcing/` and `async-messaging/`; copy their depth and teaching process rather than their subject-specific assumptions.

Write or update `MISSION.md` with:

- Intended learner and assumed prerequisites.
- Concrete capabilities the learner should gain: explain, implement, debug, compare, or design something observable.
- Scope, exclusions, practical stack and a realistic suggested study pace.
- What independent practice or a capstone will demonstrate those capabilities.

Use the current request and existing course notes to make reasonable choices. Ask about a missing constraint only when it materially changes the course; continue independent work meanwhile. State assumptions when information is unavailable. A topic's audience need not be pursuing Staff or Principal.

**Done when:** the mission names observable learning outcomes and the assumptions needed to teach them.

## 2. Build the progression

Sequence the course around prerequisites and increasingly demanding tasks. Adapt this default arc to the subject:

| Stage | Teaching focus | Evidence of learning |
|---|---|---|
| Foundations | Vocabulary, motivating problem, simplest mental model | Explain a small example and distinguish similar concepts. |
| Core mechanics | Internal state, transitions, guarantees, implementation | Trace a mechanism and build its simplest correct form. |
| Practical application | Integrate the idea into a realistic problem | Implement a working example and explain its behavior. |
| Failure and operation | Broken assumptions, concurrency, recovery, testing, observability where relevant | Reproduce and diagnose a failure, then justify a repair. |
| Advanced variations | Harder constraints, combinations, scale, evolution | Adapt the model to an unfamiliar case. |
| Judgment and synthesis | Alternatives, boundaries, design decisions and capstone | Defend a choice and explain when another approach is better. |

For algorithms, correctness proofs, complexity and constraint changes serve the same role as guarantees, failure handling and production trade-offs in systems courses. For leadership topics, use situations, competing objectives, worked decisions and realistic exercises.

Keep each lesson focused on one achievable learning outcome. Use multiple lessons for a large subject rather than reducing it to bullet points. Give prerequisites and a suggested study block; estimates are guidance, not evidence of mastery. Lesson counts follow the topic, not the counts of the reference courses.

**Done when:** the course map covers the promised scope in prerequisite order, with a practice task for every major outcome.

## 3. Create the course artifacts

Use a descriptive topic folder and this default structure:

```text
<topic>/
  index.html                 # Course map, prerequisites, study route, linked artifacts
  MISSION.md                 # Learning contract and scope
  NOTES.md                   # Topic-specific teaching choices and learner preferences
  GLOSSARY.md                # Terms introduced in this course
  RESOURCES.md               # Primary sources, provenance and verification limits
  GUIDE.md                   # Complete continuous reading edition
  lessons/
    0001-descriptive-name.html
  reference/
    0001-descriptive-name-cheatsheet.html
  assets/
    theme.css                # Topic identity over shared course styles
    <topic>-sim.js            # Only when topic-specific simulations are needed
    lab/                     # Optional runnable lab, setup, verification and cleanup
  learning-records/           # Dated delivery or evidence-based learning notes
  capstone/                  # Optional project brief, milestones and acceptance criteria
```

Use existing naming conventions when extending a topic. Add retrieval sheets for the major lessons or concepts; one sheet may cover a coherent group. The course map must link every delivered lesson, reference and lab. Metadata files should contain useful content, not empty template headings.

`GUIDE.md` must teach the same core material as the HTML lessons in a continuous form, including worked examples, reasoning, code where relevant, diagrams and exercises. Keep both editions synchronized. Topic-specific content sources or generators are acceptable when they keep outputs consistent; document how to rebuild and check them.

Preserve existing study notes and records. A delivery record may list created artifacts and validation; record the learner's understanding or progress only when the conversation or completed work supports it.

**Done when:** the requested scope exists as complete learning material, with a course map and reading edition, not merely a scaffold.

## 4. Teach each lesson

Use this sequence, adapting section names to the material:

1. **Orient.** State the outcome, prerequisite links and concrete problem this lesson solves. Define unfamiliar terms when first needed.
2. **Start small.** Work through a basic example or baseline, explaining each decision. Identify the limitation that motivates the next idea.
3. **Expose the mechanism.** Show internal state and transitions, the invariants or guarantees, and why the design preserves them. Explain how and why, not just API usage.
4. **Make it visible.** Include a relevant diagram and a narrated walkthrough. Label entities, arrows and state changes. A trace needs concrete values; an architecture diagram needs clear responsibilities.
5. **Apply it.** Provide realistic code or a worked scenario. Explain contracts, dependencies, inputs, outputs and edge cases. Supply helpers and setup needed to reproduce a runnable example; label intentionally partial fragments or learner TODOs.
6. **Break or vary it.** Give a specific counterexample or failure scenario. Change an assumption, explain the consequence, then derive the repair or an alternative.
7. **Compare choices.** Name trade-offs, operational or complexity costs, alternatives, and when to avoid the technique.
8. **Practice retrieval.** Usually include three reasoning-focused quiz questions, with plausible options of comparable length and explanations for each option. Use the shared quiz widget for ordinary courses. Keep answers hidden until requested or attempted.
9. **Transfer.** Give an independent exercise, a changed scenario, or a task to try in the learner's own system, with acceptance criteria and hints separated from the solution.
10. **Connect.** Link the retrieval sheet, previous/next lesson, course map and relevant primary references.

Maintain one recognizable domain example across related systems lessons where useful. Increase difficulty by changing constraints or removing guidance, rather than merely adding jargon. Technical code follows the course's declared stack: Async Messaging's Kotlin/Spring preference and Event Sourcing's Java/Postgres examples are course-specific. State new-course choices in its mission; use a framework only when the topic calls for it.

## 5. Build meaningful interaction and practice

Reuse the styles and widget contracts in `assets/course/`. Read existing markup before introducing a new widget. Put course-specific assets in its folder; keep shared changes compatible with sibling courses.

- A guided walkthrough explains fixed example states with previous/next/reset controls. Label it as a walkthrough.
- A live simulator recomputes behavior from the learner's input and explains the resulting state. Label simplifications; a browser model is not proof that the real service or JVM code behaved identically.
- Exercises should progress from basic reconstruction through application to an unfamiliar variation. Provide expected observations or acceptance criteria, not only commands to run.
- Add a lab when executing the mechanism materially improves understanding. Prefer Podman for container examples, following existing lab conventions. Document prerequisites, startup, verification, failure experiments and cleanup; keep effects local to the lab's own resources.
- A capstone should bring several lessons together, with milestones and assessable criteria. Separate learner exercises from complete reference solutions.

Use accessible native controls, readable code, keyboard focus, reduced-motion support, and layouts that work on narrow screens. Reuse the site's diagram approach where appropriate; if a course promises offline use, its required diagrams and interactions must use local assets or include an offline fallback.

## 6. Integrate with the library

- Follow [the ten-area classification guide](library-areas.md). Add the course to its primary area in `assets/library-areas.json`; use related-learning links for relevant material in other areas. Run `python3 scripts/build-library.py` to synchronize the directory in root `index.html` and `README.md`. Extend topics within the agreed areas; change the broad classification only when the user requests it.
- Add a visible library return link to its course map. Cross-link related courses and state prerequisites rather than repeating whole chapters.
- Keep working source and published links: ordinary courses use `assets/course/links.js` for local Markdown and Jekyll HTML handling. Respect `_config.yml` and its exclusions; avoid two sources writing the same published HTML path.
- Rebuild shared navigation with `python3 scripts/build-navigation.py` and audit it with `python3 scripts/check-navigation.py`. Follow [navigation conventions](navigation.md): every learning page needs its library, area, course and section links, plus previous/next links within its sequence.
- Check that metadata, lesson counts, reading editions, reference links and previous/next navigation match the final content. Retain existing URLs when inserting or extending lessons.

## 7. Verify before delivery

Check the changed scope, using existing course scripts when available:

- Open every new local link and anchor structurally, accounting for Jekyll-generated pages and local Markdown fallbacks. Check that the map and full guide cover the same promised topics.
- Validate code in the declared language and version when tooling permits. Test examples against their stated contracts and meaningful boundary/failure cases. Mark snippets that were only reviewed, and report unavailable tooling accurately.
- Exercise quizzes, walkthrough controls, simulator inputs and resets. Check invalid input and edge states; confirm explanations match behavior.
- Inspect desktop and narrow-screen rendering and keyboard navigation when browser tooling permits. Distinguish browser verification, DOM simulation and static checks in the report.
- Verify research against primary sources where access permits, recording source URLs and relevant versions/dates. Keep unvisited references explicitly unverified; explain access blockers without inventing findings.
- Run relevant syntax and whitespace checks. Limit broader test runs to shared behavior affected by the change.

**Done when:** the requested learning material is complete and integrated; the applicable checks pass or concrete limitations are documented. Report delivered artifacts and verification separately from the learner's mastery.

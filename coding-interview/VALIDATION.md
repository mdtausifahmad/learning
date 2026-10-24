# Validation record

24 October 2026. The delivered curriculum contains **20 patterns, 81 worked problems, and 126 additional practice variations**. Difficulty terms are teaching labels; they are not current employer frequency data.

- All 81 Java snippets compiled using `javac --release 17` and passed executable example and boundary assertions.
- All 81 Kotlin snippets compiled with Kotlin 1.9.25, running on Java 21 and targeting JVM 17, and passed executable example and boundary assertions. The compiler and its dependencies were already cached locally; nothing was downloaded.
- All 20 authored lesson contracts passed structural checks, including minimum depth, practice counts, concrete trace steps, quizzes, complete code and executable checks.
- Local references and anchors passed across 45 generated HTML pages. Optional external links were not visited and are not claimed to be current.
- The shipped UI script passed **254 in-memory DOM checks** against generated pages: search, every lesson's trace controls, quiz feedback, language switching, progress persistence, unavailable/corrupt storage, the two input-driven state experiments, invalid-input feedback, and embedded transfer prompts. This is a dependency-free simulation, not a browser renderer.
- A separate Java review passed **11,000 deterministic randomized differential cases** for median partition, range-sum counting, skyline, weighted scheduling and trapped water against simpler reference algorithms. Details and seed are in `content/linear-validation.json`; that author-run differential harness was temporary. The checked-in validator reruns the per-problem checks.
- JavaScript syntax and Git whitespace checks passed.

## Limits

Browser visual/responsive QA could not be completed. A local headless Chromium launch was blocked by the execution sandbox; the in-app browser rejected local `file:` navigation because its URL policy allows only HTTP/HTTPS. No alternate browser path was used to bypass that rejection. Responsive breakpoints, focus styles, native controls, reduced-motion handling and local assets are implemented, but their rendered appearance has not been visually verified in this run.

Current primary-source research remains incomplete. Automatic approval review rejected public Amazon and Microsoft source access because its configured `codex-auto-review` model was invalid. No external page was retrieved. See [RESOURCES.md](RESOURCES.md) for the dated review queue and what remains to verify after access is restored. Algorithm implementation checks do not verify company guidance or guarantee an interview outcome.

Generated HTML can be used directly without a build or network connection. The JavaScript is progressively enhanced: reading, code and native solution disclosures remain available if browser storage is unavailable.

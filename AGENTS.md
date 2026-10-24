# Software Engineering Library

This repository is a growing learning library for software engineers. Organize content by subject; Senior → Staff → Principal is one career-development path. The public entry points are the root `index.html` and `README.md`.

## Adding or extending learning topics

**Before creating a course, adding lessons, or expanding a topic, read [the course-authoring workflow](docs/course-authoring.md).** It defines the teaching progression, required artifacts, lesson structure, integration steps and completion checks.

Use these existing courses as references:

- [Event Sourcing](event-sourcing/index.html): staged foundations → mechanisms → production → advanced applications → judgment, with interactive examples and a runnable lab.
- [Async Messaging](async-messaging/index.html): practical explanations, internals, explicit trade-offs, failure scenarios and progressive exercises.

For an existing topic, first read its `MISSION.md`, `NOTES.md`, course map and relevant learning records. These describe that course's audience, stack and teaching preferences. Apply those preferences to that course; other topics may need different choices.

## Teaching requirements

- Begin with the learner's prerequisites and explain new concepts from first principles. Derive mechanisms through concrete examples before introducing abstractions.
- Teach **what happens, how it works internally, why it was designed that way, and what it costs**. Include failure modes, alternatives and when the technique is inappropriate.
- Progress from guided examples to independent practice and unfamiliar variations. Explain the reasoning behind each step, including the assumptions that make it valid.
- Pair diagrams with explanations of actual state changes. Use interactive traces or simulators where they improve understanding.
- Make the HTML lessons and full guide substantive teaching material, not outlines or collections of external links.
- Record delivery separately from learner progress. Passing a quiz, reading a page, and mastering a topic are different claims; learner records must reflect evidence.

## Repository conventions

- Reuse `assets/course/` for ordinary courses and keep topic-specific styling in `<topic>/assets/theme.css`. Inspect an existing lesson before using the shared quiz or animation APIs.
- Use relative links and a visible return link to the library. Support direct local reading and GitHub Pages; account for Markdown-to-HTML link handling in `assets/course/links.js`.
- After adding, removing, or reordering pages, run `python3 scripts/build-navigation.py` and `python3 scripts/check-navigation.py`. See [navigation conventions](docs/navigation.md) for section indexes, page sequences and published Markdown handling.
- Preserve existing lesson URLs and chapter identifiers when extending a course. Add cross-links rather than duplicating another course's teaching.
- Classify new topics using [the ten-area classification guide](docs/library-areas.md). Edit `assets/library-areas.json` and run `python3 scripts/build-library.py` to synchronize the home page and README. Give a course one primary home and cross-link relevant lessons in other areas. Keep goal-based learning paths separate.
- For generated content, edit its authoring source and generator, then rebuild. For coding-interview changes, read [its README](coding-interview/README.md) and [authoring contract](coding-interview/AUTHORING.md).
- Cite primary sources for technical claims. Label inaccessible references and unverified version-dependent claims accurately; a source URL alone is not evidence that it was reviewed.

## Completion

Follow the authoring workflow's verification checklist. Report what was delivered, which checks ran, and any concrete limitations. Keep the user's requested scope: a complete course request needs complete lessons; a single-lesson request needs the relevant lesson and synchronized course navigation.

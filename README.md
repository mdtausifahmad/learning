# Software Engineering Library

A central entry point for learning software engineering across ten areas: technical foundations, architecture, quality and operations, security, communication, product and domain knowledge, execution, leadership, learning, and career development. This is a growing library, with more topics and learning paths added over time.

**[Open the learning library](index.html).** Follow a course from the beginning, explore a subject in depth, or return to a reference when you need it at work.

## Choose a learning path

- **Prepare for coding interviews:** start with [problem-solving foundations](coding-interview/foundations.html), then practice patterns in Java and Kotlin.
- **Design systems:** begin with [System Design & Architecture at Scale](system-design/index.html), then deepen your understanding of messaging and event sourcing.
- **Understand production:** work through [Reliability & Observability](Observability/index.html).
- **Develop your engineering practice:** explore [Technical Skills](technical-skills/index.html) and [Leadership Skills](leadership-skills/index.html).
- **Plan career growth:** follow the [Senior → Staff → Principal career path](career-path/index.html).

<!-- library-areas:start -->
## Browse by area

The ten areas below are the library’s permanent broad classification. Courses have one primary home; focused lessons are cross-linked where they help. Areas without a dedicated course still show relevant existing material.

| Area | What belongs here | Available learning |
|---|---|---|
| 1. Technical foundations and implementation | Understanding computers and building software effectively | [Coding Interviews: Patterns & Problem Solving](coding-interview/index.html); [Technical Skills](technical-skills/index.html) |
| 2. Software design and architecture | Structuring systems and making technical trade-offs | [System Design & Architecture at Scale](system-design/index.html); [Async Messaging](async-messaging/index.html); [Event Sourcing](event-sourcing/index.html)<br>Related lessons: [Software architecture](technical-skills/lessons/0003-software-architecture.html) |
| 3. Quality, delivery, and operations | Making software correct, maintainable, deployable, and reliable | [Reliability & Observability](Observability/index.html)<br>Related lessons: [Cloud infrastructure](technical-skills/lessons/0007-cloud-infrastructure.html); [Testing event-sourced systems](event-sourcing/lessons/0014-testing-event-sourced-systems.html) |
| 4. Security, privacy, and responsible engineering | Protecting users and systems, and understanding engineering responsibilities | No dedicated course yet. Related: [Security and privacy](technical-skills/lessons/0006-security-and-privacy.html); [Privacy in event histories](event-sourcing/lessons/0013-gdpr-crypto-shredding-and-temporal-queries.html) |
| 5. Communication and collaboration | Working effectively with other people | No dedicated course yet. Related: [Technical writing: design docs, RFCs and ADRs](technical-skills/lessons/0005-technical-writing-docs-rfcs-adrs.html); [Communication and executive writing](leadership-skills/lessons/0002-communication-and-executive-writing.html); [Stakeholder management and trust](leadership-skills/lessons/0005-stakeholder-management-and-trust.html) |
| 6. Product, business, and domain knowledge | Understanding which problems matter and why | No dedicated course yet. Related: [Prioritization and impact](leadership-skills/lessons/0008-prioritization.html); [Cloud costs and pragmatic choices](technical-skills/lessons/0007-cloud-infrastructure.html) |
| 7. Planning and execution | Turning ambiguous goals into completed work | No dedicated course yet. Related: [Technical and project leadership](leadership-skills/lessons/0003-technical-and-project-leadership.html); [Prioritization](leadership-skills/lessons/0008-prioritization.html); [Back-of-the-envelope estimation](system-design/lessons/0007-back-of-envelope-estimation.html) |
| 8. Leadership and technical strategy | Helping teams and organizations make better decisions | [Leadership Skills](leadership-skills/index.html)<br>Related lessons: [Engineering strategy and technical vision](technical-skills/lessons/0004-engineering-strategy-and-vision.html) |
| 9. Learning, thinking, and personal effectiveness | Improving how you learn, reason, and work | No dedicated course yet. Related: [Reasoning from first principles](coding-interview/foundations.html); [Deliberate practice and reflection](coding-interview/practice.html) |
| 10. Career and professional development | Intentionally developing your professional direction | [Career path: Senior → Staff → Principal](career-path/index.html)<br>Related lessons: [Coding interview preparation](coding-interview/index.html); [Competency assessment and tracking](06-self-assessment-and-tracking.md); [T-shaped breadth and depth](technical-skills/lessons/0008-tshaped-breadth-and-depth.html) |

Example topics for all ten areas are listed on the [library home page](index.html#topics). Those scope lists include future additions; links identify available material.
<!-- library-areas:end -->

The career playbook is one path through the library. Individual courses can be studied independently; their course maps state the intended audience and prerequisites.

## How to learn here

1. Pick a subject or a goal from the library home page.
2. Use the course map to choose a lesson and check its prerequisites.
3. Work through examples, diagrams, quizzes and exercises where available.
4. Practice without the explanation, then return to the full guide or reference sheet.
5. Record what you learned and revisit the parts you cannot yet explain independently.

Most courses contain an `index.html` map, interactive pages in `lessons/`, retrieval sheets in `reference/`, and a `GUIDE.md` reading edition. Some add runnable labs or capstones. The coding course also includes a generated offline `GUIDE.html` and downloadable solutions.

Open `index.html` directly from disk or use the published GitHub Pages site. The coding course uses local assets throughout. Some other courses load Mermaid diagrams from a CDN or have labs with their own runtime requirements; check each course's instructions.

## Career development

The original Senior → Staff → Principal material now has a dedicated [career-path landing page](career-path/index.html) and [career guide](career-path/GUIDE.md). Its seven playbook chapters keep their existing root-level paths so saved links and course references continue to work.

## Adding topics as the library grows

Agents should follow [AGENTS.md](AGENTS.md) and its [course-authoring workflow](docs/course-authoring.md) when creating or extending learning material.

- Place a new course or deep dive in a descriptive subject folder with its own `index.html`. Use existing courses as examples, adapting the format to the subject.
- State the intended audience, prerequisites and learning outcomes. Explain from first principles where appropriate; include examples, practice and source provenance.
- Classify new material within the ten agreed areas in `assets/library-areas.json`, then run `python3 scripts/build-library.py` to update the home page and this directory. Give each course one primary home and cross-link focused lessons in other relevant areas. See [the classification guide](docs/library-areas.md).
- Link back to the library from the topic's course map. Keep links relative and preserve existing URLs when reorganizing content.
- Update a course's generator when its pages are generated, then rebuild the HTML. The coding course documents its authoring and checks in [its README](coding-interview/README.md).
- Check local links, anchors, keyboard navigation and small-screen layout. Distinguish source-backed findings from unverified references and personal learning notes.

The site uses Jekyll for published Markdown pages and serves course HTML directly. Site metadata lives in `_config.yml`; shared document styles are in `assets/site.css`; most existing courses share `assets/course/`. The GitHub repository URL remains `mdtausifahmad/principal-engineering`.

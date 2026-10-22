# Technical Skills — Resources

Curated, high-trust sources, grouped by lesson. Each lesson names one **primary source**; start there.
Verify a link before you rely on it, because books get new editions (DDIA's 2nd edition renumbers its chapters).

## Distributed systems ([Lesson 01](lessons/0001-data-intensive-distributed-systems.html))
- **Primary:** [Martin Kleppmann, *Designing Data-Intensive Applications*](https://dataintensive.net/): the
  reference for encoding, replication, consistency, consensus and derived data.
- [DDIA references](https://github.com/ept/ddia-references): the papers behind every chapter, for going deeper.
- [MIT 6.824 Distributed Systems](https://pdos.csail.mit.edu/6.824/): lectures and labs (Raft, sharded KV).
- [Kleppmann's distributed-systems lectures](https://www.youtube.com/playlist?list=PLeKd45zvjcDFUEv_ohr_HdUFe97RItdiB):
  clocks, causality and consensus explained from first principles.
- [Unmesh Joshi, *Patterns of Distributed Systems*](https://martinfowler.com/articles/patterns-of-distributed-systems/):
  named implementation patterns (leases, generation clocks, high-water marks).
- [The Raft site](https://raft.github.io/): the paper plus an interactive visualisation of leader election and log replication.

## Reliability & SRE ([Lesson 02](lessons/0002-reliability-observability-sre.html))
- **Primary:** [Google, *Site Reliability Engineering*](https://sre.google/sre-book/table-of-contents/): SLOs,
  error budgets, toil, overload, cascading failure and postmortems.
- [*The Site Reliability Workbook*](https://sre.google/workbook/table-of-contents/): the practical companion,
  with worked SLOs, alerting on burn rate, and NALSD.
- [All Google SRE books](https://sre.google/books/), including *Building Secure and Reliable Systems*.
- Deep dive: the [Observability](../Observability/index.html) course for telemetry pipelines and alert design.

## Software architecture ([Lesson 03](lessons/0003-software-architecture.html))
- **Primary:** [Martin Fowler, *Software Architecture Guide*](https://martinfowler.com/architecture/): what
  architecture is, and why it is about the important stuff.
- [Fowler, *Is High Quality Software Worth the Cost?*](https://martinfowler.com/articles/is-quality-worth-cost.html):
  the economic case for internal quality.
- [Fowler, *Strangler Fig Application*](https://martinfowler.com/bliki/StranglerFigApplication.html): the
  canonical incremental-migration pattern.
- [Richards & Ford, *Fundamentals of Software Architecture*](https://www.amazon.com/dp/1492043451):
  characteristics, styles and fitness functions.
- [Ford et al., *Software Architecture: The Hard Parts*](https://www.amazon.com/dp/1492086894): granularity,
  data ownership, sagas and distributed trade-offs.

## Strategy & vision ([Lesson 04](lessons/0004-engineering-strategy-and-vision.html))
- **Primary:** [StaffEng, *Writing an engineering strategy*](https://staffeng.com/guides/engineering-strategy/):
  the "five design docs → strategy → vision" recipe.
- [Will Larson, *Eng strategies*](https://lethain.com/eng-strategies/): applying Rumelt's kernel to engineering.
- [*How Big Technical Changes Happen at Slack*](https://slack.engineering/how-big-technical-changes-happen-at-slack/):
  a real example of adoption and enforcement.
- [Will Larson, *An Elegant Puzzle*](https://lethain.com/elegant-puzzle/): systems thinking for engineering organisations.

## Technical writing ([Lesson 05](lessons/0005-technical-writing-docs-rfcs-adrs.html))
- **Primary:** [Malte Ubl, *Design Docs at Google*](https://www.industrialempathy.com/posts/design-docs-at-google/):
  anatomy, lifecycle, and when not to write one.
- [ADR home and registry](https://adr.github.io/): definitions, tooling and template options (including MADR).
- [ADR templates collection](https://github.com/joelparkerhenderson/architecture-decision-record): many worked examples.
- [Michael Nygard, *Documenting Architecture Decisions*](https://www.cognitect.com/blog/2011/11/15/documenting-architecture-decisions):
  the original ADR post.
- [The Pragmatic Engineer, *Scaling engineering teams via RFCs*](https://blog.pragmaticengineer.com/scaling-engineering-teams-via-writing-things-down-rfcs/):
  how companies run RFC processes at scale.
- Companion: [Leadership Skills](../leadership-skills/index.html) for executive communication and influence.

## Security & privacy ([Lesson 06](lessons/0006-security-and-privacy.html))
- **Primary:** [OWASP Top 10](https://owasp.org/www-project-top-ten/): the shared vocabulary for web
  application risk (the 2021 edition is used here).
- [OWASP Cheat Sheet Series](https://cheatsheetseries.owasp.org/): concrete guidance on authentication,
  sessions, JWTs, password storage and input validation.
- [Microsoft, *STRIDE threat categories*](https://learn.microsoft.com/en-us/azure/security/develop/threat-modeling-tool-threats):
  threat modelling on data-flow diagrams.

## Cloud infrastructure ([Lesson 07](lessons/0007-cloud-infrastructure.html))
- **Primary:** [AWS Well-Architected Framework](https://aws.amazon.com/architecture/well-architected/): six
  pillars that work as vendor-neutral review prompts.
- [Dan McKinley, *Choose Boring Technology*](https://mcfunley.com/choose-boring-technology): innovation tokens.
- [Intercom, *Run Less Software*](https://www.intercom.com/blog/run-less-software/): why managed and
  commodity services usually win.
- [Amazon Builders' Library](https://aws.amazon.com/builders-library/): timeouts, retries with jitter,
  load shedding and static stability from Amazon practice.
- [The Twelve-Factor App](https://12factor.net/): baseline conventions for cloud-friendly services.

## T-shaped expertise ([Lesson 08](lessons/0008-tshaped-breadth-and-depth.html))
- **Primary:** [StaffEng, *Staff archetypes*](https://staffeng.com/guides/staff-archetypes/): Tech Lead,
  Architect, Solver and Right Hand.
- [Charity Majors, *Useful things to know about engineering levels*](https://charity.wtf/p/useful-things-to-know-about-engineering-levels):
  breadth, depth and how levels really work.
- [System Design Primer](https://github.com/donnemartin/system-design-primer): a broad map for filling breadth gaps.

## Related courses in this workspace
- [System Design & Architecture at Scale](../system-design/index.html): roadmap item A1, a prerequisite for
  Lessons 01, 02 and 07.
- [Observability](../Observability/index.html): a hands-on deep dive behind Lesson 02.
- [Leadership Skills](../leadership-skills/index.html): Part B of the roadmap, influence and communication.
- [Skills to upgrade](../02-skills-to-upgrade.md): the roadmap this course belongs to.

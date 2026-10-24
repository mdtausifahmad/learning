# Coding interview course

Open [index.html](index.html) directly from disk or from the published site. All lesson content, diagrams, execution traces, quizzes, language switches, and the two state experiments are local; opening optional original-problem/source links requires Internet access.

Start with [foundations](foundations.html), then follow patterns 01–20. Each pattern has a retrieval sheet in `reference/`. [GUIDE.html](GUIDE.html) is the full reading edition; `GUIDE.md` is the source reading edition. [RESOURCES.md](RESOURCES.md) separates source links from verified evidence.

## Rebuild and validate

The checked-in HTML needs no build step. For authoring, edit `content/NN-slug.json` using the schema in `AUTHORING.md`, then:

```sh
node coding-interview/scripts/build.mjs
python3 coding-interview/scripts/validate.py --java
python3 coding-interview/scripts/validate.py --kotlin
node coding-interview/scripts/test-ui.mjs
```

The builder uses the `marked` package, either installed normally or located through `CODEX_NODE_MODULES`. Its fallback locates the Codex bundled dependency directory on the authoring machine. Kotlin validation uses `kotlinc`/`kotlin` on PATH, or a compiler classpath supplied with `KOTLIN_COMPILER_CLASSPATH`, `KOTLIN_STDLIB`, and `JAVA`. Compilation writes only to temporary directories. `test-ui.mjs` uses an in-memory DOM harness, not a browser renderer. No test installs or downloads dependencies.

Jekyll serves the generated HTML directly. `_config.yml` excludes the guide/reference Markdown sources that would otherwise target the same HTML destinations, and authoring scripts/content. The guide download is `downloads/coding-interview-guide.txt` to retain an available raw-text download on the published site.

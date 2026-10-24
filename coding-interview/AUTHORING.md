# Coding interview lesson contract

Each pattern owns `content/NN-slug.json`. Do not edit other authors' files. This is author guidance, not an instruction to delegate.

Write plain JSON with these fields:

```json
{
  "id": "01", "slug": "hashing", "title": "Hashing and complements",
  "group": "Linear data", "prerequisites": "Lesson 00", "estimatedMinutes": 90,
  "motivation": "What repeated work this removes; explain from zero.",
  "recognition": ["Concrete structural clue, with reason"],
  "mentalModel": "Several paragraphs of Markdown, basic vocabulary then derivation. At least 350 words across mentalModel, invariant, and reasoning sections.",
  "invariant": "What is true before/after each step and why that proves correctness.",
  "wrongTurns": [{"temptation":"...", "counterexample":"Explicit input, wrong result, explanation", "repair":"..."}],
  "diagram": {"caption":"...", "nodes":["...","...","..."], "edges":[[0,1,"label"],[1,2,"label"]]},
  "trace": [{"state":"Concrete array/table/tree state", "explanation":"Why this transition is safe"}],
  "problems": [{
    "title":"...", "difficulty":"Easy", "statement":"Self-contained original paraphrase; constraints and return contract.",
    "example":"Concrete input, output, why", "baseline":"Naive algorithm with time and space.",
    "reasoning":["Step 1 derives state", "Step 2 derives transition", "Step 3 termination/correctness"],
    "invariant":"Problem-specific correctness argument",
    "java":"static ... method ...", "kotlin":"fun ...",
    "complexity":"Time and space with variables defined, including output/recursion distinction.",
    "edgeCases":["Input => expected output"], "twist":"A precise variation and how state/proof changes.",
    "source":"https://leetcode.com/problems/.../"
  }],
  "practice": [{"title":"...", "difficulty":"Medium", "url":"https://leetcode.com/problems/.../", "hint":"State/invariant hint", "approach":"Solution outline, complexity, and twist"}],
  "quiz": [{"question":"...", "choices":["...","...","..."], "answer":0, "explanation":"..."}],
  "transfer": {"prompt":"Unfamiliar problem without naming the pattern", "hints":["...","..."], "solution":"Derivation and complexity"},
  "review": ["Specific spaced-retrieval prompt"],
  "sources": [{"label":"...", "url":"...", "note":"What this primary source supports, not frequency claims"}]
}
```

Quality: four fully worked problems per pattern, increasing from easy/foundation through medium to hard (a few naturally advanced patterns may begin at medium; say why). At least six additional practice problems with useful solution outlines. Each worked problem has complete Java 17-compatible and Kotlin/JVM functions, with helper types/functions defined in the SAME snippet. No pseudocode or omitted helpers. Java snippets will be wrapped in `class SolutionNNP` with `import java.util.*;`; Kotlin snippets compiled independently with `import java.util.*`. Use static nested Java node classes as needed. Avoid public top-level Java classes. Specify null/empty behavior, input mutation, numeric assumptions; use long/Long for potentially large sums. Do not silently assume ASCII, nonnegative values, sorted input, or bounded arithmetic. Explain baseline → bottleneck → improved state → invariant → trace → bounds. Explain precisely why a tempting alternative fails. Each pattern needs at least two wrong turns, four trace states, two quizzes with explanations, and an unseen transfer challenge. Use diagram nodes/edges for simple offline SVG and trace text for concrete step-through. No company question-frequency claims or guarantees. Links are provenance/practice, never needed to read the lesson. No external assets. JSON strings accept Markdown. Stay within your assigned files.

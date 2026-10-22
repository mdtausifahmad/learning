/* ===========================================================================
   Renders Mermaid diagrams in course HTML pages.

   Markup:  <figure><pre class="mermaid">flowchart LR
              A["Client"] --> B["Service"]</pre><figcaption>…</figcaption></figure>

   Load as a module:  <script type="module" src="../../assets/course/mermaid.js"></script>
   Same renderer and theme as the Markdown pages (_layouts/default.html). If the
   CDN is unreachable the diagram source stays visible as a code block.
   =========================================================================== */
const nodes = document.querySelectorAll("pre.mermaid");

if (nodes.length) {
  try {
    const { default: mermaid } = await import("https://cdn.jsdelivr.net/npm/mermaid@11/dist/mermaid.esm.min.mjs");
    mermaid.initialize({ startOnLoad: false, theme: "neutral", securityLevel: "strict" });
    await mermaid.run({ nodes });
  } catch (err) {
    console.warn("mermaid: diagrams left as source", err);
  }
}

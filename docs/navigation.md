# Learning-page navigation

Every standalone HTML page starts with a keyboard-visible skip link targeting its focusable `main` landmark. A compact top navigation provides breadcrumbs to the library home, primary learning area, course map and section index. Its native “Explore” disclosure contains the section shortcuts; it works without JavaScript. Coding-interview shortcuts also include foundations, mixed practice and sources. The bottom navigation contains the single previous/next pager, section shortcuts and “Back to top”. The Jekyll layout follows the same structure for published Markdown.

Lessons and reference sheets each have their own previous/next sequence, ordered by their existing numbered filenames. Guides and other supporting resources have a separate sequence in stable filename order. The career chapters keep their numbered order and original URLs. A first or last page labels the section boundary and keeps its index links available; indexes and the library home have no artificial previous/next sequence. Existing related-lesson links remain available as related reading or course shortcuts.

## Rebuild and verify

```sh
python3 scripts/build-navigation.py
python3 scripts/check-navigation.py
```

The builder discovers courses from `assets/library-areas.json`, generates `lessons/index.html`, `reference/index.html`, and `materials.html` where applicable, and refreshes the marked navigation blocks. It also writes `_data/navigation.json` for the Jekyll layout. Both `scripts/build-library.py` and `coding-interview/scripts/build.mjs` invoke it after rebuilding their content. Edit the builder rather than its generated blocks or data. The builder also normalizes reading landmarks, removes redundant legacy navigation bars, and preserves specialized lesson/reference links as related reading. Author page-specific links in content rather than adding another site header or sequence pager.

Register a new course in the catalog before building its navigation. Preserve numbered lesson filenames and existing URLs. Publishing exclusions in `_config.yml` are omitted from supporting-resource indexes. Course-authoring sources that have generated HTML versions use those HTML pages in the indexes.

The audit checks targets and HTML anchors in every existing navigation element, as well as every standalone HTML page and both copies of the shared navigation, every navigation target and HTML anchor, sequence boundaries and reciprocal previous/next links. It also checks the Markdown layout integration and metadata. Run course-specific checks for any interactive behavior affected by a change.

## Local and published reading

Navigation is emitted as HTML rather than fetched at runtime. Links between HTML pages work without JavaScript. Local links to Markdown open the source file. `assets/course/links.js` changes those links to their rendered HTML paths on the published site, including nested README indexes. Jekyll's shared layout adds the navigation to rendered Markdown guides, career chapters, labs and records; `relative_url` supports deployment under a project subpath.

Raw Markdown source views do not execute the Jekyll layout. Their rendered navigation is available on the published site or a local Jekyll preview. Structural audits validate generated HTML and layout metadata; they do not establish visual or keyboard behavior in a real browser.

## Shared interaction checks

Run `node scripts/test-course-ui.mjs` after changing shared quizzes or walkthroughs. This dependency-free DOM simulation exercises answer feedback, retry behavior, arrow-key stepping, playback and accessibility state. It does not verify rendered layout or actual assistive-technology announcements. Run `node coding-interview/scripts/test-ui.mjs` for the separate coding-interview controls.

"""Audit shared navigation coverage, local targets, and reciprocal sequences.

This checks static HTML plus Jekyll metadata/templates, without a browser.
Run after build-navigation.py (also invoked by the content builders).
"""
from collections import Counter
from html.parser import HTMLParser
import json
from pathlib import Path
from urllib.parse import unquote, urlsplit

ROOT = Path(__file__).resolve().parents[1]
metadata = json.loads((ROOT / '_data/navigation.json').read_text())
groups = json.loads((ROOT / '_data/navigation-sequences.json').read_text())


class Page(HTMLParser):
    def __init__(self):
        super().__init__(); self.ids = []; self.navs = []; self.active = None; self.depth = 0; self.nav_depth = 0; self.all_links = []
    def handle_starttag(self, tag, attrs):
        a = dict(attrs)
        if 'id' in a: self.ids.append(a['id'])
        if tag == 'nav': self.nav_depth += 1
        if tag == 'a' and self.nav_depth and 'href' in a: self.all_links.append(a['href'])
        if tag == 'nav':
            if self.active is not None: self.depth += 1
            elif 'page-navigation' in a.get('class', '').split():
                self.active = []; self.depth = 1; self.navs.append(self.active)
        if tag == 'a' and self.active is not None: self.active.append(a)
    def handle_endtag(self, tag):
        if tag == 'nav': self.nav_depth -= 1
        if tag == 'nav' and self.active is not None:
            self.depth -= 1
            if not self.depth: self.active = None


pages = {}
for p in ROOT.rglob('*.html'):
    if any(x.startswith('.') or x in ('_layouts', '_includes', '_site', 'node_modules', 'build') for x in p.relative_to(ROOT).parts): continue
    parsed = Page(); parsed.feed(p.read_text()); pages[p.resolve()] = parsed

errors = []
links = 0
all_nav_links = 0
for p, parsed in pages.items():
    key = p.relative_to(ROOT).as_posix()
    for raw in parsed.all_links:
        u = urlsplit(raw)
        if u.scheme or u.netloc: continue
        target = (p.parent / unquote(u.path)).resolve() if u.path else p
        if not target.exists() and target.suffix == '.html': target = target.with_suffix('.md')
        if not target.exists(): errors.append(f'{key}: missing existing navigation target {raw}')
        elif u.fragment and target in pages and unquote(u.fragment) not in pages[target].ids:
            errors.append(f'{key}: missing existing navigation anchor {raw}')
        all_nav_links += 1
    if key not in metadata: errors.append(f'{key}: missing navigation metadata'); continue
    data = metadata[key]
    if len(parsed.navs) != 2: errors.append(f'{key}: expected top and bottom navigation')
    if parsed.ids.count('page-top') != 1: errors.append(f'{key}: expected one page-top anchor')
    expected = [*data['parents'], *data['sections']]
    for nav in parsed.navs:
        resolved = []
        for a in nav:
            u = urlsplit(a['href']); target = (p.parent / unquote(u.path)).resolve() if u.path else p
            resolved.append((target, unquote(u.fragment)))
            if u.scheme or u.netloc: errors.append(f'{key}: shared navigation must stay local')
            if not target.exists(): errors.append(f'{key}: missing target {a["href"]}')
            if u.fragment and target in pages and unquote(u.fragment) not in pages[target].ids:
                errors.append(f'{key}: missing anchor {a["href"]}')
            links += 1
        for entry in expected:
            u = urlsplit(entry['url'])
            if ((ROOT / u.path).resolve(), unquote(u.fragment)) not in resolved:
                errors.append(f'{key}: missing parent/section link {entry["url"]}')
        for relation, field in [('prev', 'previous'), ('next', 'next')]:
            actual = [a for a in nav if a.get('rel') == relation]
            wanted = data.get(field)
            if len(actual) != int(bool(wanted)):
                errors.append(f'{key}: incorrect {field} count')
            elif wanted and (p.parent / actual[0]['href']).resolve() != (ROOT / wanted['url']).resolve():
                errors.append(f'{key}: incorrect {field} target')

seen = Counter()
for group in groups:
    for i, key in enumerate(group):
        seen[key] += 1
        data = metadata[key]
        for field, j in [('previous', i-1), ('next', i+1)]:
            expected = group[j] if 0 <= j < len(group) else None
            actual = data.get(field)
            if (actual['url'] if actual else None) != expected: errors.append(f'{key}: sequence mismatch for {field}')
        for entry in [*data['parents'], *data['sections']]:
            if not (ROOT / urlsplit(entry['url']).path).exists(): errors.append(f'{key}: missing Markdown parent')
if any(n != 1 for n in seen.values()): errors.append('A page appears in multiple sequences')
for key in metadata:
    if not (ROOT / key).exists(): errors.append(f'Stale metadata: {key}')
for p in ROOT.glob('[0-9][0-9]-*.md'):
    if p.name not in seen: errors.append(f'Career chapter lacks sequence: {p.name}')
for p in pages:
    if p.parent.name in ('lessons', 'reference') and p.name != 'index.html' and p.relative_to(ROOT).as_posix() not in seen:
        errors.append(f'Lesson/reference missing sequence: {p}')
layout = (ROOT / '_layouts/default.html').read_text()
for position in ('top', 'bottom'):
    if f'include page-navigation.html position="{position}"' not in layout: errors.append(f'Markdown layout lacks {position} navigation')
include = (ROOT / '_includes/page-navigation.html').read_text()
for token in ('site.data.navigation[page.path]', 'page_nav.parents', 'page_nav.sections', 'page_nav.previous', 'page_nav.next', 'relative_url'):
    if token not in include: errors.append(f'Jekyll include missing {token}')
if errors: raise SystemExit('\n'.join(errors))
print(f'Navigation audit passed: {len(pages)} HTML pages with top/bottom navigation; {len(groups)} reciprocal sequences ({len(seen)} pages); {all_nav_links} total navigation links and anchors ({links} shared); {sum(k.endswith(".md") for k in metadata)} Markdown layout entries.')

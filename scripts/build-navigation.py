"""Build static page navigation, section indexes, and Jekyll navigation data.

Sequences are numeric lesson/reference filenames; supporting materials use a
stable filename order. Existing page URLs and specialized links are preserved.
"""
import html
import fnmatch
from html.parser import HTMLParser
import json
import os
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
START = '<!-- page-navigation:start -->'
END = '<!-- page-navigation:end -->'


class Title(HTMLParser):
    def __init__(self):
        super().__init__(); self.active = False; self.parts = []
    def handle_starttag(self, tag, attrs):
        if tag == 'h1' and not self.parts: self.active = True
    def handle_endtag(self, tag):
        if tag == 'h1': self.active = False
    def handle_data(self, data):
        if self.active: self.parts.append(data)


def title(path):
    text = path.read_text()
    if path.suffix == '.html':
        parser = Title(); parser.feed(text)
        if parser.parts: return ' '.join(''.join(parser.parts).split())
        match = re.search(r'<title>(.*?)</title>', text, re.S | re.I)
        if match: return html.unescape(match[1].split(' · ')[0])
    match = re.search(r'^#\s+(.+)', text, re.M)
    return match[1].replace('`', '') if match else path.stem.replace('-', ' ').title()


def item(path, label=None):
    return {'url': str(path), 'title': label or title(ROOT / path)}


def rel(source, target):
    path, sep, fragment = target.partition('#')
    return os.path.relpath(ROOT / path, (ROOT / source).parent) + (sep + fragment if sep else '')


def anchor(source, entry, label=None, relation=None):
    attrs = f' rel="{relation}"' if relation else ''
    return f'<a href="{html.escape(rel(source, entry["url"]), quote=True)}"{attrs}>{html.escape(label or entry["title"])}</a>'


def render(source, data, position):
    crumbs = ' <span aria-hidden="true">›</span> '.join(anchor(source, x) for x in data['parents'])
    links = ''.join(anchor(source, x) for x in data['sections'])
    pager = ''
    if position == 'bottom' and (data.get('previous') or data.get('next')):
        parts = []
        if data.get('previous'): parts.append(anchor(source, data['previous'], '← Previous: ' + data['previous']['title'], 'prev'))
        else: parts.append('<span>Start of this section</span>')
        if data.get('next'): parts.append(anchor(source, data['next'], 'Next: ' + data['next']['title'] + ' →', 'next'))
        else: parts.append('<span>End of this section</span>')
        pager = '<div class="page-navigation-pager">' + ''.join(parts) + '</div>'
    if position == 'top':
        content = f'<div class="page-navigation-parents">{crumbs}</div><details class="navigation-menu"><summary>Explore</summary><div class="page-navigation-sections">{links}</div></details>'
    else:
        content = pager + f'<div class="page-navigation-sections">{links}<a href="#page-top">Back to top</a></div>'
    return START + f'\n<nav class="page-navigation page-navigation-{position}" aria-label="{position.capitalize()} page navigation">{content}</nav>\n' + END


def strip_nav(text):
    return re.sub(re.escape(START) + r'.*?' + re.escape(END) + r'\n?', '', text, flags=re.S)


def reading_structure(text, source, data):
    """Migrate legacy chrome while keeping specialized teaching links visible.

    This also runs after content generators, so rebuilding a course cannot
    reintroduce duplicate navigation or omit the reading landmark.
    """
    text = strip_nav(text)
    text = re.sub(r'<a\b[^>]*class="(?:skip|skip-link)"[^>]*>.*?</a>\s*', '', text, flags=re.S)
    text = re.sub(r'<nav\b[^>]*class="(?:library-nav|site-nav|top)"[^>]*>.*?</nav>\s*', '', text, flags=re.S)
    destinations = {rel(source, e['url']) for e in [*data['parents'], *data['sections'],
                    *[data[k] for k in ('previous', 'next') if data.get(k)]]}

    def related(match):
        # Retain reference sheets and links outside this page's sequence.
        links = []
        for link in re.finditer(r'<a\b[^>]*href="([^"]+)"[^>]*>.*?</a>', match[0], re.S):
            if html.unescape(link[1]) not in destinations and link[0] not in links:
                links.append(link[0])
        return '<aside class="related-reading" aria-label="Related reading">' + ''.join(links) + '</aside>' if links else ''

    text = re.sub(r'<nav\b[^>]*class="lesson-nav"[^>]*>.*?</nav>', related, text, flags=re.S)
    if re.search(r'<main\b', text):
        def landmark(match):
            attrs = match[1]
            if not re.search(r'\bid=', attrs): attrs += ' id="main"'
            if not re.search(r'\btabindex=', attrs): attrs += ' tabindex="-1"'
            return '<main' + attrs + '>'
        text = re.sub(r'<main\b([^>]*)>', landmark, text, count=1)
    else:
        text = re.sub(r'(<body\b[^>]*>)\s*', r'\1\n<main id="main" tabindex="-1">\n', text, count=1)
        text = text.replace('</body>', '</main>\n</body>', 1)
    return text


def build():
    areas = json.loads((ROOT / 'assets/library-areas.json').read_text())
    courses = {str(Path(e['url']).parent): (area, e) for area in areas for e in area['entries']}
    home = item('index.html', 'Library home')
    all_areas = item('index.html#topics', 'All learning areas')
    metadata = {}
    groups = []
    index_count = 0
    exclusions = [line.strip()[2:] for line in (ROOT / '_config.yml').read_text().split('exclude:', 1)[1].splitlines() if line.strip().startswith('- ')]
    for course, (area, entry) in courses.items():
        root = ROOT / course
        course_item = item(entry['url'], entry['title'])
        parents = [home, item('index.html#' + area['id'], area['title']), course_item]
        sections = []
        section_groups = []
        for folder, label in [('lessons', 'Lessons'), ('reference', 'Reference sheets')]:
            files = sorted(p.relative_to(ROOT).as_posix() for p in (root / folder).glob('*.html') if p.name != 'index.html')
            if files:
                index = item(f'{course}/{folder}/index.html', label)
                sections.append(index); section_groups.append((index, files))
        # Include every supporting HTML/Markdown page, even labs and learning records.
        # Generated Markdown duplicates defer to their existing HTML counterparts.
        materials = []
        for p in sorted(root.rglob('*')):
            if p.suffix not in ('.html', '.md') or any(x.startswith('.') or x in ('build', 'node_modules') for x in p.relative_to(root).parts): continue
            local = p.relative_to(root)
            if any(fnmatch.fnmatch(p.relative_to(ROOT).as_posix(), rule) for rule in exclusions): continue
            if local.parts[0] in ('lessons', 'reference') or p == root / 'index.html' or p == root / 'materials.html': continue
            if p.suffix == '.md' and p.with_suffix('.html').exists(): continue
            if course == 'coding-interview' and p.name in ('FOUNDATIONS.md', 'PRACTICE.md', 'RESOURCES.md'): continue
            materials.append(p.relative_to(ROOT).as_posix())
        if course == 'career-path':
            materials = [p.name for p in sorted(ROOT.glob('[0-9][0-9]-*.md'))] + materials
        if materials:
            index = item(f'{course}/materials.html', 'Guides and resources')
            sections.append(index); section_groups.append((index, materials))
        metadata[entry['url']] = {'title': entry['title'], 'parents': parents[:-1], 'sections': sections}
        for index, files in section_groups:
            rows = ''.join(f'<li>{anchor(index["url"], item(f))}</li>\n' for f in files)
            body = f'''<!doctype html>
<html lang="en"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>{html.escape(index['title'])} · {html.escape(entry['title'])}</title><link rel="stylesheet" href="{rel(index['url'], 'assets/site.css')}"></head><body><main class="section-index"><h1>{html.escape(index['title'])}</h1><p>{html.escape(entry['title'])}. Choose a page below or follow the previous and next links in this section.</p><ol>{rows}</ol></main></body></html>\n'''
            (ROOT / index['url']).write_text(body)
            index_count += 1
            metadata[index['url']] = {'title': index['title'], 'parents': parents, 'sections': sections}
            groups.append(files)
            for i, f in enumerate(files):
                metadata[f] = {'title': title(ROOT / f), 'parents': parents + [index], 'sections': sections,
                               'previous': item(files[i-1]) if i else None,
                               'next': item(files[i+1]) if i+1 < len(files) else None}
    for p in sorted(ROOT.rglob('*')):
        if p.suffix not in ('.html', '.md') or any(x.startswith('.') or x in ('_layouts', '_includes', '_site', 'node_modules', 'build') for x in p.relative_to(ROOT).parts): continue
        key = p.relative_to(ROOT).as_posix()
        if key not in metadata:
            course = next((c for c in courses if key.startswith(c + '/')), None)
            if course:
                base = metadata[courses[course][1]['url']]
                metadata[key] = {'title': title(p), 'parents': base['parents'] + [item(courses[course][1]['url'], courses[course][1]['title'])], 'sections': base['sections']}
            else:
                metadata[key] = {'title': title(p), 'parents': [home], 'sections': [all_areas]}
    for source, data in metadata.items():
        if source.startswith('coding-interview/'):
            data['sections'] = [item('coding-interview/foundations.html', 'Foundations'),
                                item('coding-interview/practice.html', 'Mixed practice'),
                                *data['sections'], item('coding-interview/research.html', 'Sources')]
        elif len(data['parents']) == 1:
            data['sections'] = [all_areas, item('index.html#learning-paths', 'Learning paths'),
                                item('career-path/index.html', 'Career development')]
        path = ROOT / source
        if path.suffix != '.html': continue
        text = path.read_text()
        if 'assets/course/links.js' not in text:
            text = text.replace('</body>', f'<script src="{rel(source, "assets/course/links.js")}" defer></script>\n</body>')
        text = reading_structure(text, source, data)
        main_id = re.search(r'<main\b[^>]*\bid="([^"]+)"', text)[1]
        opening = f'\n<a class="skip-link" href="#{main_id}">Skip to content</a>\n' + render(source, data, 'top') + '\n'
        text = re.sub(r'(<body\b[^>]*>)\s*', lambda m: m[1] + opening, text, count=1, flags=re.I)
        text = re.sub(r'\s*</body>', '\n' + render(source, data, 'bottom') + '\n</body>', text, count=1, flags=re.I)
        if 'id="page-top"' not in text:
            text = re.sub(r'<body([^>]*)>', r'<body\1 id="page-top">', text, count=1, flags=re.I)
        css = rel(source, 'assets/navigation.css')
        if 'assets/navigation.css' not in text:
            text = text.replace('</head>', f'<link rel="stylesheet" href="{css}">\n</head>')
        path.write_text(text)
    (ROOT / '_data').mkdir(exist_ok=True)
    (ROOT / '_data/navigation.json').write_text(json.dumps(metadata, indent=2, ensure_ascii=False) + '\n')
    (ROOT / '_data/navigation-sequences.json').write_text(json.dumps(groups, indent=2) + '\n')
    print(f'Navigation built: {sum(p.endswith(".html") for p in metadata)} HTML pages, {sum(p.endswith(".md") for p in metadata)} Markdown source entries, {index_count} section indexes.')


if __name__ == '__main__':
    build()

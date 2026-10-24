"""Rebuild the ten-area directory in index.html and README.md.

Edit assets/library-areas.json, then run python3 scripts/build-library.py.
The generated pages need no runtime dependency or network connection.
"""
import html
import json
from pathlib import Path
from urllib.parse import urlsplit

ROOT = Path(__file__).resolve().parents[1]
START = '<!-- library-areas:start -->'
END = '<!-- library-areas:end -->'


def esc(value):
    return html.escape(str(value), quote=True)


def link(item):
    fallback = f' data-md="{esc(item["md"])}"' if item.get('md') else ''
    return f'<a href="{esc(item["url"])}"{fallback}>{esc(item["title"])}</a>'


def markdown_link(item):
    return f'[{item["title"]}]({item.get("md", item["url"])})'


def check_target(item):
    url = urlsplit(item['url'])
    assert not url.scheme and not url.netloc, 'Catalog links must be local.'
    target = ROOT / url.path
    if not target.exists() and target.suffix == '.html':
        target = target.with_suffix('.md')
    assert target.exists(), f'Missing catalog target: {item["url"]}'
    if item.get('md'):
        assert (ROOT / item['md']).exists(), f'Missing fallback: {item["md"]}'


def replace_region(file, content):
    text = file.read_text()
    assert text.count(START) == text.count(END) == 1, f'Expected one directory region in {file}'
    before, rest = text.split(START)
    _, after = rest.split(END)
    file.write_text(before + START + '\n' + content + '\n' + END + after)


areas = json.loads((ROOT / 'assets/library-areas.json').read_text())
assert len(areas) == 10, 'Keep the agreed ten broad areas; add topics inside them.'
ids = [identifier for area in areas for identifier in [area['id'], *area['aliases']]]
assert len(ids) == len(set(ids)), 'Duplicate area/alias anchor.'
primary_urls = [item['url'] for area in areas for item in area['entries']]
assert len(primary_urls) == len(set(primary_urls)), 'Give each course or path one primary home.'
for area in areas:
    for item in [*area['entries'], *area['related']]:
        check_target(item)
        for child in item.get('links', []):
            check_target(child)

output = ['<section id="topics" aria-labelledby="topics-heading">',
          '<h2 id="topics-heading">Browse by area</h2>',
          '<p>These ten areas organize the library as it grows. Choose any area; numbering is for navigation, not study order. Topic lists describe each area’s scope; linked courses and lessons are the material available today.</p>',
          '<nav class="topic-nav" aria-label="Ten learning areas"><ol class="area-directory">']
for area in areas:
    output.append(f'<li><a href="#{esc(area["id"])}">{esc(area["title"])}</a></li>')
output.append('</ol></nav>')
for number, area in enumerate(areas, 1):
    output.append(f'<section class="subject" id="{esc(area["id"])}" aria-labelledby="{esc(area["id"])}-heading">')
    for alias in area['aliases']:
        output.append(f'<span id="{esc(alias)}" class="area-alias" aria-hidden="true"></span>')
    output.append(f'<h3 id="{esc(area["id"])}-heading">{number}. {esc(area["title"])}</h3>')
    output.append(f'<p class="subject-intro">{esc(area["description"])}.</p>')
    output.append('<details class="area-scope"><summary>Topics in this area, including future additions</summary><p>' + esc(', '.join(area['topics'])) + '.</p></details>')
    if not area['entries']:
        output.append('<p class="coverage-note">No dedicated course yet. Start with the related lessons below.</p>')
    for item in area['entries']:
        output.append(f'<article class="library-entry"><h4>{link(item)}</h4><p>{esc(item["description"])}</p>')
        if item.get('meta'):
            output.append(f'<p class="meta">{esc(item["meta"])}</p>')
        if item.get('links'):
            output.append('<p class="entry-links">' + ''.join(link(child) for child in item['links']) + '</p>')
        output.append('</article>')
    if area['related']:
        output.append('<div class="related-learning"><h4>Related learning already available</h4><ul>')
        for item in area['related']:
            note = ' — ' + esc(item['note']) if item.get('note') else ''
            output.append(f'<li>{link(item)}{note}</li>')
        output.append('</ul></div>')
    output.append('</section>')
output.append('</section>')

table = ['## Browse by area', '',
         'The ten areas below are the library’s permanent broad classification. Courses have one primary home; focused lessons are cross-linked where they help. Areas without a dedicated course still show relevant existing material.', '',
         '| Area | What belongs here | Available learning |', '|---|---|---|']
for number, area in enumerate(areas, 1):
    primary = '; '.join(markdown_link(item) for item in area['entries'])
    related = '; '.join(markdown_link(item) for item in area['related'])
    learning = primary
    if related:
        learning += ('<br>Related lessons: ' if learning else 'No dedicated course yet. Related: ') + related
    table.append(f'| {number}. {area["title"]} | {area["description"]} | {learning} |')
table += ['', 'Example topics for all ten areas are listed on the [library home page](index.html#topics). Those scope lists include future additions; links identify available material.']

replace_region(ROOT / 'index.html', '\n'.join(output))
replace_region(ROOT / 'README.md', '\n'.join(table))
print(f'Built {len(areas)} areas, {len(primary_urls)} primary course/path entries and {sum(len(a["related"]) for a in areas)} related-learning links.')

# Refresh cross-course navigation after updating the catalog.
import runpy
runpy.run_path(str(ROOT / "scripts/build-navigation.py"), run_name="__main__")

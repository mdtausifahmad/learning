"""Validate authored contracts, generated local links, and executable JVM examples.

Usage: python3 coding-interview/scripts/validate.py [--java] [--kotlin]
Kotlin: uses kotlinc on PATH, or KOTLIN_COMPILER_CLASSPATH and KOTLIN_STDLIB.
JAVA may point at a compatible Java executable (Java 17+).
All compilation outputs are temporary; no build artifacts enter the course.
"""
import argparse
import html.parser
import json
import os
from pathlib import Path
import shutil
import subprocess
import tempfile
from urllib.parse import unquote, urlsplit

ROOT = Path(__file__).resolve().parents[1]
parser = argparse.ArgumentParser()
parser.add_argument('--java', action='store_true')
parser.add_argument('--kotlin', action='store_true')
args = parser.parse_args()
lessons = [json.loads(p.read_text()) for p in sorted((ROOT/'content').glob('[0-9][0-9]-*.json'))]
assert len(lessons) == 20, f'Expected 20 patterns, got {len(lessons)}'
assert [l['id'] for l in lessons] == [f'{n:02}' for n in range(1,21)]
for l in lessons:
    name=l['id']
    assert len(l['problems']) >= 4, name
    assert len(l['practice']) >= 6, name
    assert len(l['trace']) >= 4 and len(l['wrongTurns']) >= 2 and len(l['quiz']) >= 2, name
    assert len((l['mentalModel']+' '+l['invariant']).split()) >= 350, f'Shallow model: {name}'
    for q in l['quiz']:
        assert 0 <= q['answer'] < len(q['choices']) and q['explanation'], name
    for p in l['problems']:
        for field in ['title','statement','example','baseline','reasoning','invariant','java','kotlin','complexity','edgeCases','twist','source','testJava','testKotlin']:
            assert p.get(field), (name,p['title'],field)
        assert len(p['reasoning']) >= 3

class Links(html.parser.HTMLParser):
    def __init__(self):
        super().__init__(); self.links=[]; self.ids=set()
    def handle_starttag(self,tag,attrs):
        a=dict(attrs)
        if 'id' in a: self.ids.add(a['id'])
        for k in ('href','src'):
            if k in a: self.links.append(a[k])

pages={}
for file in ROOT.rglob('*.html'):
    parsed=Links(); parsed.feed(file.read_text()); pages[file.resolve()]=parsed
broken=[]
for file,parsed in pages.items():
    for raw in parsed.links:
        u=urlsplit(raw)
        if u.scheme or u.netloc: continue
        target=(file.parent/unquote(u.path)).resolve() if u.path else file
        if not target.exists(): broken.append(f'{file.relative_to(ROOT)}: missing {raw}')
        elif u.fragment and target in pages and unquote(u.fragment) not in pages[target].ids:
            broken.append(f'{file.relative_to(ROOT)}: missing anchor {raw}')
assert not broken, '\n'.join(broken)
problems=[(l,i,p) for l in lessons for i,p in enumerate(l['problems'],1)]
print(f'Contracts and local links passed: {len(lessons)} patterns, {len(problems)} worked problems, {sum(len(l["practice"]) for l in lessons)} practice variations, {len(pages)} HTML pages.')

def run(cmd):
    result=subprocess.run(cmd,text=True,capture_output=True)
    if result.returncode:
        raise RuntimeError(' '.join(map(str,cmd[:5]))+'\n'+result.stdout+'\n'+result.stderr)
    return result

java=os.environ.get('JAVA','java')
with tempfile.TemporaryDirectory(prefix='coding-course-') as tmp:
    tmp=Path(tmp); classes=tmp/'classes'; classes.mkdir()
    if args.java:
        files=[]
        for l,i,p in problems:
            classname=f'Solution{l["id"]}P{i}'
            file=tmp/(classname+'.java')
            file.write_text('import java.util.*;\nclass '+classname+' {\n'+p['java']+'\npublic static void main(String[] args) {\n'+p['testJava']+'\n}\n}\n')
            files.append(str(file))
        run([os.environ.get('JAVAC','javac'),'--release','17','-d',str(classes),*files])
        for l,i,p in problems: run([java,'-cp',str(classes),f'Solution{l["id"]}P{i}'])
        print(f'Java: {len(files)} snippets compiled for release 17 and passed executable checks.')
    if args.kotlin:
        files=[]
        for l,i,p in problems:
            package=f'p{l["id"]}p{i}'
            file=tmp/f'Example{l["id"]}P{i}.kt'
            file.write_text(f'package {package}\nimport java.util.*\n'+p['kotlin']+'\nfun main() {\n'+p['testKotlin']+'\n}\n')
            files.append(str(file))
        stdlib=os.environ.get('KOTLIN_STDLIB','')
        compiler=shutil.which('kotlinc')
        if compiler:
            run([compiler,'-jvm-target','17','-d',str(classes),*files])
            # Use kotlin runner if installed; otherwise set KOTLIN_STDLIB explicitly.
            runner=shutil.which('kotlin')
        else:
            cp=os.environ.get('KOTLIN_COMPILER_CLASSPATH')
            if not cp or not stdlib: raise RuntimeError('Set KOTLIN_COMPILER_CLASSPATH and KOTLIN_STDLIB, or install kotlinc.')
            run([java,'-cp',cp,'org.jetbrains.kotlin.cli.jvm.K2JVMCompiler','-no-stdlib','-no-reflect','-classpath',stdlib,'-jvm-target','17','-d',str(classes),*files])
            runner=None
        for l,i,p in problems:
            main=f'p{l["id"]}p{i}.Example{l["id"]}P{i}Kt'
            if runner: run([runner,'-classpath',str(classes),main])
            else: run([java,'-cp',str(classes)+os.pathsep+stdlib,main])
        print(f'Kotlin: {len(files)} snippets compiled for JVM 17 and passed executable checks.')

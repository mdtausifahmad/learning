// Dependency-free DOM simulation. Tests the shipped script against generated HTML;
// does not render pixels, measure layout, launch a browser, or make network requests.
import fs from 'node:fs';
import path from 'node:path';
import vm from 'node:vm';
import assert from 'node:assert/strict';
import {fileURLToPath} from 'node:url';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const script=fs.readFileSync(path.join(root,'assets/course.js'),'utf8');
const decode=s=>s.replace(/&(amp|lt|gt|quot|#39);/g,(_,k)=>({amp:'&',lt:'<',gt:'>',quot:'"','#39':"'"}[k]));
class Element{
  constructor(tag='root',attrs={}){this.tagName=tag;this.attrs=attrs;this.children=[];this.parentElement=null;this.events={};this.hidden=false;this.disabled='disabled'in attrs;this.checked='checked'in attrs;this.value=attrs.value||'';this.dataset={};for(const[k,v]of Object.entries(attrs))if(k.startsWith('data-'))this.dataset[k.slice(5).replace(/-([a-z])/g,(_,c)=>c.toUpperCase())]=v;}
  get className(){return this.attrs.class||'';}set className(v){this.attrs.class=v;}
  get classList(){return {add:c=>this.className=[...new Set([...this.className.split(' '),c])].join(' '),remove:c=>this.className=this.className.split(' ').filter(x=>x!==c).join(' ')};}
  get textContent(){return this.children.map(c=>typeof c==='string'?c:c.textContent).join('');}
  set textContent(v){this.children=[String(v)];}
  set innerHTML(v){this.children=[];const fragment=parse(v);this.append(...fragment.children);}
  append(...children){for(const c of children){if(typeof c!=='string')c.parentElement=this;this.children.push(c);}}
  replaceChildren(...children){this.children=[];this.append(...children);}
  setAttribute(k,v){this.attrs[k]=String(v);}
  getAttribute(k){return this.attrs[k];}
  addEventListener(name,fn){(this.events[name]||=[]).push(fn);}
  emit(name){for(const fn of this.events[name]||[])fn({target:this});}
  click(){if(!this.disabled)this.emit('click');}
  matches(sel){
    if(sel==='input:checked')return this.tagName==='input'&&this.checked;
    if(sel.startsWith('#'))return this.attrs.id===sel.slice(1);
    if(sel.startsWith('.'))return this.className.split(' ').includes(sel.slice(1));
    const attr=sel.match(/^\[([^=\]]+)(?:="([^"]*)")?\]$/);
    if(attr)return attr[1]in this.attrs&&(attr[2]===undefined||this.attrs[attr[1]]===attr[2]);
    return this.tagName===sel;
  }
  querySelectorAll(sel){const out=[];for(const child of this.children)if(typeof child!=='string'){if(child.matches(sel))out.push(child);out.push(...child.querySelectorAll(sel));}return out;}
  querySelector(sel){return this.querySelectorAll(sel)[0]||null;}
  closest(sel){return this.matches(sel)?this:this.parentElement?.closest(sel)||null;}
}
function parse(html){const root=new Element(),stack=[root];const voids=new Set(['meta','link','input','br','hr','img','path','rect']);for(const token of html.match(/<!--[\s\S]*?-->|<[^>]+>|[^<]+/g)||[]){if(token.startsWith('<!'))continue;if(token.startsWith('</')){const tag=token.slice(2,-1).trim();const at=stack.findLastIndex(n=>n.tagName===tag);if(at>0)stack.length=at;continue;}if(token.startsWith('<')){const m=token.match(/^<([\w:-]+)/);if(!m)continue;const attrs={};const text=token.slice(m[0].length,-1);for(const a of text.matchAll(/([\w:-]+)(?:\s*=\s*(?:"([^"]*)"|'([^']*)'|([^\s>]+)))?/g))attrs[a[1]]=decode(a[2]??a[3]??a[4]??'');const el=new Element(m[1],attrs);stack.at(-1).append(el);if(!voids.has(el.tagName)&&!token.endsWith('/>'))stack.push(el);}else stack.at(-1).append(decode(token));}return root;}
const storage=new Map();
function page(file,{deniedStorage=false}={}){const document=parse(fs.readFileSync(path.join(root,file),'utf8'));document.createElement=t=>new Element(t);const localStorage={getItem:k=>{if(deniedStorage)throw Error('Unavailable');return storage.get(k)??null;},setItem:(k,v)=>{if(deniedStorage)throw Error('Unavailable');storage.set(k,v);}};vm.runInNewContext(script,{document,localStorage,console});return document;}
let checks=0;
function ok(test,msg){assert.ok(test,msg);checks++;}
const home=page('index.html');
ok(home.querySelectorAll('.pattern-row').length===20,'20 pattern rows');
const search=home.querySelector('#pattern-search');search.value='nonexistent-pattern-xyz';search.emit('input');ok(home.querySelector('#search-count').textContent==='0 patterns shown','search empty result');search.value='prefix';search.emit('input');ok(home.querySelectorAll('.pattern-row').some(x=>!x.hidden),'search clue');
let lessons=0;
for(const file of fs.readdirSync(path.join(root,'lessons')).filter(f=>f.endsWith('.html')&&f!=='index.html')){
  const doc=page(`lessons/${file}`);lessons++;
  const trace=doc.querySelector('[data-trace]'),prev=trace.querySelector('[data-step="-1"]'),next=trace.querySelector('[data-step="1"]');
  const states=JSON.parse(trace.querySelector('script').textContent);ok(prev.disabled,'first trace prev disabled');
  for(let i=1;i<states.length;i++)next.click();ok(next.disabled&&trace.querySelector('.trace-state').textContent===states.at(-1).state,'trace final state');
  trace.querySelector('[data-reset]').click();ok(trace.querySelector('.trace-state').textContent===states[0].state,'trace reset');
  for(const quiz of doc.querySelectorAll('.quiz')){
    quiz.querySelector('[data-check]').click();ok(quiz.querySelector('.feedback').textContent==='Choose an answer first.','no silent quiz submission');
    const inputs=quiz.querySelectorAll('input');inputs[(Number(quiz.dataset.answer)+1)%inputs.length].checked=true;quiz.querySelector('[data-check]').click();ok(quiz.querySelector('.feedback').className.includes('incorrect'),'incorrect reasoning feedback');inputs.forEach(x=>x.checked=false);inputs[Number(quiz.dataset.answer)].checked=true;quiz.querySelector('[data-check]').click();ok(quiz.querySelector('.feedback').className.includes('correct')&&!quiz.querySelector('.feedback').className.includes('incorrect'),'correct reasoning feedback');
  }
  doc.querySelector('[data-lang="kotlin"]').click();ok(doc.querySelectorAll('[data-code="java"]').every(x=>x.hidden)&&doc.querySelectorAll('[data-code="kotlin"]').every(x=>!x.hidden),'language switching');
  doc.querySelector('[data-lang="both"]').click();ok(doc.querySelectorAll('[data-code]').every(x=>!x.hidden),'show both languages');
  const box=doc.querySelector('[data-progress]');box.checked=true;box.emit('change');ok(page(`lessons/${file}`).querySelector('[data-progress]').checked,'persist self report');
}
ok(lessons===20,'20 lesson interactions tested');
page('index.html',{deniedStorage:true});checks++;
storage.set('coding-interview-v1','{bad-json');page('index.html');checks++;storage.clear();
const lab=page('practice.html');
ok(lab.querySelector('#window-output').textContent.includes('Answer: 3'),'window example');
lab.querySelector('#window-k').value='0';lab.querySelector('#window-run').click();ok(lab.querySelector('#window-output').textContent.includes('Answer: 0'),'zero distinct window');
lab.querySelector('#window-values').value='';lab.querySelector('#window-run').click();ok(lab.querySelector('#window-output').textContent.includes('Answer: 0'),'empty window');
lab.querySelector('#window-values').value='1,,2';lab.querySelector('#window-run').click();ok(lab.querySelector('#window-output').className.includes('lab-error'),'malformed integer list');
ok(lab.querySelector('#binary-output').textContent.includes('Boundary index = 1'),'lower bound duplicates');
lab.querySelector('#binary-values').value='';lab.querySelector('#binary-run').click();ok(lab.querySelector('#binary-output').textContent.includes('Boundary index = 0'),'empty lower bound');
lab.querySelector('#binary-values').value='2,1';lab.querySelector('#binary-run').click();ok(lab.querySelector('#binary-output').textContent.includes('must be sorted'),'reject unsorted lower bound');
lab.querySelector('#unseen').click();ok(lab.querySelector('#unseen-output').querySelectorAll('details').length>=2,'embedded transfer catalog works without fetch');
console.log(`DOM simulation passed: ${checks} checks across ${lessons} lessons, search, storage and experiments. Pixel/layout verification is not included.`);

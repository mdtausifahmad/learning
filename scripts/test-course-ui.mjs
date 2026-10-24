// Dependency-free DOM simulation of shared quiz and walkthrough behavior.
// This verifies events and accessibility state, not browser layout or announcements.
import fs from 'node:fs';
import path from 'node:path';
import vm from 'node:vm';
import assert from 'node:assert/strict';
import {fileURLToPath} from 'node:url';
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const decode=s=>s.replace(/&(amp|lt|gt|quot|#39);/g,(_,k)=>({amp:'&',lt:'<',gt:'>',quot:'"','#39':"'"}[k]));
class Element{
  constructor(tag='root',attrs={}){this.tagName=tag;this.attrs=attrs;this.children=[];this.parentElement=null;this.events={};this.hidden=false;this.disabled='disabled'in attrs;this.checked='checked'in attrs;this.value=attrs.value||'';this.style={};this.dataset={};for(const[k,v]of Object.entries(attrs))if(k.startsWith('data-'))this.dataset[k.slice(5).replace(/-([a-z])/g,(_,c)=>c.toUpperCase())]=v;}
  get className(){return this.attrs.class||'';}set className(v){this.attrs.class=v;}
  get classList(){return {add:c=>this.className=[...new Set([...this.className.split(' '),c])].join(' '),remove:c=>this.className=this.className.split(' ').filter(x=>x!==c).join(' '),toggle:(c,on)=>{const yes=on??!this.className.split(' ').includes(c);this.className=this.className.split(' ').filter(x=>x!==c).concat(yes?[c]:[]).join(' ');}};}
  get textContent(){return this.children.map(c=>typeof c==='string'?c:c.textContent).join('');}
  set textContent(v){this.children=[String(v)];}
  set innerHTML(v){this.children=[];const fragment=parse(v);this.append(...fragment.children);}
  append(...children){for(const c of children){if(typeof c!=='string')c.parentElement=this;this.children.push(c);}}
  appendChild(child){this.append(child);return child;}
  replaceChildren(...children){this.children=[];this.append(...children);}
  setAttribute(k,v){this.attrs[k]=String(v);}
  getAttribute(k){return this.attrs[k]??null;}
  hasAttribute(k){return k in this.attrs;}
  addEventListener(name,fn){(this.events[name]||=[]).push(fn);}
  emit(name,event={}){for(const fn of this.events[name]||[])fn({target:this,...event});}
  click(){if(!this.disabled)this.emit('click');}
  matches(sel){
    if(sel==='input:checked')return this.tagName==='input'&&this.checked;
    const compound=sel.match(/^(\.[\w-]+)(\[[^\]]+\])$/);if(compound)return this.matches(compound[1])&&this.matches(compound[2]);
    if(sel.startsWith('#'))return this.attrs.id===sel.slice(1);
    if(sel.startsWith('.'))return this.className.split(' ').includes(sel.slice(1));
    const attr=sel.match(/^\[([^=\]]+)(?:="([^"]*)")?\]$/);
    if(attr)return attr[1]in this.attrs&&(attr[2]===undefined||this.attrs[attr[1]]===attr[2]);
    return this.tagName===sel;
  }
  querySelectorAll(sel){if(sel.includes(' ')){const [parent,...rest]=sel.split(' ');return [...new Set(this.querySelectorAll(parent).flatMap(node=>node.querySelectorAll(rest.join(' '))))];}const out=[];for(const child of this.children)if(typeof child!=='string'){if(child.matches(sel))out.push(child);out.push(...child.querySelectorAll(sel));}return out;}
  querySelector(sel){return this.querySelectorAll(sel)[0]||null;}
  closest(sel){return this.matches(sel)?this:this.parentElement?.closest(sel)||null;}
}
function parse(html){const root=new Element(),stack=[root];const voids=new Set(['meta','link','input','br','hr','img','path','rect']);for(const token of html.match(/<script\b[^>]*>[\s\S]*?<\/script>|<!--[\s\S]*?-->|<[^>]+>|[^<]+/g)||[]){if(token.startsWith('<script')){const m=token.match(/^<script([^>]*)>([\s\S]*)<\/script>$/);const attrs={};for(const a of m[1].matchAll(/([\w:-]+)="([^"]*)"/g))attrs[a[1]]=a[2];const el=new Element('script',attrs);el.append(m[2]);stack.at(-1).append(el);continue;}if(token.startsWith('<!'))continue;if(token.startsWith('</')){const tag=token.slice(2,-1).trim();const at=stack.findLastIndex(n=>n.tagName===tag);if(at>0)stack.length=at;continue;}if(token.startsWith('<')){const m=token.match(/^<([\w:-]+)/);if(!m)continue;const attrs={};const text=token.slice(m[0].length,-1);for(const a of text.matchAll(/([\w:-]+)(?:\s*=\s*(?:"([^"]*)"|'([^']*)'|([^\s>]+)))?/g))attrs[a[1]]=decode(a[2]??a[3]??a[4]??'');const el=new Element(m[1],attrs);stack.at(-1).append(el);if(!voids.has(el.tagName)&&!token.endsWith('/>'))stack.push(el);}else stack.at(-1).append(decode(token));}return root;}

let checks=0;
function ok(value,message){assert.ok(value,message);checks++;}
function page(file,script){
  const document=parse(fs.readFileSync(path.join(root,file),'utf8'));
  document.createElement=t=>new Element(t);
  document.readyState='complete';
  const timers=new Map();let id=0;
  vm.runInNewContext(fs.readFileSync(path.join(root,script),'utf8'),{
    document,console,setInterval:fn=>{timers.set(++id,fn);return id;},clearInterval:n=>timers.delete(n)
  });
  return {document,timers};
}
const {document:quizDoc}=page('event-sourcing/lessons/0001-state-vs-history-mental-model.html','assets/course/quiz.js');
for(const quiz of quizDoc.querySelectorAll('[data-quiz]')){
  const buttons=quiz.querySelectorAll('button'),feedback=quiz.querySelector('.quiz-explain');
  ok(feedback.getAttribute('aria-live')==='polite'&&!feedback.hidden,'live feedback exists before answering');
  ok(quiz.getAttribute('aria-labelledby')===quiz.querySelector('.quiz-q').id,'question names the quiz group');
  const wrong=buttons.find(b=>b.getAttribute('data-correct')==='false');wrong.click();
  ok(feedback.textContent.includes('Not quite.'),'incorrect answer explains the result');
  ok(buttons.every(b=>!b.disabled&&b.getAttribute('aria-disabled')==='true'),'answered choices preserve focus and expose disabled state');
  const text=feedback.textContent;buttons.find(b=>b.getAttribute('data-correct')==='true').click();
  ok(feedback.textContent===text,'disabled choices cannot change an answer');
}
// Exercise the opt-in retry contract independently of the default one-attempt quizzes.
const retryDoc=parse(fs.readFileSync(path.join(root,'event-sourcing/lessons/0001-state-vs-history-mental-model.html'),'utf8'));
retryDoc.readyState='complete';const retry=retryDoc.querySelector('[data-quiz]');retry.setAttribute('data-retry','');
vm.runInNewContext(fs.readFileSync(path.join(root,'assets/course/quiz.js'),'utf8'),{document:retryDoc});
const retryChoices=retry.querySelectorAll('button');retryChoices.find(b=>b.getAttribute('data-correct')==='false').click();
ok(retryChoices.filter(b=>b.getAttribute('aria-disabled')==='true').length===1,'retry disables only the attempted incorrect choice');
retryChoices.find(b=>b.getAttribute('data-correct')==='true').click();
ok(retry.querySelector('.quiz-explain').textContent.includes('Correct.'),'retry can reach the correct explanation');

const {document:animDoc,timers:animTimers}=page('event-sourcing/lessons/0001-state-vs-history-mental-model.html','assets/course/anim.js');
for(const scene of animDoc.querySelectorAll('[data-anim]')){
  const prev=scene.querySelector('[data-prev]'),next=scene.querySelector('[data-next]'),play=scene.querySelector('[data-play]');
  ok(prev.disabled,'walkthrough begins at the first step');next.click();
  ok(scene.querySelector('[data-count]').textContent.startsWith('2 /'),'next changes the step');
  ok(scene.querySelector('.anim-caps').getAttribute('aria-live')==='polite','caption changes are announced');
  const dots=scene.querySelectorAll('.anim-dot');ok(dots[1].getAttribute('aria-current')==='step','current step is exposed');
  scene.emit('keydown',{key:'ArrowLeft',preventDefault(){}});ok(prev.disabled,'left arrow returns to the first step');
  play.click();ok(play.getAttribute('aria-pressed')==='true'&&animTimers.size===1,'play starts only on request');
  play.click();ok(play.getAttribute('aria-pressed')==='false'&&animTimers.size===0,'pause stops playback');
  dots.at(-1).click();ok(next.disabled,'last step disables next');scene.querySelector('[data-replay]').click();ok(prev.disabled,'replay restores first step');
}
const {document:flowDoc,timers:flowTimers}=page('async-messaging/lessons/0019-debezium-postgresql-cdc-atlas.html','assets/course/stepflow.js');
for(const scene of flowDoc.querySelectorAll('.stepflow')){
  const prev=scene.querySelector('.sf-prev'),next=scene.querySelector('.sf-next'),play=scene.querySelector('.sf-play');
  ok(prev.disabled,'stepflow begins at the first step');
  scene.emit('keydown',{key:'ArrowRight',preventDefault(){}});ok(scene.querySelector('.sf-counter').textContent.startsWith('Step 2'),'right arrow changes stepflow state');
  ok(scene.querySelector('.sf-caption').getAttribute('aria-atomic')==='true','stepflow announces the complete explanation');
  play.click();ok(play.getAttribute('aria-pressed')==='true'&&flowTimers.size===1,'stepflow exposes playback state');
  next.click();ok(play.getAttribute('aria-pressed')==='false'&&flowTimers.size===0,'manual stepping stops playback');
  scene.querySelector('.sf-reset').click();ok(prev.disabled,'stepflow reset returns to the first step');
}
console.log(`Shared course DOM simulation passed: ${checks} quiz, retry, keyboard, playback and accessibility-state checks. Browser verification is not included.`);

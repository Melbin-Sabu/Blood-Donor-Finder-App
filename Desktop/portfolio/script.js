// Typing Animation

const roles = [
"Full Stack Developer",
"MERN Stack Learner",
"UI Designer"
];

let roleIndex = 0;
let charIndex = 0;

const typing = document.querySelector(".typing");

function typeEffect(){

if(charIndex < roles[roleIndex].length){

typing.textContent += roles[roleIndex].charAt(charIndex);

charIndex++;

setTimeout(typeEffect,100);

}else{

setTimeout(eraseEffect,1500);

}
}

function eraseEffect(){

if(charIndex > 0){

typing.textContent =
roles[roleIndex].substring(0,charIndex-1);

charIndex--;

setTimeout(eraseEffect,50);

}else{

roleIndex++;

if(roleIndex >= roles.length){
roleIndex = 0;
}

setTimeout(typeEffect,200);
}
}

typeEffect();


// Contact Form

const form = document.querySelector("form");

form.addEventListener("submit",function(e){

e.preventDefault();

alert("Thank you for contacting me!");

form.reset();

});


// Scroll Reveal

const observer = new IntersectionObserver(entries=>{

entries.forEach(entry=>{

if(entry.isIntersecting){
entry.target.classList.add("show");
}

});

});

document.querySelectorAll("section").forEach(section=>{

section.classList.add("hidden");

observer.observe(section);

});


// Scroll Progress Bar

window.addEventListener("scroll",()=>{

let scrollTop =
document.documentElement.scrollTop;

let height =
document.documentElement.scrollHeight -
document.documentElement.clientHeight;

let progress =
(scrollTop/height)*100;

document.getElementById("progress-bar")
.style.width = progress + "%";

});


// Project Card Tilt

const cards =
document.querySelectorAll(".project-card");

cards.forEach(card=>{

card.addEventListener("mousemove",(e)=>{

const x =
e.offsetX/card.clientWidth - 0.5;

const y =
e.offsetY/card.clientHeight - 0.5;

card.style.transform =
`perspective(1000px)
rotateY(${x*10}deg)
rotateX(${-y*10}deg)`;

});

card.addEventListener("mouseleave",()=>{

card.style.transform =
"perspective(1000px) rotateY(0deg) rotateX(0deg)";

});

});


// MOBILE MENU

const menuToggle =
document.getElementById("menu-toggle");

const navLinks =
document.getElementById("nav-links");

if(menuToggle){

menuToggle.addEventListener("click",()=>{

navLinks.classList.toggle("active");

});

}
document.querySelectorAll("#nav-links a").forEach(link => {

link.addEventListener("click", () => {

navLinks.classList.remove("active");

});

});
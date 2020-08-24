
const colors = [
    "#60d394",
    "royalblue",
    "#ff3333",
    "greenyellow",
    "#ffa222",
    "violet"
];
const names = ["Aqua green", "Blue", "Red", "Green", "Orange", "Pink"];
var numeros = [];
var numerosUsuario = [];
var nickname = prompt("Enter a nickname: ");

const button = document.getElementById("button");
const pads = document.querySelectorAll(".pads div");
const visual = document.querySelector(".visual");
const body = document.querySelector(".body");
const text = document.getElementById("color");
const posicion = document.getElementById("ronda");
const puntaje = document.getElementById("puntaje");
const taps = document.getElementById("taps");
const contenedor = document.querySelector("#home");


var interval = 1500;
var mini = 500;
var bandGlobal = true;
var ronda = 1;
var userTaps=0;
var numeroAleatorio;

contenedor.style.display = "none";

button.addEventListener('click', () => {
    
    button.innerText = "Next";
    button.style.visibility = "hidden";
    start();
});

window.addEventListener('load', () => {

    
    changeColor("si");

});

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function changeColor(usuario) {

    pads.forEach((pad, index) => {
    
        pad.addEventListener('click', function () {
            if (bandGlobal) {
                body.style.backgroundColor = 'white';
                color.innerText = names[index];
                body.style.backgroundColor = colors[index];
                userTaps++;                
                setTaps(userTaps);
                numerosUsuario.push(index);                
                if (numerosUsuario.length == numeros.length) {
                    var prueba = numerosUsuario.every(function (v, i) { return v === numeros[i] });
                    button.style.visibility = "visible";
                    interval = interval - 100;
                                      
                    if(prueba){
                        puntaje.innerText = 'Score: ' + (ronda-1);                                            
                    } else{
                    	validateSave();
                        button.innerText = 'Restart';
                        showGoBack();
                        reset();
                    }
                    
                    bandGlobal = false;
                    setTaps(0);  
                }
                
            }
        });

    });
}






function validateSave(){
    var opcion = confirm(puntaje.innerText+" Save score?");
    if(opcion){
    	validateNickname();
    	console.log("Guardando puntaje!"); 
    	$.ajax({ 
    		  type: "POST", 
    		  url: "colorsApp/index.html?nickname="+nickname+"&score="+ronda, 
    		  data: [nickname,ronda], 
    		  success: function(datos){ 
    		         console.log(datos)
    		     }, 
    		  dataType: "text"
    		}); 
    }
}

function validateNickname(){
	if(nickname == null || nickname=="" || nickname== " "){
		nickname = "anonymus";
	}
}

function reset(){
    interval = 1500;
    bandGlobal = true;
    ronda = 1;
    userTaps=0;
    numeros=[];
    numerosUsuario=[];
    puntaje.innerText = 'Score: ' + 0;
}

function simulateClick(index) {
    color.innerText = names[index];
    body.style.backgroundColor = colors[index];
    

}

function createBubbles(index){
    
    const bubble = document.createElement("div");
    visual.appendChild(bubble);
    bubble.style.backgroundColor = "white";
    bubble.style.animation = 'jump 1s ease';
    console.log("i: ",index," pos: ",document.getElementById(index+1).style.position);
    
    bubble.addEventListener('animationend', function () {
        visual.removeChild(this);
    });
}

async function start() {
    
    numeros = [];
    for (var i = 0; i < ronda; i++) {
        await sleep(interval);
        numeroAleatorio = parseInt(generateRandomNumber(0, 6), 10);
              
        simulateClick(numeroAleatorio);
        posicion.innerText = "# " + (i+1);
        numeros.push(numeroAleatorio);
    }
    ronda++;
    
    bandGlobal = true;
    
    validateUserAnswer();
}

function showGoBack(){
	
	contenedor.style.display = 'block';	
	
	contenedor.addEventListener('click', function(){
		location.replace("inicio.html");
	});
}

function validateUserAnswer() {
    numerosUsuario = [];    
}

function generateRandomNumber(min, max) {
    return Math.random() * (max - min) + min;
}

function setTaps(tap){
    
    userTaps = tap;
    taps.innerText = 'Your taps: ' + userTaps;   
}

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
alert("Nombre digitado: "+nickname);
const button = document.getElementById("button");
const pads = document.querySelectorAll(".pads div");
const visual = document.querySelector(".visual");
const body = document.querySelector(".body");
const text = document.getElementById("color");
const posicion = document.getElementById("ronda");
const puntaje = document.getElementById("puntaje");
const taps = document.getElementById("taps");
var interval = 1500;
var mini = 500;
var bandGlobal = true;
var ronda = 1;
var userTaps=0;
var numeroAleatorio;

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
                        puntaje.innerText = nickname+"'s " + 'Score: ' + (ronda-1);                                            
                    } else{
                        alert("You Lose! "+puntaje.innerText);
                        button.innerText = 'Restart';
                        reset();
                    }
                    
                    bandGlobal = false;
                    setTaps(0);  
                }
                
            }
        });

    });
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
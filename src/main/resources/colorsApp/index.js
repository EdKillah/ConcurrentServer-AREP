console.log("Cargando...");

window.addEventListener('load', () => {
    const sounds = document.querySelectorAll(".sound");
    const pads = document.querySelectorAll(".pads div");
    const visual = document.querySelector(".visual");
    const body = document.querySelector(".body");
    const text = document.getElementById("color");
    console.log("Visual: ",visual);
    const colors = [
        "#60d394",
        "royalblue",
        "#ff3333",
        "greenyellow",
        "#ffa222",
        "violet"
    ];
    const names = ["Aqua green","Blue","Red","Green","Orange","Pink"];

    pads.forEach((pad,index) => { 
        pad.addEventListener('click', function(){
        sounds[index].currentTime = 0;        
        color.innerText = names[index];
        body.style.backgroundColor = colors[index];   
        createBubbles(index);
        });
    });

 
    const createBubbles = index => {
        const bubble = document.createElement("div");
        visual.appendChild(bubble);
        bubble.style.backgroundColor = "white";
        if(index>4){
            index--;
        }
        else{
            index++;
        }        
        bubble.style.animation = 'jump 1s ease';
        bubble.addEventListener('animationend', function() {
            visual.removeChild(this);
        });

    }

});
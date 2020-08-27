const datos = document.querySelector(".table");
const tbody = document.querySelector("tbody");
const button = document.getElementById("btn");
var list;


$.ajax({
	type : "GET",
	url : "ranking",
	success : function(datos) {
		list = datos;
		console.log(datos);
		console.log("Lista: ",list);
		printList(list);
	},
});


function printList(list) {
	
	list = modifyList(list);
	list.forEach(element => {
	
		element = modifyElement(element);
		
		const tr = document.createElement('tr');
		element.forEach(i => {
			const td = document.createElement('td');
			td.innerHTML = i;
			tr.appendChild(td);
		});

		tbody.appendChild(tr);
	});


}

function modifyList(list){
	list = list.substring(1,(list.length-4));
	list = list.split("],");
	return list;
}

function modifyElement(element){
	element = element.replace("[","");
	element = element.replace("]","");
	element = element.split(",");
	return element;
}

btn.addEventListener('click',function(){
	location.replace("inicio.html");
});
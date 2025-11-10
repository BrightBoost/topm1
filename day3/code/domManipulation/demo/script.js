let knopje = document.getElementById("knopje");
let example = document.getElementById("example");

knopje.addEventListener("click", () => {
    example.textContent = "MAGIC";
});

knopje.addEventListener("mouseover", () => {
    knopje.textContent = "JA hallo?";
});

let formulier = document.getElementById("hetFormulier");
formulier.addEventListener("submit", (event) => {
    event.preventDefault();
    let nameInput = document.getElementById("name").value;
    let newParagraph = document.createElement("p");
    newParagraph.textContent = "Hallo " + nameInput;
    document.getElementById("resultaat").appendChild(newParagraph);
});
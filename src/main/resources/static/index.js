// Número de palabras del texto
let numberOfWords = 10;


//Función que recupera el texto del archivo wordlist.txt y lo transforma en una matriz de longitud numberOfWords
function createText(numberOfWords) {
    return new Promise((resolve, reject) => {
        fetch('/resources/static/wordlist.txt')
            .then(response => response.text())
            .then(data => {
                // Divida el texto en una serie de palabras separadas por nuevas líneas
                const words = data.split('\n');

                const randomWords = [];

                // Obtener una palabra aleatoria de la tabla
                while (randomWords.length < numberOfWords) {
                    const randomIndex = Math.floor(Math.random() * words.length);
                    randomWords.push(words[randomIndex]);
                }

                resolve(randomWords);

            })
            .catch(error => reject(error));

    });
}


function practice() {

    let cursorPosition = 0;
    let text = null;
    let startTime = null;
    let numberOfErrors = 0;

    function startTimer() {
        startTime = new Date();
    }

    // Función que detiene el temporizador, calcula las estadísticas y reinicia un nuevo ejercicio.
    function stopTimer() {
        const endTime = new Date();
        const elapsedTime = (endTime - startTime) / 1000; // Convertit en secondes
        console.log("Temps total de l'exercice : " + elapsedTime + " secondes");

        // Cálculo del número de palabras por minuto, suponiendo palabras de 5 letras
        let wpm = text.length / 5;
        wpm = wpm * 60 / elapsedTime;
        document.getElementById("wpm").innerHTML = wpm.toFixed(2);

        // Cálculo de la precisión
        let accuracy = (text.length - numberOfErrors) * 100 / text.length;
        document.getElementById("accuracy").innerHTML = accuracy.toFixed(2) + "%";

        // Cálculo de CPM
        let cpm = text.length * 60 / elapsedTime;
        document.getElementById("cpm").innerHTML = cpm.toFixed(0);

        //Comenzar un nuevo ejercicio después de parar el temporizador
        startNewExercise();
    }

    function startNewExercise() {
        createText(numberOfWords)
            .then(function (result) {
                text = result.join(' ').split('');
                cursorPosition = 0;
                numberOfErrors = 0;
                updateText(text, cursorPosition);
                startTimer(); // Inicia el temporizador cuando se teclea la primera letra
                waitForUserInput();
            })
            .catch(error => console.error('Une erreur s\'est produite :', error));
    }

    startNewExercise(); // Comience su primer ejercicio

    function waitForUserInput() {
        let position = 0;

        document.addEventListener('keypress', function (event) {
            if (position === 0) {
                // Primera letra tecleada, temporizador en marcha
                startTimer();
            }

            if (event.key === text[position]) {
                // El usuario ha escrito la letra correcta
                position++;

                if (position === text.length) {
                    // El usuario ha terminado, el temporizador se detiene
                    document.removeEventListener('keypress', arguments.callee);
                    stopTimer();
                } else {
                    updateText(text, position);
                }
            } else {
                // El usuario ha cometido un error
                numberOfErrors++;
                document.getElementById("lettersToType").style.color = "red";
                document.getElementById("typedLetters").style.color = "red";
            }
        });
    }

    // Actualiza el texto que se muestra en la pantalla después de cada pulsación de tecla del usuario
    function updateText(text, position) {
        const typedLetters = text.slice(0, position);
        const lettersToType = text.slice(position);
        document.getElementById("lettersToType").textContent = lettersToType.join('');
        document.getElementById("typedLetters").textContent = typedLetters.join('');
        document.getElementById("lettersToType").style.color = "black";
        document.getElementById("typedLetters").style.color = "grey";
    }
}

practice();


// Esta función no se usa por el momento
function azertyToColemak(azertyString) {
    let colemakString;

    for (let i = 0; i < azertyString.length; i++) {
        switch (azertyString[i]) {
            case "q":
                colemakString += "a";
                break;
            case "w":
                colemakString += "z";
                break;
            case "e":
                colemakString += "k";
                break;
            case "r":
                colemakString += "s";
                break;
            case "t":
                colemakString += "f";
                break;
            case "y":
                colemakString += "o";
                break;
            case "u":
                colemakString += "i";
                break;
            case "i":
                colemakString += "l";
                break;
            case "o":
                colemakString += "m";
                break;
            case "p":
                colemakString += "r";
                break;
            case "a":
                colemakString += "q";
                break;
            case "s":
                colemakString += "d";
                break;
            case "d":
                colemakString += "g";
                break;
            case "f":
                colemakString += "e";
                break;
            case "g":
                colemakString += "t";
                break;
            case "j":
                colemakString += "y";
                break;
            case "k":
                colemakString += "n";
                break;
            case "l":
                colemakString += "u";
                break;
            case "z":
                colemakString += "w";
                break;
            case "n":
                colemakString += "j";
                break;
            case "m":
                colemakString += ",";
                break;
            default:
                colemakString += azertyString[i];
        }
    }

    return colemakString;
}



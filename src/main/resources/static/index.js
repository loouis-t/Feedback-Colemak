function createText() {
    return new Promise((resolve, reject) => {
        fetch('/generateRandomText')
            .then(response => response.json())
            .then(data => {
                resolve(data);
            })
            .catch(error => {
                console.error('Erreur lors de la récupération du texte aléatoire :', error);
                reject(error);
            });
    });
}

async function practice() {

    let cursorPosition = 0;
    let text = null;
    let startTime = null;
    let numberOfErrors = 0;
    let emulate = await getColemakEmulationSetting();

    function startTimer() {
        startTime = new Date();
    }

    // Función que detiene el temporizador, calcula las estadísticas y reinicia un nuevo ejercicio.
    async function stopTimer() {
        const endTime = new Date();
        const elapsedTime = (endTime - startTime) / 1000; // Convertit en secondes
        console.log("Temps total de l'exercice : " + elapsedTime + " secondes");

        // Cálculo del número de palabras por minuto, suponiendo palabras de 5 letras
        let wpm = text.length / 5;
        wpm = wpm * 60 / elapsedTime;
        document.getElementById("wpm").innerHTML = wpm.toFixed(2);

        // Cálculo de la precisión
        let accuracy = (text.length - numberOfErrors) * 100 / text.length;
        accuracy = accuracy < 0 ? 0 : accuracy;
        document.getElementById("accuracy").innerHTML = accuracy.toFixed(2);

        // Cálculo de CPM
        let cpm = text.length * 60 / elapsedTime;
        document.getElementById("cpm").innerHTML = cpm.toFixed(0);

        // post "/add-stats" route to save stats in database
        postStats(wpm, accuracy, cpm, elapsedTime);

        //Comenzar un nuevo ejercicio después de parar el temporizador
        startNewExercise();
    }

    // Get colemak emulation setting from database
    async function getColemakEmulationSetting() {
        const response = await fetch('/emulate-colemak');
        return await response.json();
    }

    // Guarda los datos en la base de datos
    async function postStats(wpm, accuracy, cpm, elapsedTime) {
        const data = new URLSearchParams({
            wpm: wpm,
            accuracy: accuracy,
            cpm: cpm,
            elapsedTime: elapsedTime
        });

        const response = await fetch('/add-stats', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: data,
        });

        if (!response.ok)
            console.log("An error occurred while saving the stats. Is user logged in ?");
    }


    function startNewExercise() {
        createText()
            .then(function (result) {
                text = result.join(' ').split('');
                cursorPosition = 0;
                numberOfErrors = 0;
                updateText(text, cursorPosition, false, true);
                startTimer(); // Inicia el temporizador cuando se teclea la primera letra
                waitForUserInput(emulate);
            })
            .catch(error => console.error('Une erreur s\'est produite :', error));
    }

    startNewExercise(); // Comience su primer ejercicio

    function waitForUserInput(emulate) {
        let position = 0;
        let error = false;

        document.addEventListener('keypress', function (event) {
            if (position === 0) {
                // Primera letra tecleada, temporizador en marcha
                startTimer();
            }

            // select .key class where textContent is equal to the key pressed
            // exclude every other key than simple letters
            const excludedClasses = [
                'key__symbols',
                'key__bottom-funct',
                'key__space',
                'key__ta',
                'key__caps',
                'key__delete',
                'key__enter',
                'key__shift-left',
                'key__oneandhalf'
            ];

            // Add class key--pressed to the key pressed
            document.querySelectorAll('.key').forEach(key => {
                if (excludedClasses.every(exClass => !key.classList.contains(exClass)) && key.textContent.toLocaleLowerCase().trim().includes(event.key)) {
                    key.classList.add('key--pressed');
                }
            });

            if (emulate ? letterToKeyCode(text[position]) === event.code : text[position] === event.key) {
                // El usuario ha escrito la letra correcta
                position++;

                if (position === text.length) {
                    // El usuario ha terminado, el temporizador se detiene
                    document.removeEventListener('keypress', arguments.callee);
                    stopTimer();
                } else {
                    updateText(text, position, error);
                    error = false;
                }
            } else {
                // El usuario ha cometido un error
                numberOfErrors++;
                error = true;
            }
        });

        // Remove class key--pressed to the key pressed when released
        document.addEventListener('keyup', function (_) {
            document.querySelectorAll('.key').forEach(key => key.classList.remove('key--pressed'));
        });
    }

    // Actualiza el texto que se muestra en la pantalla después de cada pulsación de tecla del usuario
    function updateText(text, position, error = false, restart = false) {
        const typedLettersElement = document.getElementById("typedLetters");
        const lettersToType = text.slice(position);

        // Si restart es verdadero, entonces el texto debe ser reiniciado
        if (restart) {
            const typedLetters = text.slice(0, position);
            typedLettersElement.textContent = typedLetters.join('');
        }

        // Si la posición es 0, entonces el usuario no ha escrito nada todavía
        if (text[position - 1] !== undefined) {
            typedLettersElement
                .innerHTML += `<span class="${error ? 'error' : ''}">${text[position - 1]}</span>`;
        }

        document.getElementById("lettersToType").textContent = lettersToType.join('');
    }
}

practice()
    .catch((error) => console.error('Une erreur s\'est produite :', error));

function letterToKeyCode(letter) {
    switch (letter) {
        case 'a':
            return 'KeyA';
        case 'z':
            return 'KeyZ';
        case 'e':
            return 'KeyK';
        case 'r':
            return 'KeyS';
        case 't':
            return 'KeyF';
        case 'y':
            return 'KeyO';
        case 'u':
            return 'KeyI';
        case 'i':
            return 'KeyL';
        case 'o':
            return 'Semicolon';
        case 'p':
            return 'KeyR';
        case 'q':
            return 'KeyQ';
        case 's':
            return 'KeyD';
        case 'd':
            return 'KeyG';
        case 'f':
            return 'KeyE';
        case 'g':
            return 'KeyT';
        case 'h':
            return 'KeyH';
        case 'j':
            return 'KeyY';
        case 'k':
            return 'KeyN';
        case 'l':
            return 'KeyU';
        case 'm':
            return 'KeyM';
        case 'w':
            return 'KeyW';
        case 'x':
            return 'KeyX';
        case 'c':
            return 'KeyC';
        case 'v':
            return 'KeyV';
        case 'b':
            return 'KeyB';
        case 'n':
            return 'KeyJ';
        default:
            return 'Space';
    }
}

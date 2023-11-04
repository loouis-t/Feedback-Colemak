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

    // Table of typing times for each letter
    let perKeyTimes = Array.from({length: 27}, () => []);
    let startTimePerKey = null;

    function startTimer() {
        startTime = new Date();
    }

    // Function that stops the timer, calculates the statistics and starts a new exercise.
    async function stopTimer() {
        const endTime = new Date();
        const elapsedTime = (endTime - startTime) / 1000; // Convertit en secondes
        console.log("Temps total de l'exercice : " + elapsedTime + " secondes");

        // Calculation of words per minute, assuming 5 letters words
        let wpm = text.length / 5;
        wpm = wpm * 60 / elapsedTime;
        document.getElementById("wpm").innerHTML = wpm.toFixed(2);

        // Calculation of typing accuracy
        let accuracy = (text.length - numberOfErrors) * 100 / text.length;
        accuracy = accuracy < 0 ? 0 : accuracy;
        document.getElementById("accuracy").innerHTML = accuracy.toFixed(2);

        // Calculation of characters per minute
        let cpm = text.length * 60 / elapsedTime;
        document.getElementById("cpm").innerHTML = cpm.toFixed(0);

        // Calculating the speed at which each key is pressed
        let perKeyWpm = Array.from({length: 27}, () => -1);
        for (let i = 0; i < perKeyTimes.length; i++) {
            if (perKeyTimes[i].length > 0) {
                // Calculating the average typing time for each key.
                let totalLetterTime = perKeyTimes[i].reduce((acc, time) => acc + time, 0);
                let averageLetterTime = totalLetterTime / perKeyTimes[i].length;
                // Calculation of WPM using 60 seconds per minute
                perKeyWpm[i] = 60 / (averageLetterTime * 5);
            }
        }

        // post "/add-stats" route to save stats in database
        await postStats(wpm, accuracy, cpm, elapsedTime, perKeyWpm);

        getLetterStats();

        // Start a new exercise after stopping the timer
        startNewExercise();
    }

    // Get colemak emulation setting from database
    async function getColemakEmulationSetting() {
        const response = await fetch('/emulate-colemak');
        return await response.json();
    }

    // Saves the data in the database
    async function postStats(wpm, accuracy, cpm, elapsedTime, perKeyWpm) {
        const data = new URLSearchParams({
            wpm: wpm,
            accuracy: accuracy,
            cpm: cpm,
            elapsedTime: elapsedTime,
            perKeyWpm: perKeyWpm,
        });

        const response = await fetch('/add-stats', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: data,
        });

        if (!response.ok)
            console.log("An error occurred while saving the stats. Is user logged in?");
    }

    function getLetterStats() {
        document.querySelectorAll('.keys').forEach(key => {
            fetch(`/get-letter-stats?letter=${key.textContent}`)
                .then(response => response.json())
                .then(data => key.style.backgroundColor = data["background"])
                .catch(error => console.error('Une erreur s\'est produite :', error));
        });
    }

    function startNewExercise() {
        createText()
            .then(function (result) {
                text = result.join(' ').split('');
                cursorPosition = 0;
                numberOfErrors = 0;
                updateText(text, cursorPosition, false, true);
                startTimer(); // Inicia el temporizador cuando se teclea la primera letra
                perKeyTimes = Array.from({length: 27}, () => []);
                waitForUserInput(emulate);
            })
            .catch(error => console.error('Une erreur s\'est produite :', error));
    }

    startNewExercise(); // Start your first exercise


    function waitForUserInput(emulate) {
        let position = 0;
        let error = false;

        document.addEventListener('keypress', function (event) {
            if (position === 0) {
                // First letter typed, timer started
                startTimer();
                startTimePerKey = new Date();
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
                let pressedKeyContent = key.textContent.toLocaleLowerCase().trim();

                if (
                    excludedClasses.every(exClass => !key.classList.contains(exClass))
                    && (
                        (emulate && letterToKeyCode(pressedKeyContent) === event.code)
                        || (!emulate && pressedKeyContent.includes(event.key))
                    )
                ) {
                    key.classList.add('key--pressed');
                }
            });

            if (emulate ? letterToKeyCode(text[position]) === event.code : text[position] === event.key) {
                // The user has typed the correct letter
                let keyTime = (new Date() - startTimePerKey) / 1000;
                if (keyTime >= 0.004) {
                    let letter;
                    if (text[position].charCodeAt(0) !== 32)
                        letter = text[position].charCodeAt(0) - 96;
                    else
                        letter = 0;

                    perKeyTimes[letter].push(keyTime);
                }

                position++;

                if (position === text.length) {
                    // The user has finished, the timer stops
                    document.removeEventListener('keypress', arguments.callee);
                    stopTimer();
                } else {
                    updateText(text, position, error);
                    error = false;
                    startTimePerKey = new Date();
                }
            } else {
                // The user has made a mistake
                numberOfErrors++;
                error = true;
            }
        });

        // Remove class key--pressed to the key pressed when released
        document.addEventListener('keyup', function (_) {
            document.querySelectorAll('.key').forEach(key => key.classList.remove('key--pressed'));
        });
    }

    // Updates the text displayed on the screen after each keystroke by the user
    function updateText(text, position, error = false, restart = false) {
        const typedLettersElement = document.getElementById("typedLetters");
        const lettersToType = text.slice(position);

        // If restart is true, then the text must be restarted
        if (restart) {
            const typedLetters = text.slice(0, position);
            typedLettersElement.textContent = typedLetters.join('');
        }

        // If the position is 0, then the user has not written anything yet
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

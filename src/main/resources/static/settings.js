const darkMode = document.querySelector('#toggle');
const textLength = document.querySelector('#range');
const emulateColemak = document.querySelector('#checkbox');

// This function is used to handle the dark mode
function handleDarkMode() {
    darkMode
        .addEventListener('change', async (e) => {
            localStorage.setItem("darkMode", e.target.checked.toString());
            if (e.target.checked)
                document.body.classList.add('dark-mode');
            else
                document.body.classList.remove('dark-mode');

            await updateSettings();
        });


    /* darkMode.checked = localStorage.getItem("darkMode") === "true";
    if (darkMode.checked) {
        document.body.classList.add("dark-mode");
    } else {
        document.body.classList.remove("dark-mode");
    } */
}

// This function is used to update the text length
function handleTextLength() {
    textLength
        .addEventListener('change', async (e) => {
            textLength.nextElementSibling.textContent = textLength.value;
            localStorage.setItem("textLength", e.target.value);
            await updateSettings();
        });
}

// This function is used to emulate the Colemak keyboard
function handleEmulateColemak() {
    emulateColemak
        .addEventListener('change', async (e) => {
            localStorage.setItem("emulateColemak", e.target.checked.toString());
            await updateSettings();
        });
}

handleDarkMode();
handleTextLength();
handleEmulateColemak();

// This function is used to update the settings in the database
async function updateSettings() {
    const data = new URLSearchParams({
        darkMode: darkMode.checked.toString(),
        textLength: textLength.value.toString(),
        emulateColemak: emulateColemak.checked.toString(),
    });

    console.log(data);

    await fetch('/update-settings', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: data,
    })
        .then(response => console.log(response))
        .catch(error => console.error('Une erreur s\'est produite :', error));
}
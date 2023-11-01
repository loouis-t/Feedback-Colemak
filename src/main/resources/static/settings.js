const toggle = document.querySelector('#toggle');

toggle
    .addEventListener('change', (e) => {
        if (e.target.checked) {
            document.body.classList.add('dark-mode');
            localStorage.setItem("darkMode", "true");
        }
        else {
            document.body.classList.remove('dark-mode');
            localStorage.setItem("darkMode", "false");
        }
    });

window.onload = () => {
    toggle.checked = localStorage.getItem("darkMode") === "true";
    if (toggle.checked) {
        document.body.classList.add("dark-mode");
    } else {
        document.body.classList.remove("dark-mode");
    }
}
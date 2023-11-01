// This document is mainly used to set the dark mode of the page.

function handleSessionDarkMode() {
    const darkMode = localStorage.getItem("darkMode");

    if (darkMode === "true") {
        document.body.classList.add("dark-mode");
    } else {
        document.body.classList.remove("dark-mode");
    }
}

handleSessionDarkMode();

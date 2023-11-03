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

// Prevent spacebar from scrolling the page
window.addEventListener('keydown', function(e) {
    if(e.code === "Space" && e.target === document.body) {
        e.preventDefault();
        document.dispatchEvent(new KeyboardEvent('keypress', {code: e.code}));
    }
});

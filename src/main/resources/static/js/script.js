document.addEventListener("DOMContentLoaded", function() {
    const socialButtons = document.querySelectorAll(".social-button");

    socialButtons.forEach(button => {
        button.addEventListener("mousedown", function() {
            button.classList.add("clicked");
        });
        button.addEventListener("mouseup", function() {
            button.classList.remove("clicked");
        });
    });
});

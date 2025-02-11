// ## Contraseñas #######
// Revela el texto insertado en los inputs passwords

    function verContrasenia() {
        var passwordField = document.getElementById("contrasenia");
        var checkbox = document.getElementById("checkbox");

        passwordField.type = checkbox.checked ? "text" : "password";
    }


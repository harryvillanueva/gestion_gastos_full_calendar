// URL base de tu backend Spring Boot
const API_URL = 'http://localhost:8080/api/usuarios';

// --- LÓGICA DE REGISTRO ---
const formRegistro = document.getElementById('formRegistro');
if (formRegistro) {
    formRegistro.addEventListener('submit', async (e) => {
        e.preventDefault();

        const datos = {
            nombre: document.getElementById('nombre').value,
            apellidos: document.getElementById('apellidos').value,
            email: document.getElementById('email').value,
            username: document.getElementById('username').value,
            password: document.getElementById('password').value,
            rol: document.getElementById('rol').value
        };

        try {
            const respuesta = await fetch(`${API_URL}/registro`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(datos)
            });

            const mensajeDiv = document.getElementById('mensaje');

            if (respuesta.status === 201) {
                mensajeDiv.className = 'exito';
                mensajeDiv.innerText = '¡Usuario registrado! Redirigiendo al login...';
                setTimeout(() => window.location.href = 'login.html', 2000);
            } else {
                const errorTexto = await respuesta.text();
                mensajeDiv.className = 'mensaje';
                mensajeDiv.innerText = errorTexto;
            }
        } catch (error) {
            console.error("Error conectando con el servidor:", error);
        }
    });
}

// --- LÓGICA DE LOGIN ---
const formLogin = document.getElementById('formLogin');
if (formLogin) {
    formLogin.addEventListener('submit', async (e) => {
        e.preventDefault();

        const datos = {
            username: document.getElementById('loginUsername').value,
            password: document.getElementById('loginPassword').value
        };

        try {
            const respuesta = await fetch(`${API_URL}/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(datos)
            });

            const mensajeDiv = document.getElementById('mensajeLogin');

            if (respuesta.status === 200) {
                const data = await respuesta.json();

                // Guardamos el token
                localStorage.setItem('jwt_token', data.token);

                // Redirigimos al Dashboard
                window.location.href = 'index.html';
            } else {
                const errorTexto = await respuesta.text();
                mensajeDiv.innerText = errorTexto;
            }
        } catch (error) {
            console.error("Error conectando con el servidor:", error);
        }
    });
}

// --- LÓGICA DE CERRAR SESIÓN ---
function cerrarSesion() {
    localStorage.removeItem('jwt_token');
    window.location.href = 'login.html';
}
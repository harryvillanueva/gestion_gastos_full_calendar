// js/login.js

document.addEventListener('DOMContentLoaded', () => {
    const formLogin = document.getElementById('formLogin');

    if (formLogin) {
        formLogin.addEventListener('submit', async (e) => {
            e.preventDefault();

            const datos = {
                username: document.getElementById('loginUsername').value,
                password: document.getElementById('loginPassword').value
            };

            try {
                const respuesta = await fetch(`${API_BASE_URL}/usuarios/login`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(datos)
                });

                const mensajeDiv = document.getElementById('mensajeLogin');

                if (respuesta.status === 200) {
                    const data = await respuesta.json();
                    localStorage.setItem('jwt_token', data.token);
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
});
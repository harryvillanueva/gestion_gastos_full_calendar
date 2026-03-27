

document.addEventListener('DOMContentLoaded', () => {
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

                const respuesta = await fetch(`${API_BASE_URL}/usuarios/registro`, {
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
});
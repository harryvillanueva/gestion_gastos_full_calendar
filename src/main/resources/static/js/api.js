// js/api.js

const API_BASE_URL = 'http://localhost:8080/api';

function cerrarSesion() {
    localStorage.removeItem('jwt_token');
    window.location.href = 'login.html';
}

function getToken() {
    return localStorage.getItem('jwt_token');
}

// --- ¡NUEVO! FUNCIÓN MÁGICA PARA LEER EL TOKEN ---
function obtenerDatosUsuario() {
    const token = getToken();
    if (!token) return null;

    try {
        // El payload es la segunda parte del token (separado por puntos)
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        // Decodificamos el Base64 a texto y lo parseamos a JSON
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload); // Devuelve algo como: { sub: "harry", rol: "ADMIN", id: 1 }
    } catch (e) {
        console.error("Error al decodificar el token", e);
        return null;
    }
}
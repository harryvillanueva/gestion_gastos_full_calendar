// js/api.js

// URL base de tu backend Spring Boot
const API_BASE_URL = 'http://localhost:8080/api';

// Función global para cerrar sesión (usada en el navbar del dashboard)
function cerrarSesion() {
    localStorage.removeItem('jwt_token');
    window.location.href = 'login.html';
}

// Función para obtener el token actual
function getToken() {
    return localStorage.getItem('jwt_token');
}
// js/dashboard.js

const API_MOVIMIENTOS = `${API_BASE_URL}/movimientos`;
let calendar;
let usuarioActual = obtenerDatosUsuario(); // ¡Leemos el token!

document.addEventListener('DOMContentLoaded', async function() {
    const calendarEl = document.getElementById('calendar');

    if (calendarEl && usuarioActual) {
        const token = getToken();

        // Pintamos el rol en la interfaz
        document.getElementById('etiqueta-rol').innerText = `Rol: ${usuarioActual.rol}`;

        calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'es',
            headerToolbar: { left: 'prev,next today', center: 'title', right: 'dayGridMonth' },

            // 1. CLIC EN UN DÍA VACÍO (CREAR)
            dateClick: function(info) {
                abrirModal(info.dateStr, null);
            },

            // 2. CLIC EN UN EVENTO YA EXISTENTE (VER, EDITAR O BORRAR)
            eventClick: function(info) {
                abrirModal(info.event.startStr, info.event);
            }
        });

        calendar.render();
        await cargarDatos(token);
    }

    // Escuchar el botón Guardar (Crear nuevo)
    const formMovimiento = document.getElementById('formMovimiento');
    if (formMovimiento) {
        formMovimiento.addEventListener('submit', async (e) => {
            e.preventDefault();
            await guardarMovimiento();
        });
    }

    // Escuchar los nuevos botones de Admin
    document.getElementById('btn-actualizar').addEventListener('click', actualizarMovimiento);
    document.getElementById('btn-eliminar').addEventListener('click', eliminarMovimiento);
});

// --- FUNCIONES DE CARGA Y UI ---

async function cargarDatos(token) {
    try {
        const respuesta = await fetch(API_MOVIMIENTOS, {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (respuesta.ok) {
            const movimientos = await respuesta.json();
            let saldoTotal = 0;
            calendar.removeAllEvents();

            movimientos.forEach(mov => {
                saldoTotal += (mov.tipo === 'INGRESO') ? mov.importe : -mov.importe;

                calendar.addEvent({
                    id: mov.id,
                    title: `${mov.descripcion} (${mov.importe}€)`,
                    start: mov.fecha,
                    color: mov.tipo === 'INGRESO' ? '#28a745' : '#dc3545',
                    allDay: true,
                    // Guardamos los datos completos dentro del evento para poder leerlos al hacer clic
                    extendedProps: {
                        descripcion: mov.descripcion,
                        importe: mov.importe,
                        tipo: mov.tipo
                    }
                });
            });

            actualizarSaldoUI(saldoTotal);
        }
    } catch (error) {
        console.error("Error al cargar datos:", error);
    }
}

function actualizarSaldoUI(saldo) {
    const spanSaldo = document.getElementById('saldo-actual');
    spanSaldo.innerText = saldo.toFixed(2);
    spanSaldo.className = '';

    if (saldo > 0) spanSaldo.classList.add('saldo-positivo');
    else if (saldo === 0) spanSaldo.classList.add('saldo-cero');
    else spanSaldo.classList.add('saldo-negativo');
}

// --- LÓGICA DEL MODAL DINÁMICO ---

function abrirModal(fechaStr, eventoExistente) {
    document.getElementById('fecha-seleccionada-texto').innerText = fechaStr;
    document.getElementById('fecha-seleccionada-input').value = fechaStr;
    document.getElementById('modal-movimiento').style.display = 'flex';

    const esAdmin = usuarioActual.rol === 'ADMIN';

    if (eventoExistente) {
        // MODO VER / EDITAR
        document.getElementById('titulo-modal').innerText = "Detalle del Movimiento";
        document.getElementById('id-movimiento-input').value = eventoExistente.id;
        document.getElementById('descripcion').value = eventoExistente.extendedProps.descripcion;
        document.getElementById('importe').value = eventoExistente.extendedProps.importe;
        document.getElementById('tipo').value = eventoExistente.extendedProps.tipo;

        // Configurar vista según Rol
        document.getElementById('btn-guardar').style.display = 'none'; // Nunca mostramos crear

        if (esAdmin) {
            // El admin puede editar, habilitamos campos y botones
            document.getElementById('descripcion').disabled = false;
            document.getElementById('importe').disabled = false;
            document.getElementById('tipo').disabled = false;
            document.getElementById('btn-actualizar').style.display = 'block';
            document.getElementById('btn-eliminar').style.display = 'block';
        } else {
            // El usuario básico solo puede ver, bloqueamos todo
            document.getElementById('descripcion').disabled = true;
            document.getElementById('importe').disabled = true;
            document.getElementById('tipo').disabled = true;
            document.getElementById('btn-actualizar').style.display = 'none';
            document.getElementById('btn-eliminar').style.display = 'none';
        }

    } else {
        // MODO CREAR NUEVO
        document.getElementById('titulo-modal').innerText = "Añadir Movimiento";
        document.getElementById('formMovimiento').reset();
        document.getElementById('fecha-seleccionada-input').value = fechaStr; // Restauramos la fecha tras el reset

        // Habilitar campos por si estaban bloqueados
        document.getElementById('descripcion').disabled = false;
        document.getElementById('importe').disabled = false;
        document.getElementById('tipo').disabled = false;

        document.getElementById('btn-guardar').style.display = 'block';
        document.getElementById('btn-actualizar').style.display = 'none';
        document.getElementById('btn-eliminar').style.display = 'none';
    }
}

function cerrarModal() {
    document.getElementById('modal-movimiento').style.display = 'none';
    document.getElementById('formMovimiento').reset();
}

// --- PETICIONES A LA API (CRUD) ---

async function guardarMovimiento() {
    const datos = {
        descripcion: document.getElementById('descripcion').value,
        importe: parseFloat(document.getElementById('importe').value),
        tipo: document.getElementById('tipo').value,
        fecha: document.getElementById('fecha-seleccionada-input').value
    };

    const respuesta = await fetch(API_MOVIMIENTOS, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${getToken()}` },
        body: JSON.stringify(datos)
    });

    if (respuesta.status === 201) {
        cerrarModal();
        await cargarDatos(getToken());
    } else { alert("Error al guardar"); }
}

async function actualizarMovimiento() {
    const id = document.getElementById('id-movimiento-input').value;
    const datos = {
        descripcion: document.getElementById('descripcion').value,
        importe: parseFloat(document.getElementById('importe').value),
        tipo: document.getElementById('tipo').value,
        fecha: document.getElementById('fecha-seleccionada-input').value // Esta fecha la ignorará el backend
    };

    const respuesta = await fetch(`${API_MOVIMIENTOS}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${getToken()}` },
        body: JSON.stringify(datos)
    });

    if (respuesta.ok) {
        cerrarModal();
        await cargarDatos(getToken());
    } else { alert("Error al actualizar"); }
}

async function eliminarMovimiento() {
    if (!confirm("¿Estás seguro de que quieres eliminar este movimiento?")) return;

    const id = document.getElementById('id-movimiento-input').value;

    const respuesta = await fetch(`${API_MOVIMIENTOS}/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${getToken()}` }
    });

    if (respuesta.ok) {
        cerrarModal();
        await cargarDatos(getToken());
    } else { alert("Error al eliminar"); }
}
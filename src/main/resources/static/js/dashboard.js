// js/dashboard.js

const API_MOVIMIENTOS = `${API_BASE_URL}/movimientos`;
const API_USUARIOS = `${API_BASE_URL}/usuarios`;
const API_NOTIFICACIONES = `${API_BASE_URL}/notificaciones`;
let calendar;
let usuarioActual = obtenerDatosUsuario();

document.addEventListener('DOMContentLoaded', async function() {
    const calendarEl = document.getElementById('calendar');

    if (calendarEl && usuarioActual) {
        const token = getToken();

        document.getElementById('etiqueta-rol').innerText = `Rol: ${usuarioActual.rol}`;

        // 🚀 Si es Admin, cargamos la lista de usuarios para el modal
        if (usuarioActual.rol === 'ADMIN') {
            await cargarUsuariosParaAdmin(token);
        }

        calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'es',
            headerToolbar: { left: 'prev,next today', center: 'title', right: 'dayGridMonth' },
            dateClick: function(info) { abrirModal(info.dateStr, null); },
            eventClick: function(info) { abrirModal(info.event.startStr, info.event); }
        });

        calendar.render();
        await cargarDatos(token);
    }

    const formMovimiento = document.getElementById('formMovimiento');
    if (formMovimiento) {
        formMovimiento.addEventListener('submit', async (e) => {
            e.preventDefault();
            await guardarMovimiento();
        });
    }

    document.getElementById('btn-actualizar').addEventListener('click', actualizarMovimiento);
    document.getElementById('btn-eliminar').addEventListener('click', eliminarMovimiento);
});

// --- FUNCIONES DE CARGA ---

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
                    extendedProps: { descripcion: mov.descripcion, importe: mov.importe, tipo: mov.tipo }
                });
            });
            actualizarSaldoUI(saldoTotal);
        }
    } catch (error) { console.error("Error al cargar datos:", error); }
}

// 🚀 NUEVO: Trae los usuarios si eres ADMIN
async function cargarUsuariosParaAdmin(token) {
    try {
        const respuesta = await fetch(`${API_USUARIOS}/todos`, {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (respuesta.ok) {
            const usuarios = await respuesta.json();
            const selectDestino = document.getElementById('usuarioDestino');

            usuarios.forEach(usr => {
                // No nos añadimos a nosotros mismos a la lista extra
                if (usr.id !== usuarioActual.id) {
                    const option = document.createElement('option');
                    option.value = usr.id;
                    option.text = `${usr.username} (${usr.nombre})`;
                    selectDestino.appendChild(option);
                }
            });
        }
    } catch (error) { console.error("Error cargando usuarios:", error); }
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
        document.getElementById('titulo-modal').innerText = "Detalle del Movimiento";
        document.getElementById('id-movimiento-input').value = eventoExistente.id;
        document.getElementById('descripcion').value = eventoExistente.extendedProps.descripcion;
        document.getElementById('importe').value = eventoExistente.extendedProps.importe;
        document.getElementById('tipo').value = eventoExistente.extendedProps.tipo;

        // 🚀 Ocultamos asignar usuario al editar
        document.getElementById('contenedor-asignar-usuario').style.display = 'none';
        document.getElementById('btn-guardar').style.display = 'none';

        if (esAdmin) {
            document.getElementById('descripcion').disabled = false;
            document.getElementById('importe').disabled = false;
            document.getElementById('tipo').disabled = false;
            document.getElementById('btn-actualizar').style.display = 'block';
            document.getElementById('btn-eliminar').style.display = 'block';
        } else {
            document.getElementById('descripcion').disabled = true;
            document.getElementById('importe').disabled = true;
            document.getElementById('tipo').disabled = true;
            document.getElementById('btn-actualizar').style.display = 'none';
            document.getElementById('btn-eliminar').style.display = 'none';
        }
    } else {
        document.getElementById('titulo-modal').innerText = "Añadir Movimiento";
        document.getElementById('formMovimiento').reset();
        document.getElementById('fecha-seleccionada-input').value = fechaStr;

        document.getElementById('descripcion').disabled = false;
        document.getElementById('importe').disabled = false;
        document.getElementById('tipo').disabled = false;

        document.getElementById('btn-guardar').style.display = 'block';
        document.getElementById('btn-actualizar').style.display = 'none';
        document.getElementById('btn-eliminar').style.display = 'none';

        // 🚀 Si es Admin y va a CREAR, mostramos el cajetín de asignar
        if (esAdmin) {
            document.getElementById('contenedor-asignar-usuario').style.display = 'block';
        }
    }
}

function cerrarModal() {
    document.getElementById('modal-movimiento').style.display = 'none';
    document.getElementById('formMovimiento').reset();
}

// --- PETICIONES CRUD ---

async function guardarMovimiento() {
    // 🚀 Capturamos a quién se lo asigna (si el campo está vacío, será null y se lo asigna a sí mismo)
    const idDestinoVal = document.getElementById('usuarioDestino').value;

    const datos = {
        descripcion: document.getElementById('descripcion').value,
        importe: parseFloat(document.getElementById('importe').value),
        tipo: document.getElementById('tipo').value,
        fecha: document.getElementById('fecha-seleccionada-input').value,
        usuarioIdDestino: idDestinoVal ? parseInt(idDestinoVal) : null // 🚀 Se envía al DTO
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
        fecha: document.getElementById('fecha-seleccionada-input').value
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

    // --- TRANSFERENCIAS ---
    function abrirModalTransferencia() {
        document.getElementById('modal-transferencia').style.display = 'flex';
        cargarUsuariosParaTransferencia();
    }

    function cerrarModalTransferencia() {
        document.getElementById('modal-transferencia').style.display = 'none';
        document.getElementById('formTransferencia').reset();
    }

    async function cargarUsuariosParaTransferencia() {
        const respuesta = await fetch(`${API_USUARIOS}/todos`, { headers: { 'Authorization': `Bearer ${getToken()}` } });
        if (respuesta.ok) {
            const usuarios = await respuesta.json();
            const select = document.getElementById('transf-destino');
            select.innerHTML = ''; // Limpiar
            usuarios.forEach(usr => {
                if (usr.id !== usuarioActual.id) {
                    select.innerHTML += `<option value="${usr.id}">${usr.username} (${usr.nombre})</option>`;
                }
            });
        }
    }

    document.getElementById('formTransferencia').addEventListener('submit', async (e) => {
        e.preventDefault();
        const datos = {
            usuarioIdDestino: parseInt(document.getElementById('transf-destino').value),
            importe: parseFloat(document.getElementById('transf-importe').value)
        };
        const respuesta = await fetch(`${API_MOVIMIENTOS}/transferir`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${getToken()}` },
            body: JSON.stringify(datos)
        });
        if (respuesta.ok) {
            cerrarModalTransferencia();
            await cargarDatos(getToken()); // Recargar saldo y calendario
        } else {
            alert("Error en la transferencia");
        }
    });

    // --- NOTIFICACIONES ---
    // Llama a esta función dentro del DOMContentLoaded (donde cargas los datos del calendario)
    async function cargarNotificaciones() {
        const respuesta = await fetch(API_NOTIFICACIONES, { headers: { 'Authorization': `Bearer ${getToken()}` } });
        if (respuesta.ok) {
            const notifs = await respuesta.json();
            const lista = document.getElementById('lista-notificaciones');
            lista.innerHTML = '';
            if (notifs.length === 0) {
                lista.innerHTML = '<li style="padding: 10px;">No tienes notificaciones.</li>';
            } else {
                notifs.forEach(n => {
                    const fecha = new Date(n.fecha).toLocaleString();
                    lista.innerHTML += `<li style="padding: 10px; border-bottom: 1px solid #ddd;">
                        <strong>[${fecha}]</strong>: ${n.mensaje}
                    </li>`;
                });
            }
        }
    }
}
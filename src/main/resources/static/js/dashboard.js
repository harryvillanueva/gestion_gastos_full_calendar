// js/dashboard.js

const API_MOVIMIENTOS = `${API_BASE_URL}/movimientos`;
let calendar;

document.addEventListener('DOMContentLoaded', async function() {
    const calendarEl = document.getElementById('calendar');

    if (calendarEl) {
        const token = getToken(); // Función de api.js

        // 1. Configuramos FullCalendar
        calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'es',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth'
            },
            dateClick: function(info) {
                abrirModal(info.dateStr);
            }
        });

        // 2. Pintamos y cargamos
        calendar.render();
        await cargarDatos(token);
    }

    // Configurar el formulario del Modal
    const formMovimiento = document.getElementById('formMovimiento');
    if (formMovimiento) {
        formMovimiento.addEventListener('submit', async (e) => {
            e.preventDefault();
            await guardarMovimiento();
        });
    }
});

// --- FUNCIONES DEL DASHBOARD ---

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
                    allDay: true
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

function abrirModal(fechaStr) {
    document.getElementById('fecha-seleccionada-texto').innerText = fechaStr;
    document.getElementById('fecha-seleccionada-input').value = fechaStr;
    document.getElementById('modal-movimiento').style.display = 'flex';
}

function cerrarModal() {
    document.getElementById('modal-movimiento').style.display = 'none';
    document.getElementById('formMovimiento').reset();
}

async function guardarMovimiento() {
    const token = getToken();
    const datos = {
        descripcion: document.getElementById('descripcion').value,
        importe: parseFloat(document.getElementById('importe').value),
        tipo: document.getElementById('tipo').value,
        fecha: document.getElementById('fecha-seleccionada-input').value
    };

    try {
        const respuesta = await fetch(API_MOVIMIENTOS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(datos)
        });

        if (respuesta.status === 201) {
            cerrarModal();
            await cargarDatos(token);
        } else {
            alert("Error al guardar el movimiento");
        }
    } catch (error) {
        console.error("Error de red:", error);
    }
}
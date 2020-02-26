//Al Iniciar la Aplicacion se ejecuta ESTO:
$(document).ready(function () {
    if (typeof document.getElementById("idleMsjMonitor") !== "undefined") {
        var idleMsjMonitor = document.getElementById("idleMsjMonitor");
        if (idleMsjMonitor !== null) {
            var listaNotificaciones = JSON.parse(idleMsjMonitor.innerHTML);
            if (listaNotificaciones !== 'null' && listaNotificaciones !== null && listaNotificaciones !== "undefined") {
                if (listaNotificaciones.length > 0) {
                    var i;
                    for (i = 0; i < listaNotificaciones.length; i++) {
                        notificacion(listaNotificaciones[i]);
                    }
                }
            }
        }
    }
});

//Método para que solo sea permitido introducir números desde el teclado
function pulsarClickPorId(id) {
    try {
        $('#' + id).click();
    } catch (error) {
    }
}
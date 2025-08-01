package org.ReservasRestaurante;

import org.ReservasRestaurante.business.ReservaService;
import org.ReservasRestaurante.data.ReservaRepositoryJdbc;
import org.ReservasRestaurante.presentation.RestauranteVista;

public class Main {
    public static void main(String[] args) {
        ReservaService servicio = new ReservaService(new ReservaRepositoryJdbc());
        RestauranteVista aplicacion = new RestauranteVista(servicio);
        aplicacion.iniciar();
    }
}

package org.ReservasRestaurante;

import org.ReservasRestaurante.business.ReservaService;
import org.ReservasRestaurante.conexion.HibernateSessionManager;
import org.ReservasRestaurante.data.ReservaRepositoryHibernate;
import org.ReservasRestaurante.data.ReservaRepositoryJdbc;
import org.ReservasRestaurante.presentation.RestauranteVista;

public class Main {
    public static void main(String[] args) {
        //ReservaService servicio = new ReservaService(new ReservaRepositoryJdbc());
        HibernateSessionManager.getSessionFactory();

        ReservaService servicio = new ReservaService(new ReservaRepositoryHibernate());
        RestauranteVista aplicacion = new RestauranteVista(servicio);
        aplicacion.iniciar();
    }
}

package org.ReservasRestaurante.data;

import org.ReservasRestaurante.model.Reserva;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservaRepository {
    Reserva guardar(Reserva reserva);
    Optional<Reserva> buscarPorId(String idReserva);
    List<Reserva> buscarPorCliente(String cliente);
    Optional<Reserva> buscarPorClienteYFechaHoraActiva(String cliente, LocalDateTime fechaHora);
    List<Reserva> listarTodas();
    boolean cancelarReserva(String idReserva);
}

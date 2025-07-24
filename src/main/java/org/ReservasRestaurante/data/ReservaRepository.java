package org.ReservasRestaurante.data;

import org.ReservasRestaurante.model.Reserva;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReservaRepository {
    private final List<Reserva> reservas = new ArrayList<>();

    public Reserva guardar(Reserva reserva) {
        if (reserva.getIdReserva() == null || reserva.getIdReserva().isEmpty()) {
            reserva.setIdReserva(UUID.randomUUID().toString());
        }
        reservas.add(reserva);
        return reserva;
    }

    public List<Reserva> listarTodas() {
        return new ArrayList<>(reservas);
    }

    public Optional<Reserva> buscarPorId(String idReserva) {
        return reservas.stream()
                .filter(r -> r.getIdReserva().equals(idReserva))
                .findFirst();
    }

    public List<Reserva> buscarPorCliente(String cliente) {
        return reservas.stream()
                .filter(r -> r.getCliente().equalsIgnoreCase(cliente))
                .toList();
    }

    public Optional<Reserva> buscarPorClienteYFechaHoraActiva(String cliente, LocalDateTime fechaHora) {
        return reservas.stream()
                .filter(r -> r.getCliente().equalsIgnoreCase(cliente) &&
                        r.getFechaHora().equals(fechaHora) &&
                        r.isEstado())
                .findFirst();
    }

    public boolean cancelarReserva(String idReserva) {
        Optional<Reserva> reserva = buscarPorId(idReserva);
        if (reserva.isPresent() && reserva.get().isEstado()) {
            reserva.get().setEstado(false);
            return true;
        }
        return false;
    }
}

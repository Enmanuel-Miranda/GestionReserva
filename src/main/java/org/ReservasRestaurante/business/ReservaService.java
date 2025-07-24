package org.ReservasRestaurante.business;

import org.ReservasRestaurante.data.ReservaRepository;
import org.ReservasRestaurante.exception.BusinessException;
import org.ReservasRestaurante.model.Reserva;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ReservaService {
    private final ReservaRepository repositorio;

    public ReservaService(ReservaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Reserva crearReserva(String cliente, int cantidadPersonas, LocalDateTime fechaHora)
            throws BusinessException {

        validarReglasDeNegocio(cliente, cantidadPersonas, fechaHora);

        Reserva nuevaReserva = new Reserva(cliente, cantidadPersonas, fechaHora);
        return repositorio.guardar(nuevaReserva);
    }

    private void validarReglasDeNegocio(String cliente, int cantidadPersonas, LocalDateTime fechaHora)
            throws BusinessException {

        if (cliente == null || cliente.trim().isEmpty()) {
            throw new BusinessException("Nombre de cliente inválido.");
        }

        if (cantidadPersonas < 1 || cantidadPersonas > 8) {
            throw new BusinessException("Número de personas inválido (1-8).");
        }

        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new BusinessException("No se permiten reservas para fechas pasadas.");
        }

        LocalTime hora = fechaHora.toLocalTime();
        if (hora.isBefore(LocalTime.of(12, 0)) || hora.isAfter(LocalTime.of(23, 0))) {
            throw new BusinessException("Horario de atención fuera de rango (12:00 a 23:00).");
        }

        Optional<Reserva> reservaExistente = repositorio.buscarPorClienteYFechaHoraActiva(cliente, fechaHora);
        if (reservaExistente.isPresent()) {
            throw new BusinessException("Ya existe una reserva activa para este cliente en esta fecha y hora.");
        }
    }

    public List<Reserva> listarReservasActivas() {
        return repositorio.listarTodas().stream()
                .filter(Reserva::isEstado)
                .toList();
    }

    public List<Reserva> buscarReservasPorCliente(String cliente) {
        return repositorio.buscarPorCliente(cliente);
    }

    public void cancelarReserva(String idReserva) throws BusinessException {
        if (!repositorio.cancelarReserva(idReserva)) {
            throw new BusinessException("Reserva no encontrada o ya estaba cancelada.");
        }
    }
}

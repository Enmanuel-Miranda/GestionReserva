package org.ReservasRestaurante.model;

import java.time.LocalDateTime;

public class Reserva {
    private String idReserva;
    private String cliente;
    private int cantidadPersonas;
    private LocalDateTime fechaHora;
    private boolean estado;

    public Reserva(String cliente, int cantidadPersonas, LocalDateTime fechaHora) {
        this.cliente = cliente;
        this.cantidadPersonas = cantidadPersonas;
        this.fechaHora = fechaHora;
        this.estado = true;
    }

    public String getIdReserva() { return idReserva; }
    public void setIdReserva(String idReserva) { this.idReserva = idReserva; }
    public String getCliente() { return cliente; }
    public int getCantidadPersonas() { return cantidadPersonas; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
}



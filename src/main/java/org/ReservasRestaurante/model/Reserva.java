package org.ReservasRestaurante.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idReserva;
    @Column(name = "cliente", nullable = false)
    private String cliente;
    @Column(name = "cantidad_personas", nullable = false)
    private int cantidadPersonas;
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    @Column(name = "estado", nullable = false)
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



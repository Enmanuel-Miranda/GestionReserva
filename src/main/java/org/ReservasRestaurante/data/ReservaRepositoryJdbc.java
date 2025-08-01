package org.ReservasRestaurante.data;

import org.ReservasRestaurante.conexion.ConexionMySQL;
import org.ReservasRestaurante.model.Reserva;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReservaRepositoryJdbc implements ReservaRepository{
    @Override
    public Reserva guardar(Reserva reserva) {
        if (reserva.getIdReserva() == null || reserva.getIdReserva().isEmpty()) {
            reserva.setIdReserva(UUID.randomUUID().toString());
        }
        String sql = "INSERT INTO reservas (idReserva, cliente, cantidad_personas, fecha_hora, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reserva.getIdReserva());
            ps.setString(2, reserva.getCliente());
            ps.setInt(3, reserva.getCantidadPersonas());
            ps.setObject(4, reserva.getFechaHora());
            ps.setBoolean(5, reserva.isEstado());
            ps.executeUpdate();
            return reserva;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar reserva con JDBC", e);
        }
    }

    @Override
    public Optional<Reserva> buscarPorId(String idReserva) {
        String sql = "SELECT * FROM reservas WHERE idReserva = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearReserva(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar reserva con JDBC", e);
        }
        return Optional.empty();

    }

    @Override
    public List<Reserva> buscarPorCliente(String cliente) {
        String sql = "SELECT * FROM reservas WHERE cliente = ?";
        List<Reserva> reservas = new ArrayList<>();
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReserva(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar reservas por cliente con JDBC", e);
        }
        return reservas;
    }

    @Override
    public Optional<Reserva> buscarPorClienteYFechaHoraActiva(String cliente, LocalDateTime fechaHora) {
        String sql = "SELECT * FROM reservas WHERE cliente = ? AND fecha_hora = ? AND estado = true";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente);
            ps.setObject(2, fechaHora);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearReserva(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar reserva activa con JDBC", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Reserva> listarTodas() {
        String sql = "SELECT * FROM reservas";
        List<Reserva> reservas = new ArrayList<>();
        try (Connection conn = ConexionMySQL.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reservas.add(mapearReserva(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al listar reservas con JDBC", e);
        }
        return reservas;
    }

    @Override
    public boolean cancelarReserva(String idReserva) {
        String sql = "UPDATE reservas SET estado = false WHERE idReserva = ? AND estado = true";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cancelar reserva con JDBC", e);
        }
    }

    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva(
                rs.getString("cliente"),
                rs.getInt("cantidad_personas"),
                rs.getObject("fecha_hora", LocalDateTime.class)
        );
        reserva.setIdReserva(rs.getString("idReserva"));
        reserva.setEstado(rs.getBoolean("estado"));
        return reserva;
    }

}

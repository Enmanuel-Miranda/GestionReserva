package org.ReservasRestaurante.data;

import org.ReservasRestaurante.conexion.HibernateSessionManager;
import org.ReservasRestaurante.model.Reserva;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReservaRepositoryHibernate implements ReservaRepository {

    @Override
    public Reserva guardar(Reserva reserva) {
        Transaction transaction = null;
        try (Session session = HibernateSessionManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(reserva);
            transaction.commit();
            return reserva;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error al guardar reserva con Hibernate", e);
        }
    }

    @Override
    public Optional<Reserva> buscarPorId(String idReserva) {
        try (Session session = HibernateSessionManager.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.find(Reserva.class, idReserva));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar reserva por ID con Hibernate", e);
        }
    }

    @Override
    public List<Reserva> buscarPorCliente(String cliente) {
        try (Session session = HibernateSessionManager.getSessionFactory().openSession()) {
            Query<Reserva> query = session.createQuery("from Reserva where cliente = :cliente", Reserva.class);
            query.setParameter("cliente", cliente);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar reservas por cliente con Hibernate", e);
        }
    }

    @Override
    public Optional<Reserva> buscarPorClienteYFechaHoraActiva(String cliente, LocalDateTime fechaHora) {
        try (Session session = HibernateSessionManager.getSessionFactory().openSession()) {
            Query<Reserva> query = session.createQuery(
                    "from Reserva where cliente = :cliente and fechaHora = :fechaHora and estado = true", Reserva.class);
            query.setParameter("cliente", cliente);
            query.setParameter("fechaHora", fechaHora);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar reserva activa con Hibernate", e);
        }
    }

    @Override
    public List<Reserva> listarTodas() {
        try (Session session = HibernateSessionManager.getSessionFactory().openSession()) {
            return session.createQuery("from Reserva", Reserva.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al listar reservas con Hibernate", e);
        }
    }

    @Override
    public boolean cancelarReserva(String idReserva) {
        Transaction transaction = null;
        try (Session session = HibernateSessionManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<?> query = session.createQuery(
                    "update Reserva set estado = false where idReserva = :id and estado = true");
            query.setParameter("id", idReserva);
            int updatedCount = query.executeUpdate();
            transaction.commit();
            return updatedCount > 0;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error al cancelar reserva con Hibernate", e);
        }
    }
}
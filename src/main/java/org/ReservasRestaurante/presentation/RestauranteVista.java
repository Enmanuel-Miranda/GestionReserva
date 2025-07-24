package org.ReservasRestaurante.presentation;

import org.ReservasRestaurante.business.ReservaService;
import org.ReservasRestaurante.data.ReservaRepository;
import org.ReservasRestaurante.model.Reserva;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class RestauranteVista {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final ReservaService reservaService;
    private final Scanner scanner;

    public RestauranteVista() {
        this.reservaService = new ReservaService(new ReservaRepository());
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine();
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> registrarReserva();
                case 2 -> listarReservasActivas();
                case 3 -> buscarReservasCliente();
                case 4 -> cancelarReserva();
                case 5 -> System.out.println("Saliendo del sistema...");
                default -> {
                    if (opcion != -1) {
                        System.out.println("Opción inválida");
                    }
                }
            }
        } while (opcion != 5);
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\n--- SISTEMA DE RESERVAS ---");
        System.out.println("1. Registrar nueva reserva");
        System.out.println("2. Listar reservas activas");
        System.out.println("3. Buscar reservas por cliente");
        System.out.println("4. Cancelar reserva");
        System.out.println("5. Salir");
        System.out.print("Seleccione: ");
    }

    private void registrarReserva() {
        try {
            System.out.print("Nombre del cliente: ");
            String cliente = scanner.nextLine();

            System.out.print("Cantidad de personas (1-8): ");
            int cantidadPersonas = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Fecha y hora (dd/MM/yyyy HH:mm): ");
            String fechaStr = scanner.nextLine();
            LocalDateTime fechaHora = LocalDateTime.parse(fechaStr, formatter);

            Reserva reserva = reservaService.crearReserva(cliente, cantidadPersonas, fechaHora);
            System.out.println("Reserva registrada con ID: " + reserva.getIdReserva());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listarReservasActivas() {
        List<Reserva> reservas = reservaService.listarReservasActivas();
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas activas.");
            return;
        }

        System.out.println("\nRESERVAS ACTIVAS:");
        reservas.forEach(r -> System.out.printf(
                "ID: %s | Cliente: %s | Personas: %d | Fecha: %s%n",
                r.getIdReserva(),
                r.getCliente(),
                r.getCantidadPersonas(),
                r.getFechaHora().format(formatter)
        ));
    }

    private void buscarReservasCliente() {
        System.out.print("Nombre del cliente: ");
        String cliente = scanner.nextLine();

        List<Reserva> reservas = reservaService.buscarReservasPorCliente(cliente);
        if (reservas.isEmpty()) {
            System.out.println("No se encontraron reservas");
            return;
        }

        System.out.println("\nRESERVAS PARA " + cliente + ":");
        reservas.forEach(r -> System.out.printf(
                "ID: %s | Personas: %d | Fecha: %s | Estado: %s%n",
                r.getIdReserva(),
                r.getCantidadPersonas(),
                r.getFechaHora().format(formatter),
                r.isEstado() ? "ACTIVA" : "CANCELADA"
        ));
    }

    private void cancelarReserva() {
        System.out.print("ID de reserva a cancelar: ");
        String idReserva = scanner.nextLine();

        try {
            reservaService.cancelarReserva(idReserva);
            System.out.println("Reserva cancelada exitosamente");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

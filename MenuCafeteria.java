/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.menucafeteria;

/**
 *
 * @author Cinthia
 */
// Clase SistemaCafeteria.java (Programa principal)
import java.util.*;

public class MenuCafeteria {
    private static Scanner scanner = new Scanner(System.in);
    private static GestorPedidos gestor = new GestorPedidos();

    public static void main(String[] args) {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerEnteroPositivo("Seleccione una opción: ");
            
            switch (opcion) {
                case 1:
                    agregarNuevoPedido();
                    break;
                case 2:
                    procesarPedido();
                    break;
                case 3:
                    mostrarPedidosPendientes();
                    break;
                case 4:
                    mostrarHistorialPedidos();
                    break;
                case 5:
                    buscarPedidoPorId();
                    break;
                case 6:
                    generarReporteFinanciero();
                    break;
                case 7:
                    convertirPedidoUrgente();
                    break;
                case 8:
                    salir = true;
                    System.out.println("¡Gracias por utilizar el Sistema de Gestión de Cafetería!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor intente de nuevo.");
            }
        }
        
        scanner.close();
    }
    
    private static void mostrarMenu() {
        System.out.println("\n===== Life Coffe =====");
        System.out.println("1. Agregar nuevo pedido");
        System.out.println("2. Procesar pedido pendiente");
        System.out.println("3. Mostrar pedidos pendientes");
        System.out.println("4. Mostrar historial de pedidos completados");
        System.out.println("5. Buscar pedido por ID");
        System.out.println("6. Generar reporte financiero");
        System.out.println("7. Convertir pedido a urgente");
        System.out.println("8. Salir");
        System.out.println("==========================================");
    }
    
    private static void agregarNuevoPedido() {
        System.out.println("\n--- NUEVO PEDIDO ---");
        scanner.nextLine(); // Limpiar buffer
        System.out.print("Nombre del cliente: ");
        String nombreCliente = scanner.nextLine();
        
        int idPersonalizado = leerEnteroPositivo("Ingrese el ID del pedido (0 para ID automático): ");
        
        List<Producto> catalogoProductos = gestor.getCatalogoProductos();
        List<Producto> productosSeleccionados = new ArrayList<>();
        boolean seguirAgregando = true;
        
        while (seguirAgregando) {
            System.out.println("\nProductos disponibles:");
            for (int i = 0; i < catalogoProductos.size(); i++) {
                System.out.println((i + 1) + ". " + catalogoProductos.get(i));
            }
            
            int seleccion = leerEnteroPositivo("Seleccione un producto (0 para terminar): ");
            
            if (seleccion == 0) {
                seguirAgregando = false;
            } else if (seleccion >= 1 && seleccion <= catalogoProductos.size()) {
                productosSeleccionados.add(catalogoProductos.get(seleccion - 1));
                System.out.println("Producto agregado: " + catalogoProductos.get(seleccion - 1));
            } else {
                System.out.println("Selección no válida.");
            }
        }
        
        if (productosSeleccionados.isEmpty()) {
            System.out.println("No se puede crear un pedido sin productos.");
            return;
        }
        
        Pedido nuevoPedido;
        if (idPersonalizado > 0) {
            nuevoPedido = new Pedido(idPersonalizado, nombreCliente, productosSeleccionados);
        } else {
            nuevoPedido = new Pedido(nombreCliente, productosSeleccionados);
        }
        
        System.out.print("¿Es un pedido urgente? (S/N): ");
        String esUrgente = scanner.next().trim().toUpperCase();
        if (esUrgente.equals("S")) {
            nuevoPedido.setUrgente(true);
            System.out.println("El pedido ha sido marcado como URGENTE.");
        }
        
        gestor.agregarPedido(nuevoPedido);
        System.out.println("Pedido #" + nuevoPedido.getId() + " agregado exitosamente.");
    }
    
    private static void procesarPedido() {
        Pedido pedidoProcesado = gestor.procesarPedido();
        
        if (pedidoProcesado == null) {
            System.out.println("No hay pedidos pendientes para procesar.");
        } else {
            System.out.println("\n¡Pedido procesado con éxito!");
            System.out.println(pedidoProcesado.detallesCompletos());
        }
    }
    
    private static void mostrarPedidosPendientes() {
        List<Pedido> pendientes = gestor.getPedidosPendientes();
        
        if (pendientes.isEmpty()) {
            System.out.println("No hay pedidos pendientes.");
        } else {
            System.out.println("\n--- PEDIDOS PENDIENTES ---");
            for (int i = 0; i < pendientes.size(); i++) {
                Pedido pedido = pendientes.get(i);
                System.out.println((i + 1) + ". " + pedido);
            }
        }
    }
    
    private static void mostrarHistorialPedidos() {
        List<Pedido> completados = gestor.getPedidosCompletados();
        
        if (completados.isEmpty()) {
            System.out.println("No hay pedidos completados en el historial.");
        } else {
            System.out.println("\n--- HISTORIAL DE PEDIDOS COMPLETADOS ---");
            for (Pedido pedido : completados) {
                System.out.println(pedido);
            }
        }
    }
    
    private static void buscarPedidoPorId() {
        int id = leerEnteroPositivo("Ingrese el ID del pedido a buscar: ");
        Pedido pedido = gestor.buscarPedidoPorId(id);
        
        if (pedido == null) {
            System.out.println("No se encontró ningún pedido con el ID " + id);
        } else {
            System.out.println("\n=== PEDIDO ENCONTRADO ===");
            System.out.println(pedido.detallesCompletos());
        }
    }
    
    private static void generarReporteFinanciero() {
        double totalIngresos = gestor.generarReporteFinanciero();
        List<Pedido> completados = gestor.getPedidosCompletados();
        
        System.out.println("\n====== REPORTE FINANCIERO ======");
        System.out.println("Total de pedidos completados: " + completados.size());
        System.out.println("Total de ingresos: Q" + String.format("%.2f", totalIngresos));
        
        // Calcular producto más vendido
        if (!completados.isEmpty()) {
            Map<String, Integer> conteoProductos = new HashMap<>();
            Map<String, Double> ingresosPorProducto = new HashMap<>();
            
            for (Pedido pedido : completados) {
                for (Producto producto : pedido.getProductos()) {
                    String nombreProducto = producto.getNombre();
                    conteoProductos.put(nombreProducto, conteoProductos.getOrDefault(nombreProducto, 0) + 1);
                    ingresosPorProducto.put(nombreProducto, 
                                           ingresosPorProducto.getOrDefault(nombreProducto, 0.0) + producto.getPrecio());
                }
            }
            
            // Encontrar el producto más vendido
            String productoMasVendido = "";
            int maxVentas = 0;
            
            for (Map.Entry<String, Integer> entry : conteoProductos.entrySet()) {
                if (entry.getValue() > maxVentas) {
                    maxVentas = entry.getValue();
                    productoMasVendido = entry.getKey();
                }
            }
            
            if (!productoMasVendido.isEmpty()) {
                System.out.println("\nProducto más vendido: " + productoMasVendido + 
                                   " (" + maxVentas + " unidades, Q" + 
                                   String.format("%.2f", ingresosPorProducto.get(productoMasVendido)) + ")");
            }
            
            // Mostrar top 3 productos con más ingresos
            System.out.println("\nProductos con mayores ingresos:");
            ingresosPorProducto.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": Q" + 
                                                    String.format("%.2f", entry.getValue())));
        }
        
        System.out.println("================================");
    }
    
    private static void convertirPedidoUrgente() {
        mostrarPedidosPendientes();
        
        if (gestor.getPedidosPendientes().isEmpty()) {
            return; // Ya se mostró el mensaje en el método anterior
        }
        
        int id = leerEnteroPositivo("Ingrese el ID del pedido a convertir en urgente: ");
        Pedido pedido = gestor.buscarPedidoPorId(id);
        
        if (pedido == null) {
            System.out.println("No se encontró ningún pedido con el ID " + id);
            return;
        }
        
        if (pedido.isUrgente()) {
            System.out.println("El pedido ya está marcado como urgente.");
            return;
        }
        
        // Verificar si ya está en pedidos completados
        if (gestor.getPedidosCompletados().contains(pedido)) {
            System.out.println("No se puede marcar como urgente un pedido ya completado.");
            return;
        }
        
        // Crear una nueva lista de pedidos pendientes sin el pedido seleccionado
        List<Pedido> pendientes = gestor.getPedidosPendientes();
        Queue<Pedido> nuevaCola = new LinkedList<>();
        
        // Primero agregamos el pedido urgente
        pedido.setUrgente(true);
        nuevaCola.add(pedido);
        
        // Luego agregamos el resto de pedidos (excepto el que ya marcamos como urgente)
        for (Pedido p : pendientes) {
            if (p.getId() != id) {
                nuevaCola.add(p);
            }
        }
        
        // Vaciamos la cola actual y agregamos los pedidos en el nuevo orden
        while (!gestor.getPedidosPendientes().isEmpty()) {
            gestor.procesarPedido();
        }
        
        for (Pedido p : nuevaCola) {
            gestor.agregarPedido(p);
        }
        
        System.out.println("El pedido #" + id + " ha sido marcado como urgente y movido al principio de la cola.");
    }
    
    private static int leerEnteroPositivo(String mensaje) {
        int numero = -1;
        boolean entradaValida = false;
        
        while (!entradaValida) {
            try {
                System.out.print(mensaje);
                numero = scanner.nextInt();
                
                if (numero >= 0) {
                    entradaValida = true;
                } else {
                    System.out.println("Por favor, ingrese un número no negativo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingrese un número válido.");
                scanner.next(); // Limpiar buffer
            }
        }
        
        return numero;
    }
}
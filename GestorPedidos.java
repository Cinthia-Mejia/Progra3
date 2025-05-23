/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menucafeteria;

/**
 *
 * @author Cinthia
 */
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class GestorPedidos implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Queue<Pedido> pedidosPendientes;
    private Stack<Pedido> pedidosCompletados;
    private List<Producto> catalogoProductos;
    
    public GestorPedidos() {
        this.pedidosPendientes = new LinkedList<>();
        this.pedidosCompletados = new Stack<>();
        inicializarCatalogo();
    }
    
    private void inicializarCatalogo() {
        // Lista inicial de productos disponibles
        catalogoProductos = new ArrayList<>();
        catalogoProductos.add(new Producto("Café con crema", 35.0));
        catalogoProductos.add(new Producto("Cappuccino", 45.0));
        catalogoProductos.add(new Producto("frapuchino de oreo", 48.0));
        catalogoProductos.add(new Producto("baget de jamon y queso", 30.0));
        catalogoProductos.add(new Producto("Jugo de frutos rojos", 25.0));
        catalogoProductos.add(new Producto("Pastel de piña colada", 50.0));
        catalogoProductos.add(new Producto("Croissant de aguate y huebo", 35.0));
        catalogoProductos.add(new Producto("Brownies", 20.0));
    }
    
    public List<Producto> getCatalogoProductos() {
        return new ArrayList<>(catalogoProductos);
    }
    
    public void agregarPedido(Pedido pedido) {
        if (pedido.isUrgente()) {
            // Para pedidos urgentes, creamos una nueva cola con el pedido urgente al principio
            Queue<Pedido> nuevaCola = new LinkedList<>();
            nuevaCola.add(pedido);
            
            // Añadimos el resto de pedidos pendientes
            nuevaCola.addAll(pedidosPendientes);
            
            // Reemplazamos la cola original
            pedidosPendientes = nuevaCola;
        } else {
            pedidosPendientes.add(pedido);
        }
    }
    
    public Pedido procesarPedido() {
        if (pedidosPendientes.isEmpty()) {
            return null;
        }
        
        Pedido pedidoProcesado = pedidosPendientes.poll();
        pedidosCompletados.push(pedidoProcesado);
        return pedidoProcesado;
    }
    
    public List<Pedido> getPedidosPendientes() {
        return new ArrayList<>(pedidosPendientes);
    }
    
    public List<Pedido> getPedidosCompletados() {
        List<Pedido> listaPedidosCompletados = new ArrayList<>(pedidosCompletados);
        Collections.reverse(listaPedidosCompletados); // Para mostrarlos del más reciente al más antiguo
        return listaPedidosCompletados;
    }
    
    public Pedido buscarPedidoPorId(int id) {
        // Buscar en pedidos completados
        for (Pedido pedido : pedidosCompletados) {
            if (pedido.getId() == id) {
                return pedido;
            }
        }
        
        // Buscar en pedidos pendientes
        for (Pedido pedido : pedidosPendientes) {
            if (pedido.getId() == id) {
                return pedido;
            }
        }
        
        return null;
    }
    
    public double generarReporteFinanciero() {
        double totalIngresos = 0;
        for (Pedido pedido : pedidosCompletados) {
            totalIngresos += pedido.getTotal();
        }
        return totalIngresos;
    }
}
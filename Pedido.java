/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menucafeteria;

/**
 *
 * @author Cinthia
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int contadorIds = 1;
    
    private int id;
    private String nombreCliente;
    private List<Producto> productos;
    private double total;
    private LocalDateTime fechaHora;
    private boolean urgente;
    
    // Constructor original con ID automático
    public Pedido(String nombreCliente, List<Producto> productos) {
        this.id = contadorIds++;
        this.nombreCliente = nombreCliente;
        this.productos = new ArrayList<>(productos);
        this.fechaHora = LocalDateTime.now();
        this.urgente = false;
        calcularTotal();
    }
    
    // Nuevo constructor que permite especificar el ID
    public Pedido(int id, String nombreCliente, List<Producto> productos) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.productos = new ArrayList<>(productos);
        this.fechaHora = LocalDateTime.now();
        this.urgente = false;
        calcularTotal();
        
        // Actualizar el contador si el ID proporcionado es mayor
        if (id >= contadorIds) {
            contadorIds = id + 1;
        }
    }
    
    private void calcularTotal() {
        this.total = 0;
        for (Producto producto : productos) {
            this.total += producto.getPrecio();
        }
    }
    
    public int getId() {
        return id;
    }
    
    public String getNombreCliente() {
        return nombreCliente;
    }
    
    public List<Producto> getProductos() {
        return new ArrayList<>(productos);
    }
    
    public double getTotal() {
        return total;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public boolean isUrgente() {
        return urgente;
    }
    
    public void setUrgente(boolean urgente) {
        this.urgente = urgente;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id)
          .append(" | Cliente: ").append(nombreCliente)
          .append(" | Fecha: ").append(fechaHora.format(formatter))
          .append(" | Total: Q").append(String.format("%.2f", total));
        
        if (urgente) {
            sb.append(" | URGENTE");
        }
        
        return sb.toString();
    }
    
    public String detallesCompletos() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("\n----------- PEDIDO #").append(id).append(" -----------\n")
          .append("Cliente: ").append(nombreCliente).append("\n")
          .append("Fecha y hora: ").append(fechaHora.format(formatter)).append("\n")
          .append("Productos solicitados:\n");
        
        for (Producto producto : productos) {
            sb.append("- ").append(producto).append("\n");
        }
        
        sb.append("Total a pagar: Q").append(String.format("%.2f", total)).append("\n");
        
        if (urgente) {
            sb.append("Estado: URGENTE\n");
        }
        
        sb.append("--------------------------------------\n");
        
        return sb.toString();
    }
}
package com.empresa.transacciones.app.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACCIONES")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "IDENTIFICADOR", length = 64, nullable = false)
    private String identificador;

    @Column(name = "NUMERO_REFERENCIA", length = 6, nullable = false, unique = true)
    private String numeroReferencia;

    @Column(name = "TOTAL_COMPRA", precision = 20, scale = 2, nullable = false)
    private BigDecimal totalCompra;

    @Column(name = "DIRECCION_COMPRA", length = 100)
    private String direccionCompra;

    @Column(name = "ESTADO_TRANSACCION", length = 20)
    private String estadoTransaccion;

    @Column(name = "FECHA_TRANSACCION", nullable = false)
    private LocalDateTime fechaTransaccion;

    @Column(name = "FECHA_ANULACION")
    private LocalDateTime fechaAnulacion;
    
    @Column(name = "MOTIVO", length = 50)
    private String motivo;


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getIdentificador() { return identificador; }

    public void setIdentificador(String identificador) { this.identificador = identificador; }

    public String getNumeroReferencia() { return numeroReferencia; }

    public void setNumeroReferencia(String numeroReferencia) { this.numeroReferencia = numeroReferencia; }

    public BigDecimal getTotalCompra() { return totalCompra; }

    public void setTotalCompra(BigDecimal totalCompra) { this.totalCompra = totalCompra; }

    public String getDireccionCompra() { return direccionCompra; }

    public void setDireccionCompra(String direccionCompra) { this.direccionCompra = direccionCompra; }

    public String getEstadoTransaccion() { return estadoTransaccion; }

    public void setEstadoTransaccion(String estadoTransaccion) { this.estadoTransaccion = estadoTransaccion; }

    public LocalDateTime getFechaTransaccion() { return fechaTransaccion; }

    public void setFechaTransaccion(LocalDateTime fechaTransaccion) { this.fechaTransaccion = fechaTransaccion; }

    public LocalDateTime getFechaAnulacion() { return fechaAnulacion; }

    public void setFechaAnulacion(LocalDateTime fechaAnulacion) { this.fechaAnulacion = fechaAnulacion; }
    
    
    public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@PrePersist
    public void setFechaTrx() {
    	 this.fechaTransaccion = LocalDateTime.now();
    }
}


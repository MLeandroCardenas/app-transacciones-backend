package com.empresa.transacciones.app.models;

import com.empresa.transacciones.app.utils.commons.HashUtil;
import com.empresa.transacciones.app.utils.commons.RandomUtilGenerator;
import com.empresa.transacciones.app.utils.enums.CardStatesEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "TARJETAS",
        uniqueConstraints = {
                @UniqueConstraint(name = "TARJETAS_UK_PAN", columnNames = "PAN")
        },
        indexes = {
                @Index(name = "IDX_TARJETAS_IDENTIFICADOR", columnList = "IDENTIFICADOR")
        }
)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Digits(integer = 19, fraction = 0)
    @Column(name = "PAN", precision = 19, nullable = false, unique = true)
    private BigDecimal pan;

    @NotBlank
    @Column(name = "TITULAR", length = 100, nullable = false)
    private String titular;

    @NotBlank
    @Column(name = "CEDULA", length = 15, nullable = false)
    private String cedula;

    @NotNull
    @Column(name = "TIPO", length = 1, nullable = false)
    private String tipo;

    @Column(name = "TELEFONO", precision = 10)
    private BigDecimal telefono;

    @Column(name = "ESTADO", length = 20)
    private String estado;

    @Column(name = "NUMERO_VALIDACION", precision = 3)
    private int numeroValidacion;

    @Column(name = "IDENTIFICADOR", length = 64)
    private String identificador;

    public Card() { // Noncompliant - method is empty

    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getPan() { return pan; }
    public void setPan(BigDecimal pan) { this.pan = pan; }

    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getTelefono() { return telefono; }
    public void setTelefono(BigDecimal telefono) { this.telefono = telefono; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getNumeroValidacion() { return numeroValidacion; }
    public void setNumeroValidacion(int numeroValidacion) { this.numeroValidacion = numeroValidacion; }

    public String getIdentificador() { return identificador; }
    public void setIdentificador(String identificador) { this.identificador = identificador; }

    @PrePersist
    public void generarValores() {
        if (this.pan != null && (this.identificador == null || this.identificador.isEmpty())) {
            this.identificador = HashUtil.generarHash(this.pan.toPlainString());
        }
        this.numeroValidacion = RandomUtilGenerator.generarNumero();
        this.estado = CardStatesEnum.CREADA.name();
    }
}


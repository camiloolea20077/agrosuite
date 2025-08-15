package com.erp.backend_erp.entity.inventory;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "insumo_request_details")
@Getter
@Setter
public class InsumoRequestDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "insumo_request_id", nullable = false)
    private Long insumoRequestId;

    @Column(name = "inventory_id", nullable = false)
    private Long inventoryId;

    @Column(name = "cantidad_solicitada", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadSolicitada;

    @Column(name = "cantidad_aprobada", precision = 10, scale = 2)
    private BigDecimal cantidadAprobada = BigDecimal.ZERO;

    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;

    @Column(name = "justificacion")
    private String justificacion;

    @Column(name = "observaciones")
    private String observaciones;
}

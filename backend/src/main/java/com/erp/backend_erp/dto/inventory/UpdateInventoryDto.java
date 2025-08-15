package com.erp.backend_erp.dto.inventory;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInventoryDto {
    private Long id;
    private String codigoInterno;
    private String nombreInsumo;
    private String descripcion;
    private String marca;
    private Long tipoInsumoId;
    private String unidadMedida;
    private String unidadCompra;
    private BigDecimal factorConversion;
    private BigDecimal cantidadActual;
    private BigDecimal cantidadMinima;
    private BigDecimal puntoReorden;
    private BigDecimal cantidadReservada;
    private String ubicacionAlmacen;
    private Boolean esPeligroso;
    private Boolean requiereCuidadoEspecial;
    private String notas;
    private Long estadoId;
    private Long updatedBy;
}

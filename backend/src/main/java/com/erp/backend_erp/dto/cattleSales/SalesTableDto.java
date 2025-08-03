package com.erp.backend_erp.dto.cattleSales;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesTableDto {
    private Long id;
    private String tipo_venta;
    private String fecha_venta;
    private String destino;
    private String tipo_origen;
    private String estado;
    private String observaciones;
    private Long total_animales;
    private Long precio_kilo;
    private Long peso_total;
    private Long subtotal;
    private Long iva;
    private Long descuentos;
    private Long total_venta;
}

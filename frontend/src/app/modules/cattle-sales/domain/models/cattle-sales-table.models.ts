export class CattleSalesTableModel {
    id: number;
    tipo_venta: string;
    fecha_venta: string;
    comprador: string;
    tipo_origen: string;
    observaciones: string;
    total_animales: number;
    peso_total: number;
    total_venta: number;
    precio_promedio: number;

    constructor(
        id: number,
        tipo_origen: string,
        tipo_venta: string,
        fecha_venta: string,
        comprador: string,
        observaciones: string,
        total_animales: number,
        peso_total: number,
        total_venta: number,
        precio_promedio: number
    ) {
        this.id = id;
        this.tipo_origen = tipo_origen;
        this.tipo_venta = tipo_venta;
        this.fecha_venta = fecha_venta;
        this.comprador = comprador;
        this.observaciones = observaciones;
        this.total_animales = total_animales;
        this.peso_total = peso_total;
        this.total_venta = total_venta;
        this.precio_promedio = precio_promedio;
    }
}
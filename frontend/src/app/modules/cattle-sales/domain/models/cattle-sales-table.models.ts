export class CattleSalesTableModel {
    id: number;
    tipo_venta: string;
    fecha_venta: string;
    destino: string;
    tipo_origen: string;
    observaciones: string;
    total_animales: number;
    precio_kilo: number;
    peso_total: number;
    subtotal: number;
    iva: number;
    descuentos: number;
    total_venta: number;

    constructor(
        id: number,
        tipo_venta: string,
        fecha_venta: string,
        destino: string,
        tipo_origen: string,
        observaciones: string,
        total_animales: number,
        precio_kilo: number,
        peso_total: number,
        subtotal: number,
        iva: number,
        descuentos: number,
        total_venta: number
    ) {
        this.id = id;
        this.tipo_venta = tipo_venta;
        this.fecha_venta = fecha_venta;
        this.destino = destino;
        this.tipo_origen = tipo_origen;
        this.observaciones = observaciones;
        this.total_animales = total_animales;
        this.precio_kilo = precio_kilo;
        this.peso_total = peso_total;
        this.subtotal = subtotal;
        this.iva = iva;
        this.descuentos = descuentos;
        this.total_venta = total_venta;
    }
}

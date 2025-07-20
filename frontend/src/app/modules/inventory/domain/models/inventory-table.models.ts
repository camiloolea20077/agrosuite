export class InventoryTableModel {
    id:number
    nombre_insumo:string
    unidad:string
    cantidad_total:number
    descripcion:string
    constructor(id:number, nombre_insumo:string, unidad:string, cantidad_total:number, descripcion:string) {
        this.id = id;
        this.nombre_insumo = nombre_insumo;
        this.unidad = unidad;
        this.cantidad_total = cantidad_total;
        this.descripcion = descripcion;
    }
}
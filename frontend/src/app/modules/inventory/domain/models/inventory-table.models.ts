export class InventoryTableModel {
    id:number
    nombre_insumo:string
    unidad:string
    stock_actual:number
    descripcion:string
    constructor(id:number, nombre_insumo:string, unidad:string, stock_actual:number, descripcion:string) {
        this.id = id;
        this.nombre_insumo = nombre_insumo;
        this.unidad = unidad;
        this.stock_actual = stock_actual;
        this.descripcion = descripcion;
    }
}
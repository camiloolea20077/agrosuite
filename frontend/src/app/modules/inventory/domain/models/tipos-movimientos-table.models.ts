export class TiposMovimientosTableModel {
    id:number
    codigo:string
    nombre:string
    descripcion:string
    activo:number
    constructor(id:number, codigo:string, nombre:string, descripcion:string, activo:number) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }
}
export class EmployeesTableModel {
    id: number;
    nombre: string;
    identificacion: string;
    cargo: string;
    fecha_ingreso: string;
    activo:number
    constructor(nombre: string, identificacion: string, cargo: string, fecha_ingreso: string, activo:number, id: number) {
        this.nombre = nombre;
        this.id = id;
        this.identificacion = identificacion;
        this.cargo = cargo;
        this.fecha_ingreso = fecha_ingreso;
        this.activo = activo;
    }
}
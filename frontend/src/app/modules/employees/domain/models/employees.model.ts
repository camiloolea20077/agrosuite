export class EmployeesModel {
    id: number;
    nombre: string;
    identificacion: string;
    cargo: string;
    fecha_ingreso: string;
    activo: number;

    constructor(id: number, nombre: string, identificacion: string, cargo: string, fecha_ingreso: string, activo: number) {
        this.id = id;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.cargo = cargo;
        this.fecha_ingreso = fecha_ingreso;
        this.activo = activo;
    }
}
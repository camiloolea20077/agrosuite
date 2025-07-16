export class BirthsModel {
    id: number;
    tipo_vaca: number;
    nombre_toro: number;
    fecha_nacimiento: string;
    numero_cria: string;
    observaciones: string;
    sexo: string;
    color_cria: string;

    constructor(id: number, tipo_vaca: number, nombre_toro: number, fecha_nacimiento: string, numero_cria: string, observaciones: string, sexo: string, color_cria: string) {
        this.id = id;
        this.tipo_vaca = tipo_vaca;
        this.nombre_toro = nombre_toro;
        this.fecha_nacimiento = fecha_nacimiento;
        this.numero_cria = numero_cria;
        this.observaciones = observaciones;
        this.sexo = sexo;
        this.color_cria = color_cria;
    }
}
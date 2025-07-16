export class BirthsTableModel {
    numero_ganado: string;
    numero_toro: string;
    fecha_nacimiento: string;
    numero_cria: number;
    sexo: string;
    color_cria: string;
    observaciones: string;

    constructor(numero_ganado: string, numero_toro: string, fecha_nacimiento: string, numero_cria: number, sexo: string, color_cria: string, observaciones: string) {
        this.numero_ganado = numero_ganado;
        this.numero_toro = numero_toro;
        this.fecha_nacimiento = fecha_nacimiento;
        this.numero_cria = numero_cria;
        this.sexo = sexo;
        this.color_cria = color_cria;
        this.observaciones = observaciones;
    }
}
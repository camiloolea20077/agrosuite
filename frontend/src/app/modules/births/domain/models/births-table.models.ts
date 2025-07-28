export class BirthsTableModel {
    id: number;
    numero_ganado: string;
    numero_toro: string;
    fecha_nacimiento: string;
    numero_cria: number;
    sexo: string;
    peso_cria: string;
    color_cria: string;
    observaciones: string;

    constructor(id: number, numero_ganado: string, numero_toro: string, fecha_nacimiento: string, numero_cria: number, sexo: string, color_cria: string, observaciones: string, peso_cria: string) {
        this.numero_ganado = numero_ganado;
        this.numero_toro = numero_toro;
        this.id = id;
        this.peso_cria = peso_cria;
        this.fecha_nacimiento = fecha_nacimiento;
        this.numero_cria = numero_cria;
        this.sexo = sexo;
        this.color_cria = color_cria;
        this.observaciones = observaciones;
    }
}
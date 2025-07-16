export class CattleModel{
    id: number;
    tipo_ganado: string;
    numero_ganado: string;
    sexo: string;
    color: string;
    peso: string;
    fecha_nacimiento: string;
    lote_ganado: string;
    observaciones: string;
    activo: number;
    constructor(
        id: number,
        tipo_ganado: string,
        numero_ganado: string,
        sexo: string,
        color: string,
        peso: string,
        fecha_nacimiento: string,
        lote_ganado: string,
        observaciones: string,
        activo: number
    ) {
        this.id = id;
        this.tipo_ganado = tipo_ganado;
        this.numero_ganado = numero_ganado;
        this.sexo = sexo;
        this.color = color;
        this.peso = peso;
        this.fecha_nacimiento = fecha_nacimiento;
        this.lote_ganado = lote_ganado;
        this.observaciones = observaciones;
        this.activo = activo;
    }
}
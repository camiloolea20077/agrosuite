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
    embarazada: number;
    tipo_animal:string
    fecha_embarazo:string | null
    constructor(
        id: number,
        embarazada: number,
        tipo_animal:string,
        fecha_embarazo:string,
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
        this.embarazada = embarazada;
        this.tipo_animal=tipo_animal;
        this.fecha_embarazo=fecha_embarazo
    }
}
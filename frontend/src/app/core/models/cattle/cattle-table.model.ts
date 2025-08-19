export class CattleTableModel {
    id: number;
    tipo_ganado: string;
    numero_ganado: string;
    tipo_animal: string;
    fecha_embarazo: string;
    peso: string;
    fecha_nacimiento: string;
    lote_ganado: string;
    embarazo: number;
    activo: number;

    constructor(
        id: number,
        tipo_ganado: string,
        numero_ganado: string,
        tipo_animal: string,
        fecha_embarazo: string,
        peso: string,
        fecha_nacimiento: string,
        lote_ganado: string,
        embarazo: number,
        activo: number
    ) {
        this.id = id;
        this.tipo_ganado = tipo_ganado;
        this.numero_ganado = numero_ganado;
        this.tipo_animal = tipo_animal;
        this.fecha_embarazo = fecha_embarazo;
        this.peso = peso;
        this.fecha_nacimiento = fecha_nacimiento;
        this.lote_ganado = lote_ganado;
        this.embarazo = embarazo;
        this.activo = activo;
    }
}
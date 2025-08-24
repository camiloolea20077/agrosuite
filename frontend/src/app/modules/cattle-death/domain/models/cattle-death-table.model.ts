export class CattleDeathTableModel {
    id: number;
    tipo_animal: string;
    numero_animal: string;
    sexo_animal: string;
    fecha_muerte: string;
    motivo_muerte: string;
    causa_categoria: string;
    peso_muerte: string;
    nombre: string;

    constructor(id: number, tipo_animal: string, numero_animal: string, sexo_animal: string, fecha_muerte: string, motivo_muerte: string, causa_categoria: string, peso_muerte: string, nombre: string) {
        this.id = id;
        this.tipo_animal = tipo_animal;
        this.numero_animal = numero_animal;
        this.sexo_animal = sexo_animal;
        this.fecha_muerte = fecha_muerte;
        this.motivo_muerte = motivo_muerte;
        this.causa_categoria = causa_categoria;
        this.peso_muerte = peso_muerte;
        this.nombre = nombre;
    }
}
export class DesteteDto {
    id: number;
    numero_cria: string;
    sexo: string;
    fecha_nacimiento: string;
    edad_meses: number;
    peso_cria: number;
    color_cria: string;
    nombre_madre: string;
    nombre_padre: string;

    constructor(id: number, numero_cria: string, sexo: string, fecha_nacimiento: string, edad_meses: number, peso_cria: number, color_cria: string, nombre_madre: string, nombre_padre: string) {
        this.id = id;
        this.numero_cria = numero_cria;
        this.sexo = sexo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.edad_meses = edad_meses;
        this.peso_cria = peso_cria;
        this.color_cria = color_cria;
        this.nombre_madre = nombre_madre;
        this.nombre_padre = nombre_padre;
    }
}
export interface UpdateCattleDto {
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
    tipo_animal:string;
    fecha_embarazo:string | null
}
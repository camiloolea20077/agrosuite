export interface UpdateCattleDeathDto {
    id: number;
    cattleId: number;
    birthId: number;
    fechaMuerte: string;
    motivoMuerte: string;
    descripcionDetallada: string;
    pesoMuerte: string;
    causaCategoria: string;
}
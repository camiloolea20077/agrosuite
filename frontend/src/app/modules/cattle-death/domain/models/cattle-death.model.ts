export class CattleDeathModel {
    id: number;
    cattleId: number;
    birthId: number;
    fechaMuerte: string;
    motivoMuerte: string;
    descripcionDetallada: string;
    pesoMuerte: string;
    causaCategoria: string;

    constructor(id: number, cattleId: number, birthId: number, fechaMuerte: string, motivoMuerte: string, descripcionDetallada: string, pesoMuerte: string, causaCategoria: string) {
        this.id = id;
        this.cattleId = cattleId;
        this.birthId = birthId;
        this.fechaMuerte = fechaMuerte;
        this.motivoMuerte = motivoMuerte;
        this.descripcionDetallada = descripcionDetallada;
        this.pesoMuerte = pesoMuerte;
        this.causaCategoria = causaCategoria;
    }
}
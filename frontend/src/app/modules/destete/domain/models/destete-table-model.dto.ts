export class DesteteTableDto {
    id: number;
    numero_cria: string;
    fecha_nacimiento: string; // LocalDate se maneja como string en frontend
    fecha_proxima_destete: string;
    dias_restantes: number;

    constructor(id: number, numero_cria: string, fecha_nacimiento: string, fecha_proxima_destete: string, dias_restantes: number) {
        this.id = id;
        this.numero_cria = numero_cria;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_proxima_destete = fecha_proxima_destete;
        this.dias_restantes = dias_restantes;
    }
}

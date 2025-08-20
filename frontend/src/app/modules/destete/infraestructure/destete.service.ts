import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { DesteteTableDto } from "../domain/models/destete-table-model.dto";
import { MigrarTerneroDto } from "../domain/dto/migrar-ternero.dto";
import { ResultadoMigracionDto } from "../domain/dto/resultado-migracion.dto";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: 'root'
})
export class DesteteService {
    // ========================= Tipos de Insumos =========================

     private apiUrl = `${environment.destetesURL}`; // Ajusta según tu configuración

    constructor(private http: HttpClient) { }

    /**
     * Obtiene la lista de terneros listos para destetar
     */
    obtenerTernerosParaDestetar(): Observable<ResponseModel<DesteteTableDto[]>> {
        return this.http.get<ResponseModel<DesteteTableDto[]>>(
            `${this.apiUrl}/terneros-listos`
        );
    }

    /**
     * Migra un ternero individual (cría o venta)
     */
    migrarTernero(migrarDto: MigrarTerneroDto): Observable<ResponseModel<ResultadoMigracionDto>> {
        return this.http.post<ResponseModel<ResultadoMigracionDto>>(
            `${this.apiUrl}/migrar`,
            migrarDto
        );
    }

    /**
     * Obtiene el historial de destetes
     */
    obtenerHistorialDestetes(): Observable<ResponseModel<any[]>> {
        return this.http.get<ResponseModel<any[]>>(
            `${this.apiUrl}/historial`
        );
    }

    /**
     * Migra múltiples terneros en lote
     */
    migrarLoteTerneros(migrarDtos: MigrarTerneroDto[]): Observable<ResponseModel<ResultadoMigracionDto[]>> {
        return this.http.post<ResponseModel<ResultadoMigracionDto[]>>(
            `${this.apiUrl}/migrar-lote`,
            migrarDtos
        );
    }
}
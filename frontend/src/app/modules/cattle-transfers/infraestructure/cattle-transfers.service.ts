import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { CattleTransferDto, CattleTransferModel, CreateCattleTransferDto } from "../domain/dto/create-cattle-transfer.dto";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { Observable } from "rxjs";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";
import { CattleTransfersTableModel } from "../domain/models/cattle-tranfers-table.model";
import { ICattleTransfersFilterTable } from "../domain/models/cattle-tranfers-filter-table.model";

@Injectable({ providedIn: 'root' })
export class CattleTransferService {
    private apiUrl = environment.cattleTransfers;

    constructor(private http: HttpClient) { }

    createTransfer(
        payload: CreateCattleTransferDto
    ): Observable<ResponseModel<CattleTransferModel>> {
        return this.http.post<ResponseModel<CattleTransferModel>>(
            `${this.apiUrl}/create`,
            payload
        );
    }
    pageTransfers(iFilterTable: IFilterTable<ICattleTransfersFilterTable>): Observable<ResponseTableModel<CattleTransfersTableModel>> {
        return this.http.post<ResponseTableModel<CattleTransfersTableModel>>(
            `${this.apiUrl}/page`,
            iFilterTable
        );
    }
    getCattleTransferById(id: number): Observable<ResponseModel<CattleTransferDto>> {
    return this.http.get<ResponseModel<CattleTransferDto>>(`${this.apiUrl}/${id}`);
    }
}
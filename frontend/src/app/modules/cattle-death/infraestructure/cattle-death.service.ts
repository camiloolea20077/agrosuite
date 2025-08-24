import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";
import { environment } from "src/environments/environment";
import { CattleDeathTableModel } from "../domain/models/cattle-death-table.model";
import { ICattleDeathFilterTable } from "../domain/models/cattle-death-filter-table.model";
import { CreateCattleDeathDto } from "../domain/dto/crate-cattle-death.dto";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { CattleDeathModel } from "../domain/models/cattle-death.model";

@Injectable({
    providedIn: "root",
})
export class CattleDeathService {

    private apiUrl = environment.cattleDeath

    constructor(private http: HttpClient) { }

    pageCattleDeath(iFilterTable: IFilterTable<ICattleDeathFilterTable>): Observable<ResponseTableModel<CattleDeathTableModel>> {
        return this.http.post<ResponseTableModel<CattleDeathTableModel>>(`${this.apiUrl}/page`, iFilterTable);
    }

    createCattleDeath(createDeath: CreateCattleDeathDto): Observable<ResponseModel<CattleDeathModel>> {
        return this.http.post<ResponseModel<CattleDeathModel>>(`${this.apiUrl}/create`, createDeath);
    }
    getCattleDeathById(id: number): Observable<ResponseModel<CattleDeathModel>> {
        return this.http.get<ResponseModel<CattleDeathModel>>(`${this.apiUrl}/${id}`);
    }
}
import { Injectable } from "@angular/core";
import { CattleService } from "../services/cattle.service";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { Observable } from "rxjs";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";
import { CattleTableModel } from "../models/cattle/cattle-table.model";
import { ICattleFilterTable } from "../models/cattle/cattle-filter-table.model";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { CreateCattleDto } from "../models/cattle/create-cattle.dto";
import { CattleModel } from "../models/cattle/cattle.models";
import { UpdateCattleDto } from "../models/cattle/update-cattle.dto";

@Injectable({
    providedIn: 'root',
})
export class CattleRepositoryImpl {

    constructor(private readonly cattleService: CattleService) { }
        pageClients(iFilterTable: IFilterTable<ICattleFilterTable>): Observable<ResponseTableModel<CattleTableModel>> {
            return this.cattleService.pageCattle(iFilterTable);
        }
        createCattle(
            createDentalPieces: CreateCattleDto
        ): Observable<ResponseModel<CattleModel>> {
            return this.cattleService.createCattle(createDentalPieces)
        }

        updateCattle(id: number, updateCattle: UpdateCattleDto): Observable<ResponseModel<UpdateCattleDto>> {
            return this.cattleService.updateCattle(id, updateCattle)
        }
        getCattleById(id: number): Observable<ResponseModel<CattleModel>> {
            return this.cattleService.getCattleById(id);
        }
        deleteCattle(id: number): Observable<ResponseModel<boolean>> {
            return this.cattleService.deleteCattle(id);
        }

}
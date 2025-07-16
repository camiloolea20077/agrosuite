import { Injectable } from "@angular/core";
import { BirthsService } from "../services/births.service";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { Observable } from "rxjs";
import { IBirthsFilterTable } from "src/app/modules/births/domain/models/births-filter-table.model";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";
import { BirthsTableModel } from "src/app/modules/births/domain/models/births-table.models";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { BirthsModel } from "src/app/modules/births/domain/models/births.model";
import { UpdateBirthsDto } from "src/app/modules/births/domain/dto/update-births.dto";
import { CreateBirthsDto } from "src/app/modules/births/domain/dto/create-births.dto";

@Injectable({
    providedIn: 'root',
})

export class BirthsRepositoryImpl {
    constructor(private readonly birthsService: BirthsService) { }

    pageClients(iFilterTable: IFilterTable<IBirthsFilterTable>): Observable<ResponseTableModel<BirthsTableModel>> {
        return this.birthsService.pageBirths(iFilterTable);
    }
    createBirth(
        createDentalPieces: CreateBirthsDto
    ): Observable<ResponseModel<BirthsModel>> {
        return this.birthsService.createBirth(createDentalPieces)
    }

    updateBirth(id: number, updateCattle: UpdateBirthsDto): Observable<ResponseModel<UpdateBirthsDto>> {
        return this.birthsService.updateBirth(id, updateCattle)
    }
    getBirthById(id: number): Observable<ResponseModel<BirthsModel>> {
        return this.birthsService.getBirthById(id);
    }
    deleteBirth(id: number): Observable<ResponseModel<boolean>> {
        return this.birthsService.deleteBirth(id);
    }
}
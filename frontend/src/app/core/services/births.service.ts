import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/app/environments/environment";
import { CreateBirthsDto } from "src/app/modules/births/domain/dto/create-births.dto";
import { UpdateBirthsDto } from "src/app/modules/births/domain/dto/update-births.dto";
import { IBirthsFilterTable } from "src/app/modules/births/domain/models/births-filter-table.model";
import { BirthsTableModel } from "src/app/modules/births/domain/models/births-table.models";
import { BirthsModel } from "src/app/modules/births/domain/models/births.model";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";

@Injectable({
    providedIn: 'root',
})
export class BirthsService {
    private apiUrl = environment.birthsUrl;

    constructor(private http: HttpClient) { }

    pageBirths(
        iFilterTable: IFilterTable<IBirthsFilterTable>
    ): Observable<ResponseTableModel<BirthsTableModel>> {
        return this.http.post<ResponseTableModel<BirthsTableModel>>(
            `${this.apiUrl}/page`,
            iFilterTable
        );
    }
    createBirth(createDentalPieces: CreateBirthsDto): Observable<ResponseModel<BirthsModel>>{
        return this.http.post<ResponseModel<BirthsModel>>(
            `${this.apiUrl}/create`,
            createDentalPieces
        )
    }
    updateBirth(id: number, updateBirth: UpdateBirthsDto): Observable<ResponseModel<UpdateBirthsDto>> {
        const updateBirthsDto: UpdateBirthsDto = { ...updateBirth, id };
        return this.http.put<ResponseModel<UpdateBirthsDto>>(
            `${this.apiUrl}/update`,
            updateBirthsDto
        );
    }
    getBirthById(id: number): Observable<ResponseModel<BirthsModel>> {
        return this.http.get<ResponseModel<BirthsModel>>(`${this.apiUrl}/${id}`);
    }
    deleteBirth(id: number): Observable<ResponseModel<boolean>> {
        return this.http.delete<ResponseModel<boolean>>(`${this.apiUrl}/${id}`);
    }
}
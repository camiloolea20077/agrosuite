import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { ResponseTableModel } from 'src/app/shared/utils/models/response-table.model';
import { CattleTableModel } from '../models/cattle/cattle-table.model';
import { ICattleFilterTable } from '../models/cattle/cattle-filter-table.model';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';
import { CreateCattleDto } from '../models/cattle/create-cattle.dto';
import { CattleModel } from '../models/cattle/cattle.models';
import { UpdateCattleDto } from '../models/cattle/update-cattle.dto';
import { environment } from 'src/app/environments/environment';

@Injectable({
    providedIn: 'root',
})
export class CattleService {
    private apiUrl = environment.cattleUrl;

    constructor(private http: HttpClient) { }

    pageCattle(
        iFilterTable: IFilterTable<ICattleFilterTable>
    ): Observable<ResponseTableModel<CattleTableModel>> {
        return this.http.post<ResponseTableModel<CattleTableModel>>(
            `${this.apiUrl}/page`,
            iFilterTable
        );
    }
    createCattle(createDentalPieces: CreateCattleDto): Observable<ResponseModel<CattleModel>>{
        return this.http.post<ResponseModel<CattleModel>>(
            `${this.apiUrl}/create`,
            createDentalPieces
        )
    }
    updateCattle(id: number, updateCattle: UpdateCattleDto): Observable<ResponseModel<UpdateCattleDto>> {
        return this.http.put<ResponseModel<UpdateCattleDto>>(
            `${this.apiUrl}/update`,
            updateCattle
        );
    }
    getCattleById(id: number): Observable<ResponseModel<CattleModel>> {
        return this.http.get<ResponseModel<CattleModel>>(`${this.apiUrl}/${id}`);
    }
    deleteCattle(id: number): Observable<ResponseModel<boolean>> {
        return this.http.delete<ResponseModel<boolean>>(`${this.apiUrl}/${id}`);
    }
}

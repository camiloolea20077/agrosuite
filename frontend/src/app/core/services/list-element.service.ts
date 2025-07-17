import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/app/environments/environment";
import { CreateBirthsDto } from "src/app/modules/births/domain/dto/create-births.dto";
import { BirthsModel } from "src/app/modules/births/domain/models/births.model";
import { ListElementFarmsModes } from "src/app/shared/utils/models/list-element-farms.model";
import { ListElementModel } from "src/app/shared/utils/models/list-element.model";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";

@Injectable({
    providedIn: 'root'
})
export class ListElementService {

    private apiUrl = environment.listElementUrl;

    constructor(private readonly http: HttpClient) { }


    forListById(): Observable<ResponseModel<ListElementModel[]>> {
        return this.http.get<ResponseModel<ListElementModel[]>>(
            `${this.apiUrl}/list-for-cattle-female`
        )
    }
    forListByMale(): Observable<ResponseModel<ListElementModel[]>> {
        return this.http.get<ResponseModel<ListElementModel[]>>(
            `${this.apiUrl}/list-for-cattle-male`
        )
    }
    forListByFarms(): Observable<ResponseModel<ListElementFarmsModes[]>> {
        return this.http.get<ResponseModel<ListElementFarmsModes[]>>(
            `${this.apiUrl}/list-farms`
        )
    }
}
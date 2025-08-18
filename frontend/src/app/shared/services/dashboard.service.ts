import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { DashboardData } from "../dto/dashboard.dto";
import { ResponseModel } from "../utils/models/responde.models";
import { environment } from "src/environments/environment";
import { DesteteTableDto } from "../dto/destete.dto";

@Injectable({
  providedIn: 'root',
})
export class DashboardService {

    private apiUrl = environment.dashboardUrl

    constructor(private http: HttpClient) { }

    getBirthsData(): Observable<ResponseModel<DashboardData>> {
        return this.http.get<ResponseModel<DashboardData>>(`${this.apiUrl}/data`);
    }
    getDesteteData(): Observable<ResponseModel<DesteteTableDto[]>> {
        return this.http.get<ResponseModel<DesteteTableDto[]>>(`${this.apiUrl}/destete`);
    }
}
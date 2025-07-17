import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { DashboardData } from "../dto/dashboard.dto";
import { ResponseModel } from "../utils/models/responde.models";
import { environment } from "src/app/environments/environment";

@Injectable({
  providedIn: 'root',
})
export class DashboardService {

    private apiUrl = environment.dashboardUrl

    constructor(private http: HttpClient) { }

    getBirthsData(): Observable<ResponseModel<DashboardData>> {
        return this.http.get<ResponseModel<DashboardData>>(`${this.apiUrl}/data`);
    }
}
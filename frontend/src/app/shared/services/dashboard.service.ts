import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { DashboardData } from "../dto/dashboard.dto";
import { ResponseModel } from "../utils/models/responde.models";

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
    private readonly apiUrl = 'http://localhost:9001/dashboard'
    constructor(private http: HttpClient) { };

    getBirthsData(): Observable<ResponseModel<DashboardData>> {
        return this.http.get<ResponseModel<DashboardData>>(`${this.apiUrl}/data`);
    }
}
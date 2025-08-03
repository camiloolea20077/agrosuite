import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";
import { environment } from "src/environments/environment";
import { CattleSalesTableModel } from "../domain/models/cattle-sales-table.models";
import { ICattleSalesFilterTable } from "../domain/models/cattle-sales-filter-table.models";
import { CreateCattleSaleDto } from "../domain/dto/create-cattle-sale.dto";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { CattleSaleModel } from "../domain/models/cattle-sales-models";
import { CattleSaleDto } from "../domain/dto/CattleSaleDto";

@Injectable({ providedIn: 'root' })
export class SalesService {
    private apiUrl = environment.salesUrl

    constructor(private http: HttpClient) { }

    
    pageSales(iFilterTable: IFilterTable<ICattleSalesFilterTable>): Observable<ResponseTableModel<CattleSalesTableModel>> {
        return this.http.post<ResponseTableModel<CattleSalesTableModel>>(
            `${this.apiUrl}/page`,
            iFilterTable
        )
    }
  createCattleSale(sale: CreateCattleSaleDto): Observable<ResponseModel<CattleSaleModel>> {
    return this.http.post<ResponseModel<CattleSaleModel>>(`${this.apiUrl}/create`, sale);
  }
    getCattleSaleById(id: number): Observable<ResponseModel<CattleSaleDto>> {
    return this.http.get<ResponseModel<CattleSaleDto>>(`${this.apiUrl}/${id}`);
    }

  confirmarVenta(id: number): Observable<ResponseModel<void>> {
    return this.http.put<ResponseModel<void>>(`${this.apiUrl}/${id}/confirmar`, null);
  }
  anularVenta(id: number): Observable<ResponseModel<void>> {
    return this.http.put<ResponseModel<void>>(`${this.apiUrl}/${id}/anular`, null);
  }

}
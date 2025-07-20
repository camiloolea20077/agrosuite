import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";
import { environment } from "src/environments/environment";
import { InventoryTableModel } from "../domain/models/inventory-table.models";
import { Observable } from "rxjs";
import { IInventoryFilterTable } from "../domain/models/inventory-filter-table.models";

@Injectable({ providedIn: 'root' })
export class InventoryService {
    
        private apiUrl = environment.inventoryUrl;
    
        constructor(private http: HttpClient) { }
    
        pageInventory(iFilterTable: IFilterTable<IInventoryFilterTable>): Observable<ResponseTableModel<InventoryTableModel>> {
            return this.http.post<ResponseTableModel<InventoryTableModel>>(`${this.apiUrl}/page`, iFilterTable);
        }
    
}
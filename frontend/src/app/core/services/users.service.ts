import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { IUsersFilterTable } from "src/app/modules/users/domain/models/users-filter-table.model";
import { UsersTableModel } from "src/app/modules/users/domain/models/users-table.model";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export class UsersService {
    private apiUrl = environment.usersUrl

    constructor(private http: HttpClient) { }

    pageClients(iFilterTable: IFilterTable<IUsersFilterTable>): Observable<ResponseTableModel<UsersTableModel>> {
        return this.http.post<ResponseTableModel<UsersTableModel>>(
            `${this.apiUrl}/page`,
            iFilterTable
        )
    }
}
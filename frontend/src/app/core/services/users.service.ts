import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { CreateEmployeesDto } from "src/app/modules/employees/domain/dto/create-employees.dto";
import { UpdateEmployeesDto } from "src/app/modules/employees/domain/dto/update-employees.dto";
import { CreateUsersDto } from "src/app/modules/users/domain/dto/create-users.dto";
import { UpdateUsersDto } from "src/app/modules/users/domain/dto/update-users.dto";
import { IUsersFilterTable } from "src/app/modules/users/domain/models/users-filter-table.model";
import { UsersTableModel } from "src/app/modules/users/domain/models/users-table.model";
import { UsersModels } from "src/app/modules/users/domain/models/users.model";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
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
    createUsers(employees: CreateUsersDto): Observable<ResponseModel<UsersModels>> {
        return this.http.post<ResponseModel<UsersModels>>(`${this.apiUrl}/create`, employees);
    }
    updateUsers(id: number, employees: CreateUsersDto): Observable<ResponseModel<UpdateUsersDto>> {
        const updateEmployeesDto: UpdateUsersDto = { ...employees, id };
        return this.http.put<ResponseModel<UpdateUsersDto>>(
            `${this.apiUrl}/update`,
            updateEmployeesDto
        )
    }
    deleteUsers(id: number): Observable<ResponseModel<boolean>> {
        return this.http.delete<ResponseModel<boolean>>(`${this.apiUrl}/${id}`);
    }

    getUsersById(id: number): Observable<ResponseModel<UsersModels>> {
        return this.http.get<ResponseModel<UsersModels>>(`${this.apiUrl}/${id}`);
    }
}
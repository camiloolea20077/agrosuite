import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { CreateEmployeesDto } from "src/app/modules/employees/domain/dto/create-employees.dto";
import { UpdateEmployeesDto } from "src/app/modules/employees/domain/dto/update-employees.dto";
import { IEmployeesFilterTable } from "src/app/modules/employees/domain/models/employees-filter-table.models";
import { EmployeesTableModel } from "src/app/modules/employees/domain/models/employees-table.models";
import { EmployeesModel } from "src/app/modules/employees/domain/models/employees.model";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";

@Injectable({
    providedIn: 'root'
})
export class EmployeesService {

    private apiUrl = environment.employeesUrl;

    constructor(private http: HttpClient) { }

    pageEmployees(iFilterTable: IFilterTable<IEmployeesFilterTable>): Observable<ResponseTableModel<EmployeesTableModel>> {
        return this.http.post<ResponseTableModel<EmployeesTableModel>>(`${this.apiUrl}/page`, iFilterTable);
    }

    createEmployees(employees: CreateEmployeesDto): Observable<ResponseModel<EmployeesModel>> {
        return this.http.post<ResponseModel<EmployeesModel>>(`${this.apiUrl}/create`, employees);
    }

    updateEmployees(id: number, employees: UpdateEmployeesDto): Observable<ResponseModel<UpdateEmployeesDto>> {
        const updateEmployeesDto: UpdateEmployeesDto = { ...employees, id };
        return this.http.put<ResponseModel<UpdateEmployeesDto>>(
            `${this.apiUrl}/update`, 
            updateEmployeesDto);
    }

    deleteEmployees(id: number): Observable<ResponseModel<boolean>> {
        return this.http.delete<ResponseModel<boolean>>(`${this.apiUrl}/${id}`);
    }

    getEmployeesById(id: number): Observable<ResponseModel<EmployeesModel>> {
        return this.http.get<ResponseModel<EmployeesModel>>(`${this.apiUrl}/${id}`);
    }
}
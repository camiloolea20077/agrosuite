import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { ClientDto } from "../models/clients/clients.model";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";
import { IClienteFilterTable } from "../models/clients/cliente-filter-table.model";
import { ClientsTableModel } from "../models/clients/clients-table.model";

@Injectable({
    providedIn: 'root',
  })
  export class ClientesService {
    private apiUrl = 'http://localhost:9001/clients/';
  
    constructor(private http: HttpClient) {}
  
    pageClients(iFilterTable: IFilterTable<IClienteFilterTable>
    ): Observable<ResponseTableModel<ClientsTableModel>> {
      return this.http.post<ResponseTableModel<ClientsTableModel>>(
        `${this.apiUrl}page`,
        iFilterTable
      )
    }
  
    createClient(client: ClientDto): Observable<ResponseModel<ClientDto>> {
      return this.http.post<ResponseModel<ClientDto>>(`${this.apiUrl}create`, client);
    }
  }
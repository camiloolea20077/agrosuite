import { Injectable } from "@angular/core";
import { IClienteFilterTable } from "../models/clients/cliente-filter-table.model";
import { Observable } from "rxjs";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";
import { ClientsTableModel } from "../models/clients/clients-table.model";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ClientesService } from "../services/clientes.service";

@Injectable({
    providedIn: 'root',
})
export class ClientsRepositoryImpl {

    constructor (private readonly clientsService: ClientesService) { }
    pageClients(iFilterTable: IFilterTable<IClienteFilterTable>): Observable<ResponseTableModel<ClientsTableModel>> {
        return this.clientsService.pageClients(iFilterTable);
    }
}
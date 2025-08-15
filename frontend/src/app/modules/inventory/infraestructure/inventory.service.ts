import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { ResponseTableModel } from 'src/app/shared/utils/models/response-table.model';
import { environment } from 'src/environments/environment';
import { InventoryTableModel } from '../domain/models/inventory-table.models';
import { Observable } from 'rxjs';
import { IInventoryFilterTable } from '../domain/models/inventory-filter-table.models';
import { IndexDBService } from 'src/app/core/services/index-db.service';
import {
  CreateInventoryDto,
  Inventory,
  InventoryListDto,
  InventoryTableDto,
  ReserveStockDto,
  UpdateStockDto,
} from '../domain/dto/inventory.interface';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';

@Injectable({ providedIn: 'root' })
export class InventoryService {
  private apiUrl = environment.inventoryUrl;

  constructor(
    private http: HttpClient,
    private indexDBService: IndexDBService
  ) {}

  private async getHeaders(): Promise<HttpHeaders> {
    const authData = await this.indexDBService.loadDataAuthDB();
    return new HttpHeaders({
      farmid: authData?.user?.farm?.toString() || '1',
      user: authData?.user?.id?.toString() || '1',
    });
  }

  pageInventory(
    iFilterTable: IFilterTable<IInventoryFilterTable>
  ): Observable<ResponseTableModel<InventoryTableDto>> {
    return this.http.post<ResponseTableModel<InventoryTableDto>>(
      `${this.apiUrl}/page`,
      iFilterTable
    );
  }

  createInventory(
    inventory: CreateInventoryDto
  ): Observable<ResponseModel<Inventory>> {
    return this.http.post<ResponseModel<Inventory>>(
      `${this.apiUrl}/create`,
      inventory
    );
  }

  updateInventory(
    id: number,
    inventory: CreateInventoryDto
  ): Observable<ResponseModel<boolean>> {
    const updateDto: Inventory = { ...inventory, id };
    return this.http.put<ResponseModel<boolean>>(
      `${this.apiUrl}/update`,
      updateDto
    );
  }

  deleteInventory(id: number): Observable<ResponseModel<boolean>> {
    return this.http.delete<ResponseModel<boolean>>(`${this.apiUrl}/${id}`);
  }

  getInventoryById(id: number): Observable<ResponseModel<Inventory>> {
    return this.http.get<ResponseModel<Inventory>>(`${this.apiUrl}/${id}`);
  }

  updateStock(stockDto: UpdateStockDto): Observable<ResponseModel<boolean>> {
    return this.http.post<ResponseModel<boolean>>(
      `${this.apiUrl}/update-stock`,
      stockDto
    );
  }

  reserveStock(
    reserveDto: ReserveStockDto
  ): Observable<ResponseModel<boolean>> {
    return this.http.post<ResponseModel<boolean>>(
      `${this.apiUrl}/reserve-stock`,
      reserveDto
    );
  }

  releaseReservedStock(
    releaseDto: ReserveStockDto
  ): Observable<ResponseModel<boolean>> {
    return this.http.post<ResponseModel<boolean>>(
      `${this.apiUrl}/release-reserved-stock`,
      releaseDto
    );
  }
  getInventory():Observable<ResponseModel<InventoryListDto[]>>{
    return this.http.get<ResponseModel<InventoryListDto[]>>(`${this.apiUrl}/active`);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable, from, map, switchMap } from 'rxjs';
import { IndexDBService } from 'src/app/core/services/index-db.service';
import { IFilterTableMovimientos, InventoryMovement, InventoryMovementTableModelDto } from '../domain/dto/inventory.interface';
import { ResponseTableModel } from 'src/app/shared/utils/models/response-table.model';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';

@Injectable({
  providedIn: 'root'
})
export class InventoryMovementService {
  private apiUrl = `${environment.apiUrl}inventory-movements`;

  constructor(
    private http: HttpClient,
    private indexDBService: IndexDBService
  ) {}
  
  pageInventory(
    iFilterTable: IFilterTable<IFilterTableMovimientos>
  ): Observable<ResponseTableModel<InventoryMovementTableModelDto>> {
    return this.http.post<ResponseTableModel<InventoryMovementTableModelDto>>(
      `${this.apiUrl}/page`,
      iFilterTable
    );
  }
  private getHeaders(): Observable<HttpHeaders> {
    return from(
      this.indexDBService.loadDataAuthDB().then(authData =>
        new HttpHeaders({
          'farmid': authData?.user?.farm?.toString() || '1',
          'user': authData?.user?.id?.toString() || '1'
        })
      )
    );
  }

  create(movement: InventoryMovement): Observable<InventoryMovement> {
    return new Observable(observer => {
      this.getHeaders().subscribe(headers => {
        this.http.post<InventoryMovement>(`${this.apiUrl}/create`, movement, { headers })
          .subscribe(observer);
      });
    });
  }

  update(movement: InventoryMovement): Observable<boolean> {
    return new Observable(observer => {
      this.getHeaders().subscribe(headers => {
        this.http.put<boolean>(`${this.apiUrl}/update`, movement, { headers })
          .subscribe(observer);
      });
    });
  }

getById(id: number): Observable<ResponseModel<InventoryMovement>> {
  return this.http.get<ResponseModel<InventoryMovement>>(`${this.apiUrl}/${id}`);
}
  getByInventory(inventoryId: number): Observable<InventoryMovement[]> {
    return new Observable(observer => {
      this.getHeaders().subscribe(headers => {
        this.http.get<InventoryMovement[]>(`${this.apiUrl}/by-inventory/${inventoryId}`, { headers })
          .subscribe(observer);
      });
    });
  }

  getByDateRange(startDate: string, endDate: string): Observable<InventoryMovement[]> {
    return this.getHeaders().pipe(
      switchMap(headers => {
        const params = new HttpParams()
          .set('start', startDate)
          .set('end', endDate);
        return this.http.get<any>(`${this.apiUrl}/by-dates`, { headers, params });
      }),
      map(resp => Array.isArray(resp.data) ? resp.data : [])
    );
  }


  getPendingApprovals(): Observable<InventoryMovement[]> {
    return new Observable(observer => {
      this.getHeaders().subscribe(headers => {
        this.http.get<InventoryMovement[]>(`${this.apiUrl}/pending-approvals`, { headers })
          .subscribe(observer);
      });
    });
  }

  approve(id: number): Observable<boolean> {
    return new Observable(observer => {
      this.getHeaders().subscribe(headers => {
        this.http.put<boolean>(`${this.apiUrl}/${id}/approve`, {}, { headers })
          .subscribe(observer);
      });
    });
  }

  reject(id: number, reason?: string): Observable<boolean> {
    return new Observable(observer => {
      this.getHeaders().subscribe(headers => {
        const params = reason ? new HttpParams().set('reason', reason) : new HttpParams();
        this.http.put<boolean>(`${this.apiUrl}/${id}/reject`, {}, { headers, params })
          .subscribe(observer);
      });
    });
  }

  delete(id: number): Observable<boolean> {
    return new Observable(observer => {
      this.getHeaders().subscribe(headers => {
        this.http.delete<boolean>(`${this.apiUrl}/${id}`, { headers })
          .subscribe(observer);
      });
    });
  }
}

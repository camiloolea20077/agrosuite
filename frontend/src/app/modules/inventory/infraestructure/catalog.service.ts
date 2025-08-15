import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ResponseTableModel } from 'src/app/shared/utils/models/response-table.model';
import { EstadoInventario, EstadoMovimiento, Supplier, TipoInsumo, TipoMovimiento } from '../domain/dto/inventory.interface';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';



@Injectable({
  providedIn: 'root'
})
export class InventoryCatalogService {
  private baseUrl = `${environment.apiUrl}`;
    private baseUrl2 = environment.tipoInsumosUrl
  constructor(private http: HttpClient) {}

  // ========================= Tipos de Insumos =========================
  pageTiposInsumos(): Observable<ResponseModel<TipoInsumo[]>> {
    return this.http.get<ResponseModel<TipoInsumo[]>>(`${this.baseUrl2}active`);
  }

  createTipoInsumo(tipo: Omit<TipoInsumo, 'id'>): Observable<ResponseModel<TipoInsumo>> {
    return this.http.post<ResponseModel<TipoInsumo>>(`${this.baseUrl2}create`, tipo);
  }

  updateTipoInsumo(id: number, tipo: Omit<TipoInsumo, 'id'>): Observable<ResponseModel<TipoInsumo>> {
    const updateDto = { ...tipo, id };
    return this.http.put<ResponseModel<TipoInsumo>>(`${this.baseUrl2}update`, updateDto);
  }

  deleteTipoInsumo(id: number): Observable<ResponseModel<boolean>> {
    return this.http.delete<ResponseModel<boolean>>(`${this.baseUrl2}${id}`);
  }

  getTipoInsumoById(id: number): Observable<ResponseModel<TipoInsumo>> {
    return this.http.get<ResponseModel<TipoInsumo>>(`${this.baseUrl2}${id}`);
  }

  // ========================= Estados de Inventario =========================
  pageEstadosInventario(): Observable<ResponseModel<EstadoInventario[]>> {
    return this.http.get<ResponseModel<EstadoInventario[]>>(`${this.baseUrl}estados-inventario/active`);
  }

  createEstadoInventario(estado: Omit<EstadoInventario, 'id'>): Observable<ResponseModel<EstadoInventario>> {
    return this.http.post<ResponseModel<EstadoInventario>>(`${this.baseUrl}estados-inventario/create`, estado);
  }

  updateEstadoInventario(id: number, estado: Omit<EstadoInventario, 'id'>): Observable<ResponseModel<EstadoInventario>> {
    const updateDto = { ...estado, id };
    return this.http.put<ResponseModel<EstadoInventario>>(`${this.baseUrl}estados-inventario/update`, updateDto);
  }

  deleteEstadoInventario(id: number): Observable<ResponseModel<boolean>> {
    return this.http.delete<ResponseModel<boolean>>(`${this.baseUrl}estados-inventario/${id}`);
  }

  getEstadoInventarioById(id: number): Observable<ResponseModel<EstadoInventario>> {
    return this.http.get<ResponseModel<EstadoInventario>>(`${this.baseUrl}estados-inventario/${id}`);
  }

  // ========================= Tipos de Movimientos =========================
  pageTiposMovimientos(): Observable<ResponseModel<TipoMovimiento[]>> {
    return this.http.get<ResponseModel<TipoMovimiento[]>>(`${this.baseUrl}tipos-movimientos/active`);
  }

  createTipoMovimiento(tipo: Omit<TipoMovimiento, 'id'>): Observable<ResponseModel<TipoMovimiento>> {
    return this.http.post<ResponseModel<TipoMovimiento>>(`${this.baseUrl}tipos-movimientos/create`, tipo);
  }

  updateTipoMovimiento(id: number, tipo: Omit<TipoMovimiento, 'id'>): Observable<ResponseModel<TipoMovimiento>> {
    const updateDto = { ...tipo, id };
    return this.http.put<ResponseModel<TipoMovimiento>>(`${this.baseUrl}tipos-movimientos/update`, updateDto);
  }

  deleteTipoMovimiento(id: number): Observable<ResponseModel<boolean>> {
    return this.http.delete<ResponseModel<boolean>>(`${this.baseUrl}tipos-movimientos/${id}`);
  }

  getTipoMovimientoById(id: number): Observable<ResponseModel<TipoMovimiento>> {
    return this.http.get<ResponseModel<TipoMovimiento>>(`${this.baseUrl}tipos-movimientos/${id}`);
  }

  // ========================= Estados de Movimientos =========================
  pageEstadosMovimientos(): Observable<ResponseModel<EstadoMovimiento[]>> {
    return this.http.get<ResponseModel<EstadoMovimiento[]>>(`${this.baseUrl}estados-movimientos/active`);
  }

  createEstadoMovimiento(estado: Omit<EstadoMovimiento, 'id'>): Observable<ResponseModel<EstadoMovimiento>> {
    return this.http.post<ResponseModel<EstadoMovimiento>>(`${this.baseUrl}estados-movimientos/create`, estado);
  }

  updateEstadoMovimiento(id: number, estado: Omit<EstadoMovimiento, 'id'>): Observable<ResponseModel<EstadoMovimiento>> {
    const updateDto = { ...estado, id };
    return this.http.put<ResponseModel<EstadoMovimiento>>(`${this.baseUrl}estados-movimientos/update`, updateDto);
  }

  deleteEstadoMovimiento(id: number): Observable<ResponseModel<boolean>> {
    return this.http.delete<ResponseModel<boolean>>(`${this.baseUrl}estados-movimientos/${id}`);
  }

  getEstadoMovimientoById(id: number): Observable<ResponseModel<EstadoMovimiento>> {
    return this.http.get<ResponseModel<EstadoMovimiento>>(`${this.baseUrl}estados-movimientos/${id}`);
  }

  // ========================= Proveedores =========================
  pageSuppliers(iFilterTable: IFilterTable<any>): Observable<ResponseTableModel<Supplier>> {
    return this.http.post<ResponseTableModel<Supplier>>(`${this.baseUrl}suppliers/page`, iFilterTable);
  }

  createSupplier(supplier: Omit<Supplier, 'id'>): Observable<ResponseModel<Supplier>> {
    return this.http.post<ResponseModel<Supplier>>(`${this.baseUrl}suppliers/create`, supplier);
  }

  updateSupplier(id: number, supplier: Omit<Supplier, 'id'>): Observable<ResponseModel<Supplier>> {
    const updateDto = { ...supplier, id };
    return this.http.put<ResponseModel<Supplier>>(`${this.baseUrl}suppliers/update`, updateDto);
  }

  deleteSupplier(id: number): Observable<ResponseModel<boolean>> {
    return this.http.delete<ResponseModel<boolean>>(`${this.baseUrl}suppliers/${id}`);
  }

  getSupplierById(id: number): Observable<ResponseModel<Supplier>> {
    return this.http.get<ResponseModel<Supplier>>(`${this.baseUrl}suppliers/${id}`);
  }
}

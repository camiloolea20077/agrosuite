import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { TableModule, TableLazyLoadEvent } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { lastValueFrom } from 'rxjs';
import { FormsModule } from '@angular/forms';

import { CattleService } from 'src/app/core/services/cattle.service';
import { CattleTableModel } from 'src/app/core/models/cattle/cattle-table.model';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { ICattleFilterTable } from 'src/app/core/models/cattle/cattle-filter-table.model';
import { CreateCattleSaleItemDto } from '../../../domain/dto/create-cattle-sale-item.dto';

@Component({
  selector: 'app-select-cattle-modal',
  standalone: true,
  imports: [
    CommonModule,
    DialogModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    FormsModule,
  ],
  templateUrl: './select-cattle-modal.component.html',
})
export class SelectCattleModalComponent {
  @Input() visible: boolean = false;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSelect = new EventEmitter<CreateCattleSaleItemDto[]>();
  display = false;
  cattles: CattleTableModel[] = [];
  selected: CattleTableModel[] = [];

  rowSize = 10;
  totalRecords = 0;
  loading = false;

  constructor(private cattleService: CattleService) {}

  async loadTable(event: TableLazyLoadEvent): Promise<void> {
    this.loading = true;

    const filters: IFilterTable<ICattleFilterTable> = {
      page: event.first ? Math.floor(event.first / (event.rows ?? 10)) : 0,
      rows: event.rows ?? 10,
      search: event.globalFilter ?? '',
      order: event.sortOrder === -1 ? 'desc' : 'asc',
      order_by: event.sortField ?? 'id',
    };

    try {
      const res = await lastValueFrom(this.cattleService.pageCattle(filters));
      this.cattles = res.data?.content ?? [];
      this.totalRecords = res.data?.totalElements ?? 0;
    } catch {
      this.cattles = [];
      this.totalRecords = 0;
    }

    this.loading = false;
  }

  handleAccept() {
    const mapped = this.selected.map((c) => this.mapToCreateDto(c));
    this.onSelect.emit(mapped);
    this.selected = [];
    this.handleClose();
  }

  handleClose() {
    this.onClose.emit();
    this.selected = [];
  }
  open() {
    this.display = true;
  }

  close() {
    this.display = false;
  }
  private mapToCreateDto(cattle: CattleTableModel): CreateCattleSaleItemDto {
    return {
      tipoOrigen: 'GANADO',
      idOrigen: cattle.id,
      pesoVenta: parseInt(cattle.peso, 10),
      precioKilo: 0,
      precioTotal: 0,
    };
  }
}

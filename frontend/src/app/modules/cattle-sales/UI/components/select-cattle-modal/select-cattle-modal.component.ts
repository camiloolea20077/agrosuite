import { ChangeDetectorRef, Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { TableModule, TableLazyLoadEvent, Table } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { lastValueFrom } from 'rxjs';
import { FormsModule } from '@angular/forms';

import { CattleService } from 'src/app/core/services/cattle.service';
import { CattleTableModel } from 'src/app/core/models/cattle/cattle-table.model';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { ICattleFilterTable } from 'src/app/core/models/cattle/cattle-filter-table.model';
import { CreateCattleSaleItemDto } from '../../../domain/dto/create-cattle-sale-item.dto';
import { DropdownModule } from 'primeng/dropdown';
import { BirthsTableModel } from 'src/app/modules/births/domain/models/births-table.models';
import { IBirthsFilterTable } from 'src/app/modules/births/domain/models/births-filter-table.model';
import { BirthsService } from 'src/app/core/services/births.service';

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
    DropdownModule
  ],
  templateUrl: './select-cattle-modal.component.html',
})
export class SelectCattleModalComponent {
  @Input() externalSelectedGanado: CattleTableModel[] = [];
  @Input() externalSelectedNacimiento: BirthsTableModel[] = [];
  @ViewChild('dtBirths') dtBirths!: Table;
  origenSeleccionado: 'GANADO' | 'TERNERO' = 'GANADO';
  @Input() visible: boolean = false;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSelect = new EventEmitter<CreateCattleSaleItemDto[]>();
  display = false;
  cattles: CattleTableModel[] = [];
  selected: CattleTableModel[] = [];
  births: BirthsTableModel[] = []
  selectedGanado: CattleTableModel[] = [];
  selectedNacimiento: BirthsTableModel[] = [];
  selectedGlobalGanado: CattleTableModel[] = [];
  selectedGlobalNacimiento: BirthsTableModel[] = [];
  filtersTable!: IFilterTable<IBirthsFilterTable>
  rowSize = 10;
  totalRecords = 0;
  loadingTable = false;
  origenes = ['GANADO', 'TERNERO'];


  constructor(private cattleService: CattleService,
    private birthsService: BirthsService,
    private cd: ChangeDetectorRef
  ) {}

  async loadTable(lazyTable: TableLazyLoadEvent): Promise<void> {
    this.loadingTable = true;
    this.filtersTable = this.prepareTableParams(lazyTable)
    try {
      const res = await lastValueFrom(this.cattleService.pageCattle(this.filtersTable));
      this.cattles = res.data?.content ?? [];
      this.totalRecords = res.data?.totalElements ?? 0;
          // ✅ Sincronizar selección visible con selección global
    this.selectedGanado = this.cattles.filter(cattle =>
      this.selectedGlobalGanado.some(sel => sel.id === cattle.id)
    );
    } catch {
      this.cattles = [];
      this.totalRecords = 0;
    }

    this.loadingTable = false;
  }
    async loadTableBirth(lazyTable: TableLazyLoadEvent): Promise<void> {
        this.loadingTable = true
        this.filtersTable = this.prepareTableParams(lazyTable)
        try {
            const response = await lastValueFrom(
                this.birthsService.pageBirths(this.filtersTable)
            )
            this.births = response.data?.content ?? []
            this.totalRecords = response.data?.totalElements ?? 0
                // ✅ Sincronizar selección visible con selección global
    this.selectedNacimiento = this.births.filter(birth =>
      this.selectedGlobalNacimiento.some(sel => sel.id === birth.id)
    );
            this.loadingTable = false
        } catch (error) {
            this.births = []
            this.totalRecords = 0
            this.loadingTable = false
        }
    }
      private prepareTableParams(
        lazyTable: TableLazyLoadEvent
    ): IFilterTable<IBirthsFilterTable> {
        this.rowSize = lazyTable.rows ?? this.rowSize
        const currentPage = lazyTable.first
            ? Math.floor(lazyTable.first / this.rowSize)
            : 0
        return {
            page: currentPage,
            rows: this.rowSize,
            search: lazyTable.globalFilter,
            order: lazyTable.sortOrder === -1 ? 'desc' : 'asc',
            order_by: lazyTable.sortField ?? 'id',
        }
    }
  handleAccept() {
    let mapped: CreateCattleSaleItemDto[] = [];

    if (this.origenSeleccionado === 'GANADO') {
      mapped = this.selectedGlobalGanado.map((animal: CattleTableModel) =>
        this.mapGanadoToCreateDto(animal)
      );
    } else {
      mapped = this.selectedGlobalNacimiento.map((animal: BirthsTableModel) =>
        this.mapNacimientoToCreateDto(animal)
      );
    }

    this.onSelect.emit(mapped);
    this.selectedGanado = [];
    this.selectedNacimiento = [];
    this.handleClose();
  }


  handleClose() {
    this.onClose.emit();
  }
  open() {
    this.display = true;
  }

  close() {
    this.display = false;
  }
private mapGanadoToCreateDto(cattle: CattleTableModel): CreateCattleSaleItemDto {
  return {
    tipoOrigen: 'GANADO',
    idOrigen: cattle.id,
    pesoVenta: parseInt(cattle.peso, 10),
    precioKilo: 0,
    precioTotal: 0,
    numero_ganado: cattle.numero_ganado,
  };
}

private mapNacimientoToCreateDto(birth: BirthsTableModel): CreateCattleSaleItemDto {
  return {
    tipoOrigen: 'TERNERO',
    idOrigen: birth.id,
    pesoVenta: parseInt(birth.peso_cria, 10),
    precioKilo: 0,
    precioTotal: 0,
    numero_ganado: birth.numero_cria,
  };
}


  onOrigenChange(): void {
    this.selected = [];
    this.totalRecords = 0;

    const defaultEvent: TableLazyLoadEvent = {
      first: 0,
      rows: this.rowSize,
      globalFilter: '',
      sortOrder: 1,
      sortField: 'id',
    };
    if (this.origenSeleccionado === 'GANADO') {
      this.loadTable(defaultEvent);
    } else if (this.origenSeleccionado === 'TERNERO') {
      this.cd.detectChanges();
      setTimeout(() => {
        this.loadTableBirth(defaultEvent);
      }, 0);
    }
  }
  handleRowSelectGanado(event: any) {
    const alreadySelected = this.selectedGlobalGanado.find(a => a.id === event.data.id);
    if (!alreadySelected) {
      this.selectedGlobalGanado.push(event.data);
    }
  }

  handleRowUnselectGanado(event: any) {
    this.selectedGlobalGanado = this.selectedGlobalGanado.filter(a => a.id !== event.data.id);
  }

  handleRowSelectNacimiento(event: any) {
    const alreadySelected = this.selectedGlobalNacimiento.find(a => a.id === event.data.id);
    if (!alreadySelected) {
      this.selectedGlobalNacimiento.push(event.data);
    }
  }

  handleRowUnselectNacimiento(event: any) {
    this.selectedGlobalNacimiento = this.selectedGlobalNacimiento.filter(a => a.id !== event.data.id);
  }
  onSelectionChangeGanado(currentPageSelection: CattleTableModel[]) {
    const idsSeleccionados = new Set(currentPageSelection.map(item => item.id));
    const nuevos = currentPageSelection.filter(item =>
      !this.selectedGlobalGanado.some(s => s.id === item.id)
    );

    // Añadir los nuevos
    this.selectedGlobalGanado = [...this.selectedGlobalGanado, ...nuevos];

    // Eliminar los que ya no están seleccionados en esta página
    this.cattles.forEach(item => {
      if (!idsSeleccionados.has(item.id)) {
        this.selectedGlobalGanado = this.selectedGlobalGanado.filter(s => s.id !== item.id);
      }
    });
  }
  onSelectionChangeNacimiento(currentPageSelection: BirthsTableModel[]) {
    const idsSeleccionados = new Set(currentPageSelection.map(item => item.id));
    const nuevos = currentPageSelection.filter(item =>
      !this.selectedGlobalNacimiento.some(s => s.id === item.id)
    );

    // Añadir los nuevos
    this.selectedGlobalNacimiento = [...this.selectedGlobalNacimiento, ...nuevos];

    // Eliminar los que ya no están seleccionados en esta página
    this.births.forEach(item => {
      if (!idsSeleccionados.has(item.id)) {
        this.selectedGlobalNacimiento = this.selectedGlobalNacimiento.filter(s => s.id !== item.id);
      }
    });
  }

}

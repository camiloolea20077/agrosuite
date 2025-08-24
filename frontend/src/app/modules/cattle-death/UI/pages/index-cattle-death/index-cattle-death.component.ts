import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputTextModule } from 'primeng/inputtext';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ToastModule } from 'primeng/toast';
import { CattleDeathTableModel } from '../../../domain/models/cattle-death-table.model';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { ICattleDeathFilterTable } from '../../../domain/models/cattle-death-filter-table.model';
import { ColsModel } from 'src/app/shared/utils/models/cols.model';
import { HelpersService } from 'src/app/shared/utils/pipes/helper.service';
import { CattleDeathService } from '../../../infraestructure/cattle-death.service';
import { lastValueFrom } from 'rxjs';
import { FormCattleDeathComponent } from "../../components/form-cattle-death/form-cattle-death.component";
import { CattleDeathModel } from '../../../domain/models/cattle-death.model';

@Component({
  selector: 'app-index-cattle-death',
  standalone: true,
  templateUrl: './index-cattle-death.component.html',
  styleUrls: ['./index-cattle-death.component.scss'],
  providers: [MessageService, ConfirmationService],
  imports: [
    FormsModule,
    RouterModule,
    InputTextModule,
    ConfirmDialogModule,
    ProgressSpinnerModule,
    TagModule,
    ButtonModule,
    ReactiveFormsModule,
    TableModule,
    CommonModule,
    ToastModule,
    FormCattleDeathComponent
],
})
export class IndexCattleDeathComponent {
  showCattleModal = false;
  selectedCattleId: number | null = null;
  modalSlug: string = 'create';
  public rowSize = 10;
  public totalRecords = 0;
  public loadingTable = true;
  globalFilter: string = '';
  cattleDeath: CattleDeathTableModel[] = [];
  filtersTable!: IFilterTable<ICattleDeathFilterTable>;
  cols: ColsModel[] = [
    {
      field: 'tipo_animal',
      header: 'Tipo Animal',
      type: 'string',
      nameClass: 'text-center',
    },
    {
      field: 'numero_animal',
      header: 'Animal',
      type: 'string',
      nameClass: 'text-center',
    },
    {
      field: 'sexo_animal',
      header: 'Sexo',
      type: 'string',
      nameClass: 'text-center',
    },
    {
      field: 'fecha_muerte',
      header: 'Fecha Muerte',
      type: 'string',
      nameClass: 'text-center',
      minWidth: 'min-width: 180px;',
    },
    {
      field: 'motivo_muerte',
      header: 'Motivo Muerte',
      type: 'string',
      nameClass: 'text-left',
      minWidth: 'min-width: 40px;',
    },
    {
      field: 'causa_categoria',
      header: 'Causa',
      type: 'string',
      nameClass: 'text-left',
    },
    {
      field: 'peso_muerte',
      header: 'Peso Muerte',
      type: 'string',
      nameClass: 'text-left',
    },
    {
      field: 'nombre',
      header: 'Usuario',
      type: 'string',
      nameClass: 'text-left',
    },
  ];
  constructor(
    readonly _helperService: HelpersService,
    private readonly _confirmationService: ConfirmationService,
    private cattleDeathService: CattleDeathService,
    private router: Router,
    private messageService: MessageService
  ) {}
  ngOnInit(): void {
    this.loadTable;
    this.loadColumnActions();
  }
  async loadTable(lazyTable: TableLazyLoadEvent): Promise<void> {
    this.loadingTable = true;
    this.filtersTable = this.prepareTableParams(lazyTable);

    try {
      const response = await lastValueFrom(
        this.cattleDeathService.pageCattleDeath(this.filtersTable)
      );
      this.cattleDeath = response.data?.content ?? [];
      this.totalRecords = response.data?.totalElements ?? 0;
      this.loadingTable = false;
    } catch (error) {
      this.cattleDeath = [];
      this.totalRecords = 0;
      this.loadingTable = false;
    }
  }
  private prepareTableParams(
    lazyTable: TableLazyLoadEvent
  ): IFilterTable<ICattleDeathFilterTable> {
    this.rowSize = lazyTable.rows ?? this.rowSize;
    const currentPage = lazyTable.first
      ? Math.floor(lazyTable.first / this.rowSize)
      : 0;
    return {
      page: currentPage,
      rows: this.rowSize,
      search: lazyTable.globalFilter,
      order: lazyTable.sortOrder === -1 ? 'desc' : 'asc',
      order_by: lazyTable.sortField ?? 'id',
    };
  }
  async loadColumnActions(): Promise<void> {
    const columnAction = await this._helperService.showActionsTable();
    if (columnAction) {
      this.cols.push(columnAction);
    }
  }
  
  filterGlobal(event: Event) {
    this.loadTable({
      first: 0,
      rows: this.rowSize,
      globalFilter: (event.target as HTMLInputElement)?.value ?? '',
    });
  }
    // Método para obtener íconos de columnas
  getColumnIcon(field: string): string {
    const iconMap: { [key: string]: string } = {
      tipo_animal: 'pi pi-hashtag',
      numero_animal: 'pi pi-hashtag',
      sexo_animal: 'pi pi-tag',
      causa_categoria: 'pi pi-tags',
      peso_muerte: 'pi pi-tags',
      fecha_muerte: 'pi pi-calendar',
      lote_ganado: 'pi pi-chart-line',
      motivo_muerte: 'pi pi-comment',
      nombre: 'pi pi-user',
      activo: 'pi pi-power-off',
    };
    return iconMap[field] || 'pi pi-circle';
  }
  getTipoAnimalSeverity(tipoAnimal: string): string {
    switch (tipoAnimal?.toLowerCase()) {
      case 'vaca':
        return 'success';
      case 'toro':
        return 'danger';
      case 'ternero':
      case 'ternera':
        return 'info';
      default:
        return 'secondary';
    }
  }
    getTipoAnimalIcon(tipoAnimal: string): string {
    switch (tipoAnimal?.toLowerCase()) {
      case 'vaca':
        return 'pi pi-heart';
      case 'ternero':
        return 'pi pi-star';
      case 'ternero':
      case 'ternera':
        return 'pi pi-circle';
      default:
        return 'pi pi-bookmark';
    }
  }
  obtenerValorMostrar(valor: string | number | null | undefined): string {
    if (valor === null || valor === undefined || valor === '') {
      return '';
    }
    const valorString = String(valor);
    return valorString.toLowerCase().replace(/\b\w/g, (l) => l.toUpperCase());
  }
    onModalClosed(): void {
    this.showCattleModal = false;
    this.selectedCattleId = null;
  }
    onCattleDeathSaved(cattleDeath: CattleDeathModel): void {
        this.messageService.add({
            severity: 'success',
            summary: 'Éxito',
            detail: this.modalSlug === 'create' 
                ? 'Registro de muerte creado correctamente' 
                : 'Registro de muerte actualizado correctamente'
        });
        
        // Recargar la tabla
        this.loadTable({ first: 0, rows: this.rowSize });
        
        // Cerrar modal
        this.onModalClosed();
  }
      // Métodos para manejar el modal
    openCreateModal(): void {
        this.selectedCattleId = null;
        this.modalSlug = 'create';
        this.showCattleModal = true;
    }
  openEditModal(cattle: CattleDeathTableModel): void {
    this.modalSlug = 'edit';
    console.log(cattle);
    this.selectedCattleId = cattle.id;
    console.log(this.selectedCattleId);
    this.showCattleModal = true;
  }
}

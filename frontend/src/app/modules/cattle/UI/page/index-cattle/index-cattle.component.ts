import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { lastValueFrom } from 'rxjs';
import { ICattleFilterTable } from 'src/app/core/models/cattle/cattle-filter-table.model';
import { CattleTableModel } from 'src/app/core/models/cattle/cattle-table.model';
import { CattleService } from 'src/app/core/services/cattle.service';
import { ColsModel } from 'src/app/shared/utils/models/cols.model';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { HelpersService } from 'src/app/shared/utils/pipes/helper.service';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TagModule } from 'primeng/tag';
import { FormCattleComponent } from '../../components/form/form-cattle.component';
import { CattleModel } from 'src/app/core/models/cattle/cattle.models';

@Component({
  selector: 'app-index-cattle',
  standalone: true,
  templateUrl: './index-cattle.component.html',
  styleUrls: ['./index-cattle.component.scss'],
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
    FormCattleComponent,
  ],
})
export class IndexCattleComponent {
  // Variables para el modal
  showCattleModal = false;
  selectedCattleId: number | null = null;
  modalSlug: string = 'create';
  public rowSize = 10;
  public totalRecords = 0;
  public loadingTable = true;
  cattles: CattleTableModel[] = [];
  filtersTable!: IFilterTable<ICattleFilterTable>;
  cols: ColsModel[] = [
    {
      field: 'tipo_animal',
      header: 'Tipo Animal',
      type: 'string',
      nameClass: 'text-center',
    },
    {
      field: 'embarazo',
      header: 'Embarazada',
      type: 'string',
      nameClass: 'text-center',
    },
    {
      field: 'tipo_ganado',
      header: 'Tipo Ganado',
      type: 'string',
      nameClass: 'text-center',
    },
    {
      field: 'numero_ganado',
      header: '#Ganado',
      type: 'string',
      nameClass: 'text-center',
      minWidth: 'min-width: 180px;',
    },
    {
      field: 'peso',
      header: 'Peso',
      type: 'string',
      nameClass: 'text-left',
      minWidth: 'min-width: 40px;',
    },
    {
      field: 'fecha_nacimiento',
      header: 'Fecha Ingreso',
      type: 'string',
      nameClass: 'text-left',
    },
    {
      field: 'lote_ganado',
      header: 'Lote Ganado',
      type: 'string',
      nameClass: 'text-left',
    },
    {
      field: 'fecha_embarazo',
      header: 'Fecha Embarazo',
      type: 'string',
      nameClass: 'text-left',
    },
    {
      field: 'activo',
      header: 'Activo',
      type: 'icon',
      nameClass: 'text-left',
    },
  ];
  constructor(
    private fb: FormBuilder,
    readonly _helperService: HelpersService,
    private readonly _confirmationService: ConfirmationService,
    private cattleService: CattleService,
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
        this.cattleService.pageCattle(this.filtersTable)
      );
      this.cattles = response.data?.content ?? [];
      this.totalRecords = response.data?.totalElements ?? 0;
      this.loadingTable = false;
    } catch (error) {
      this.cattles = [];
      this.totalRecords = 0;
      this.loadingTable = false;
    }
  }
  private prepareTableParams(
    lazyTable: TableLazyLoadEvent
  ): IFilterTable<ICattleFilterTable> {
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
  async deleteCattle(id: number): Promise<void> {
    this._confirmationService.confirm({
      message: '¿Estás seguro de eliminar este ganado?',
      header: 'Eliminar Registro',
      icon: 'pi pi-exclamation-triangle',
      accept: async () => {
        try {
          const response = await lastValueFrom(
            this.cattleService.deleteCattle(id)
          );
          if (response.status === 200) {
            this.messageService.add({
              severity: 'success',
              summary: 'Éxito',
              detail: 'Ganado eliminado correctamente',
            });
            this.loadTable({ first: 0, rows: this.rowSize });
          }
        } catch (error) {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'No se pudo eliminar el ganado',
          });
        }
      },
    });
  }
  // Métodos adicionales para el componente de ganado

  // Variables adicionales que necesitarás
  globalFilter: string = '';
  selectedCattle: any = null;

  // Método para obtener campos de filtro global
  getFilterFields(): string[] {
    return this.cols
      .filter((col) => col.field !== 'actions')
      .map((col) => col.field);
  }

  // Método para manejar filtro global
  onGlobalFilter(event: any): void {
    this.globalFilter = event.target.value;
    // Aquí puedes implementar la lógica de filtrado
    // Si usas lazy loading, podrías disparar una nueva búsqueda
  }

  // Método para limpiar filtros
  clearFilter(): void {
    this.globalFilter = '';
    // Recargar la tabla sin filtros
    this.loadTable({ first: 0, rows: this.rowSize });
  }

  // Método para obtener íconos de columnas
  getColumnIcon(field: string): string {
    const iconMap: { [key: string]: string } = {
      tipo_animal: 'pi pi-hashtag',
      numero_ganado: 'pi pi-user',
      tipo_ganado: 'pi pi-tag',
      embarazo: 'pi pi-tags',
      peso: 'pi pi-tags',
      fecha_nacimiento: 'pi pi-calendar',
      lote_ganado: 'pi pi-chart-line',
      fecha_embarazo: 'pi pi-calendar',
      observaciones: 'pi pi-comment',
      estado: 'pi pi-circle',
      activo: 'pi pi-power-off',
    };
    return iconMap[field] || 'pi pi-circle';
  }

  // Método para obtener etiqueta de estado
  getEstadoLabel(activo: number): string {
    switch (activo) {
      case 1:
        return 'Activo';
      case 2:
        return 'Inactivo';
      default:
        return 'Desconocido';
    }
  }

  // Método para obtener severidad de estado
  getEstadoSeverity(activo: number): string {
    switch (activo) {
      case 1:
        return 'success';
      case 2:
        return 'danger';
      default:
        return 'info';
    }
  }
  // Métodos para embarazo
  getEmbarazoLabel(embarazo: number): string {
    switch (embarazo) {
      case 1:
        return 'Confirmado';
      case 2:
        return 'Por confirmar';
      case 0:
      default:
        return 'No embarazada';
    }
  }

  getEmbarazoSeverity(embarazo: number): string {
    switch (embarazo) {
      case 1:
        return 'success';
      case 2:
        return 'warning';
      case 0:
      default:
        return 'secondary';
    }
  }

  getEmbarazoIcon(embarazo: number): string {
    switch (embarazo) {
      case 1:
        return 'pi pi-check';
      case 2:
        return 'pi pi-question';
      case 0:
      default:
        return 'pi pi-times';
    }
  }

  getTipoAnimalIcon(tipoAnimal: string): string {
    switch (tipoAnimal?.toLowerCase()) {
      case 'vaca':
        return 'pi pi-heart';
      case 'toro':
        return 'pi pi-star';
      case 'ternero':
      case 'ternera':
        return 'pi pi-circle';
      default:
        return 'pi pi-bookmark';
    }
  }
  // Métodos para tipo de animal
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
  filterGlobal(event: Event) {
    this.loadTable({
      first: 0,
      rows: this.rowSize,
      globalFilter: (event.target as HTMLInputElement)?.value ?? '',
    });
  }
  // Métodos para el modal
  openCreateModal(): void {
    this.modalSlug = 'create';
    this.selectedCattleId = null;
    this.showCattleModal = true;
  }

  openEditModal(cattle: CattleTableModel): void {
    this.modalSlug = 'edit';
    this.selectedCattleId = cattle.id;
    this.showCattleModal = true;
  }

  onModalClosed(): void {
    this.showCattleModal = false;
    this.selectedCattleId = null;
  }

  onCattleSaved(cattle: CattleModel): void {
    // Recargar la tabla después de guardar
    this.loadTable({ first: 0, rows: this.rowSize });
    this.showCattleModal = false;
  }
  // Agrega este método a tu clase IndexCattleComponent con el tipo correcto
  obtenerFechaEmbarazo(item: CattleTableModel): string {
    // Si no es una vaca, mostrar N/A
    if (item.tipo_animal?.toLowerCase() !== 'vaca') {
      return 'N/A';
    }

    // Si es vaca pero no está embarazada (embarazo = 0), mostrar "No tiene"
    if (item.embarazo === 0) {
      return 'No tiene';
    }

    // Si está embarazada pero no tiene fecha, mostrar "No tiene"
    if (
      !item.fecha_embarazo ||
      item.fecha_embarazo === '' ||
      item.fecha_embarazo === null
    ) {
      return 'No tiene';
    }

    // Si tiene fecha, mostrarla
    return item.fecha_embarazo;
  }

  // Método alternativo con formateo de fecha
  obtenerFechaEmbarazoFormateada(item: CattleTableModel): string {
    // Si no es una vaca, mostrar N/A
    if (item.tipo_animal?.toLowerCase() !== 'vaca') {
      return 'N/A';
    }

    // Si es vaca pero no está embarazada (embarazo = 0), mostrar "No tiene"
    if (item.embarazo === 0) {
      return 'No tiene';
    }

    // Si está embarazada pero no tiene fecha, mostrar "No tiene"
    if (
      !item.fecha_embarazo ||
      item.fecha_embarazo === '' ||
      item.fecha_embarazo === null
    ) {
      return 'No tiene';
    }

    // Si tiene fecha, formatearla (ejemplo: de '2025-08-18' a '18/08/2025')
    try {
      const fecha = new Date(item.fecha_embarazo);
      return fecha.toLocaleDateString('es-ES'); // Formato español
    } catch (error) {
      return item.fecha_embarazo; // Si hay error en el formato, mostrar como está
    }
  }

  // También actualiza el método obtenerValorMostrar para que use el tipo correcto
  obtenerValorMostrar(valor: string | number | null | undefined): string {
    if (valor === null || valor === undefined || valor === '') {
      return '';
    }

    // Convierte a string primero, luego aplica título
    const valorString = String(valor);

    // Aplica la lógica de TitleCase manualmente
    return valorString.toLowerCase().replace(/\b\w/g, (l) => l.toUpperCase());
  }
}

import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { EstadoInventario } from '../../../domain/dto/inventory.interface';
import { InventoryCatalogService } from '../../../infraestructure/catalog.service';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { DropdownModule } from 'primeng/dropdown';
import { TagModule } from 'primeng/tag';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TooltipModule } from 'primeng/tooltip';
import { ToolbarModule } from 'primeng/toolbar';
import { ToastModule } from 'primeng/toast';
import { InventoryStateTableModel } from '../../../domain/models/inventory-state-table.models';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { IFilterInventoryStateTableModel } from '../../../domain/models/inventory-state-filter.models';
import { ColsModel } from 'src/app/shared/utils/models/cols.model';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-estados-inventario',
  standalone: true,
  templateUrl: './estados-inventario.component.html',
  styleUrls: ['./estados-inventario.component.scss'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonModule,
    TableModule,
    ToolbarModule,
    DialogModule,
    InputTextModule,
    InputTextareaModule,
    DropdownModule,
    TagModule,
    ConfirmDialogModule,
    TooltipModule,
    ToastModule,
  ],
  providers: [ConfirmationService, MessageService],
})
export class EstadosInventarioComponent implements OnInit {
  estadosInventario: EstadoInventario[] = [];
  estadosInventarioTable: InventoryStateTableModel[] = [];
  filterTable!: IFilterTable<IFilterInventoryStateTableModel>;
  displayDialog = false;
  displayDeleteDialog = false;
  estadoInventarioForm!: FormGroup;
  isEditing = false;
  public totalRecords = 0;
  selectedEstadoInventario: EstadoInventario | null = null;
  loading = false;
  public rowSize = 10;
  submitting = false;
  cols: ColsModel[] = [
    { field: 'codigo', header: 'Código',type: 'string' },
    { field: 'nombre', header: 'Nombre' ,type: 'string'},
    { field: 'descripcion', header: 'Descripción' ,type: 'string'},
    { field: 'activo', header: 'Activo' ,type: 'icon'},
    { field: 'actions', header: 'Acciones', type: 'actions' },
  ];
  constructor(
    private inventoryCatalogService: InventoryCatalogService,
    private fb: FormBuilder,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {
    this.initForm();
  }

  ngOnInit() {
    this.loadTable
  }

  async loadTable(lazyTable: TableLazyLoadEvent): Promise<void> {
    this.loading = true;
    this.filterTable = this.prepareTableParams(lazyTable);

    try {
      const response = await lastValueFrom(
        this.inventoryCatalogService.pageEstadosInventarioTable(this.filterTable)
      );
      this.estadosInventarioTable = response.data?.content ?? [];
      this.totalRecords = response.data?.totalElements ?? 0;
      this.loading = false;
    } catch (error) {
      this.estadosInventarioTable = [];
      this.totalRecords = 0;
      this.loading = false;
    }
  }
  private prepareTableParams(lazyTable: TableLazyLoadEvent): IFilterTable<IFilterInventoryStateTableModel> {
    this.rowSize = lazyTable.rows ?? this.rowSize ?? 10;
    const first = lazyTable.first ?? 0;
    const currentPage = Math.floor(first / this.rowSize);
    return {
      page: currentPage,
      rows: this.rowSize,
      search: lazyTable.globalFilter,
      order: lazyTable.sortOrder === -1 ? 'desc' : 'asc',
      order_by: lazyTable.sortField ?? 'id',
    };
  }
  private initForm() {
    this.estadoInventarioForm = this.fb.group({
      codigo: ['', [Validators.required, Validators.maxLength(20)]],
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      descripcion: ['', Validators.maxLength(500)],
      activo: [1, Validators.required],
    });
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
      codigo: 'pi pi-hashtag',
      numero_ganado: 'pi pi-user',
      nombre: 'pi pi-tag',
      descripcion: 'pi pi-tags',
      activo: 'pi pi-power-off',
    };
    return iconMap[field] || 'pi pi-circle';
  }

  loadEstadosInventario() {
    this.loading = true;
    this.inventoryCatalogService.pageEstadosInventario().subscribe({
      next: (response) => {
        this.estadosInventario = response.data || [];
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading estados inventario:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudieron cargar los estados de inventario',
        });
        this.loading = false;
      },
    });
  }

  openNew() {
    this.isEditing = false;
    this.selectedEstadoInventario = null;
    this.estadoInventarioForm.reset();
    this.estadoInventarioForm.patchValue({ activo: 1 });
    this.displayDialog = true;
  }

  editEstadoInventario(estadoInventario: EstadoInventario) {
    this.isEditing = true;
    this.selectedEstadoInventario = { ...estadoInventario };
    this.estadoInventarioForm.patchValue({
      codigo: estadoInventario.codigo,
      nombre: estadoInventario.nombre,
      descripcion: estadoInventario.descripcion,
      activo: estadoInventario.activo,
    });
    this.displayDialog = true;
  }

  deleteEstadoInventario(estadoInventario: EstadoInventario) {
    this.confirmationService.confirm({
      message: `¿Está seguro que desea eliminar el estado de inventario "${estadoInventario.nombre}"?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.performDelete(estadoInventario.id ?? 0);
      },
    });
  }

  private performDelete(id: number) {
    this.inventoryCatalogService.deleteEstadoInventario(id).subscribe({
      next: (response) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Estado de inventario eliminado correctamente',
        });
        this.loadEstadosInventario();
      },
      error: (error) => {
        console.error('Error deleting estado inventario:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudo eliminar el estado de inventario',
        });
      },
    });
  }

  saveEstadoInventario() {
    if (this.estadoInventarioForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.submitting = true;
    const formValue = this.estadoInventarioForm.value;

    if (
      this.isEditing &&
      this.selectedEstadoInventario &&
      this.selectedEstadoInventario.id !== undefined
    ) {
      this.inventoryCatalogService
        .updateEstadoInventario(this.selectedEstadoInventario.id, formValue)
        .subscribe({
          next: (response) => {
            this.messageService.add({
              severity: 'success',
              summary: 'Éxito',
              detail: 'Estado de inventario actualizado correctamente',
            });
            this.displayDialog = false;
            this.loadEstadosInventario();
            this.submitting = false;
          },
          error: (error) => {
            console.error('Error updating estado inventario:', error);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail:
                error.error?.message ||
                'No se pudo actualizar el estado de inventario',
            });
            this.submitting = false;
          },
        });
    } else {
      this.inventoryCatalogService.createEstadoInventario(formValue).subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Estado de inventario creado correctamente',
          });
          this.displayDialog = false;
          this.loadEstadosInventario();
          this.submitting = false;
        },
        error: (error) => {
          console.error('Error creating estado inventario:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail:
              error.error?.message ||
              'No se pudo crear el estado de inventario',
          });
          this.submitting = false;
        },
      });
    }
  }

  hideDialog() {
    this.displayDialog = false;
    this.estadoInventarioForm.reset();
    this.selectedEstadoInventario = null;
    this.isEditing = false;
  }

  private markFormGroupTouched() {
    Object.keys(this.estadoInventarioForm.controls).forEach((key) => {
      const control = this.estadoInventarioForm.get(key);
      if (control) {
        control.markAsTouched();
      }
    });
  }

  getFieldError(fieldName: string): string {
    const field = this.estadoInventarioForm.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) {
        return `${this.getFieldLabel(fieldName)} es requerido`;
      }
      if (field.errors['maxlength']) {
        return `${this.getFieldLabel(fieldName)} debe tener máximo ${
          field.errors['maxlength'].requiredLength
        } caracteres`;
      }
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      codigo: 'Código',
      nombre: 'Nombre',
      descripcion: 'Descripción',
      activo: 'Estado',
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.estadoInventarioForm.get(fieldName);
    return !!(field?.invalid && field.touched);
  }

  getEstadoLabel(activo: number): string {
    return activo === 1 ? 'Activo' : 'Inactivo';
  }

  getEstadoSeverity(activo: number): string {
    return activo === 1 ? 'success' : 'danger';
  }
}

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TipoMovimiento } from '../../../domain/dto/inventory.interface';
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
import { InputSwitchModule } from 'primeng/inputswitch';
import { ColsModel } from 'src/app/shared/utils/models/cols.model';
import { TiposMovimientosTableModel } from '../../../domain/models/tipos-movimientos-table.models';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { IFilterInventoryStateTableModel, IFilterTiposMovimientosTableModel } from '../../../domain/models/inventory-state-filter.models';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-tipos-movimientos',
  standalone: true,
  templateUrl: './tipos-movimientos.component.html',
  styleUrls: ['./tipos-movimientos.component.scss'],
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
    InputSwitchModule 
  ],
  providers: [ConfirmationService, MessageService]
})
export class TiposMovimientosComponent implements OnInit {

  tiposMovimientosTable: TiposMovimientosTableModel[] = [];
  filterTable!: IFilterTable<IFilterTiposMovimientosTableModel>;
  tiposMovimientos: TipoMovimiento[] = [];
  displayDialog = false;
  displayDeleteDialog = false;
  tipoMovimientoForm!: FormGroup;
  isEditing = false;
  selectedTipoMovimiento: TipoMovimiento | null = null;
  loading = false;
  public totalRecords = 0;
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
        this.inventoryCatalogService.pageTipoMovimientosTable(this.filterTable)
      );
      this.tiposMovimientosTable = response.data?.content ?? [];
      this.totalRecords = response.data?.totalElements ?? 0;
      this.loading = false;
    } catch (error) {
      this.tiposMovimientosTable = [];
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
    this.tipoMovimientoForm = this.fb.group({
      codigo: ['', [Validators.required, Validators.maxLength(20)]],
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      esEntrada: [false, Validators.required],
      esSalida: [false, Validators.required],
      requiereEmpleado: [false, Validators.required],
      requiereAprobacion: [false, Validators.required],
      descripcion: ['', Validators.maxLength(500)],
      activo: [1, Validators.required]
    });
  }
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

  filterGlobal(event: Event) {
    this.loadTable({
      first: 0,
      rows: this.rowSize,
      globalFilter: (event.target as HTMLInputElement)?.value ?? '',
    });
  }
  openNew() {
    this.isEditing = false;
    this.selectedTipoMovimiento = null;
    this.tipoMovimientoForm.reset();
    this.tipoMovimientoForm.patchValue({ activo: 1 });
    this.displayDialog = true;
  }

  editTipoMovimiento(tipoMovimiento: TipoMovimiento) {
    this.isEditing = true;
    this.selectedTipoMovimiento = { ...tipoMovimiento };
    this.tipoMovimientoForm.patchValue({
      codigo: tipoMovimiento.codigo,
      nombre: tipoMovimiento.nombre,
      descripcion: tipoMovimiento.descripcion,
      activo: tipoMovimiento.activo
    });
    this.displayDialog = true;
  }

  deleteTipoMovimiento(tipoMovimiento: TipoMovimiento) {
    this.confirmationService.confirm({
      message: `¿Está seguro que desea eliminar el tipo de movimiento "${tipoMovimiento.nombre}"?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.performDelete(tipoMovimiento.id ?? 0);
        this.loadTable({ first: 0, rows: this.rowSize });
      }
    });
  }

  private performDelete(id: number) {
    this.inventoryCatalogService.deleteTipoMovimiento(id).subscribe({
      next: (response) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Tipo de movimiento eliminado correctamente'
        });
        this.loadTable({ first: 0, rows: this.rowSize });
      },
      error: (error) => {
        console.error('Error deleting tipo movimiento:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudo eliminar el tipo de movimiento'
        });
      }
    });
  }

  saveTipoMovimiento() {
    if (this.tipoMovimientoForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.submitting = true;
    const formValue = this.tipoMovimientoForm.value;

    if (this.isEditing && this.selectedTipoMovimiento && this.selectedTipoMovimiento.id !== undefined) {
      this.inventoryCatalogService.updateTipoMovimiento(this.selectedTipoMovimiento.id, formValue).subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Tipo de movimiento actualizado correctamente'
          });
          this.displayDialog = false;
          this.loadTable({ first: 0, rows: this.rowSize });
          this.submitting = false;
        },
        error: (error) => {
          console.error('Error updating tipo movimiento:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: error.error?.message || 'No se pudo actualizar el tipo de movimiento'
          });
          this.submitting = false;
        }
      });
    } else {
      this.inventoryCatalogService.createTipoMovimiento(formValue).subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Tipo de movimiento creado correctamente'
          });
          this.displayDialog = false;
          this.loadTable({ first: 0, rows: this.rowSize });
          this.submitting = false;
        },
        error: (error) => {
          console.error('Error creating tipo movimiento:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: error.error?.message || 'No se pudo crear el tipo de movimiento'
          });
          this.submitting = false;
        }
      });
    }
  }

  hideDialog() {
    this.displayDialog = false;
    this.tipoMovimientoForm.reset();
    this.selectedTipoMovimiento = null;
    this.isEditing = false;
  }

  private markFormGroupTouched() {
    Object.keys(this.tipoMovimientoForm.controls).forEach(key => {
      const control = this.tipoMovimientoForm.get(key);
      if (control) {
        control.markAsTouched();
      }
    });
  }

  getFieldError(fieldName: string): string {
    const field = this.tipoMovimientoForm.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) {
        return `${this.getFieldLabel(fieldName)} es requerido`;
      }
      if (field.errors['maxlength']) {
        return `${this.getFieldLabel(fieldName)} debe tener máximo ${field.errors['maxlength'].requiredLength} caracteres`;
      }
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      codigo: 'Código',
      nombre: 'Nombre',
      descripcion: 'Descripción',
      activo: 'Estado'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.tipoMovimientoForm.get(fieldName);
    return !!(field?.invalid && field.touched);
  }

  getEstadoLabel(activo: number): string {
    return activo === 1 ? 'Activo' : 'Inactivo';
  }

  getEstadoSeverity(activo: number): string {
    return activo === 1 ? 'success' : 'danger';
  }
}
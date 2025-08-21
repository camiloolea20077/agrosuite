import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TipoInsumo } from '../../../domain/dto/inventory.interface';
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
import { IFilterInventoryStateTableModel } from '../../../domain/models/inventory-state-filter.models';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { InsumosTableModel } from '../../../domain/models/tipos-insumos-table.models';
import { lastValueFrom } from 'rxjs';
import { ColsModel } from 'src/app/shared/utils/models/cols.model';

@Component({
  selector: 'app-tipos-insumos',
  standalone: true,
  templateUrl: './tipos-insumos.component.html',
  styleUrls: ['./tipos-insumos.component.scss'],
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
    ToastModule
  ],
  providers: [ConfirmationService, MessageService]
})
export class TiposInsumosComponent implements OnInit {
  public rowSize = 10
  tiposInsumos: TipoInsumo[] = [];
  tiposInsumosTable: InsumosTableModel[] = [];
  filterTable!: IFilterTable<IFilterInventoryStateTableModel>;
  displayDialog = false;
  displayDeleteDialog = false;
  tipoInsumoForm!: FormGroup;
  isEditing = false;
  public totalRecords = 0;
  selectedTipoInsumo: TipoInsumo | null = null;
  loading = false;
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
        this.inventoryCatalogService.pageInsumosTable(this.filterTable)
      );
      this.tiposInsumosTable = response.data?.content ?? [];
      this.totalRecords = response.data?.totalElements ?? 0;
      this.loading = false;
    } catch (error) {
      this.tiposInsumosTable = [];
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
    this.tipoInsumoForm = this.fb.group({
      codigo: ['', [Validators.required, Validators.maxLength(20)]],
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      descripcion: ['', Validators.maxLength(500)],
      activo: [1, Validators.required]
    });
  }

  openNew() {
    this.isEditing = false;
    this.selectedTipoInsumo = null;
    this.tipoInsumoForm.reset();
    this.tipoInsumoForm.patchValue({ activo: 1 });
    this.displayDialog = true;
  }

  editTipoInsumo(tipoInsumo: TipoInsumo) {
    this.isEditing = true;
    this.selectedTipoInsumo = { ...tipoInsumo };
    this.tipoInsumoForm.patchValue({
      codigo: tipoInsumo.codigo,
      nombre: tipoInsumo.nombre,
      descripcion: tipoInsumo.descripcion,
      activo: tipoInsumo.activo
    });
    this.displayDialog = true;
  }

  deleteTipoInsumo(tipoInsumo: TipoInsumo) {
    this.confirmationService.confirm({
      message: `¿Está seguro que desea eliminar el tipo de insumo "${tipoInsumo.nombre}"?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.performDelete(tipoInsumo.id ?? 0);
      }
    });
  }

  private performDelete(id: number) {
    this.inventoryCatalogService.deleteTipoInsumo(id).subscribe({
      next: (response) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Tipo de insumo eliminado correctamente'
        });
        this.loadTable({ first: 0, rows: this.rowSize });
      },
      error: (error) => {
        console.error('Error deleting tipo insumo:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudo eliminar el tipo de insumo'
        });
      }
    });
  }

  saveTipoInsumo() {
    if (this.tipoInsumoForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.submitting = true;
    const formValue = this.tipoInsumoForm.value;

    if (this.isEditing && this.selectedTipoInsumo && this.selectedTipoInsumo.id !== undefined) {
      this.inventoryCatalogService.updateTipoInsumo(this.selectedTipoInsumo.id, formValue).subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Tipo de insumo actualizado correctamente'
          });
          this.displayDialog = false;
          this.loadTable({ first: 0, rows: this.rowSize });
          this.submitting = false;
        },
        error: (error) => {
          console.error('Error updating tipo insumo:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: error.error?.message || 'No se pudo actualizar el tipo de insumo'
          });
          this.submitting = false;
        }
      });
    } else {
      this.inventoryCatalogService.createTipoInsumo(formValue).subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Tipo de insumo creado correctamente'
          });
          this.displayDialog = false;
          this.loadTable({ first: 0, rows: this.rowSize });
          this.submitting = false;
        },
        error: (error) => {
          console.error('Error creating tipo insumo:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: error.error?.message || 'No se pudo crear el tipo de insumo'
          });
          this.submitting = false;
        }
      });
    }
  }

  hideDialog() {
    this.displayDialog = false;
    this.tipoInsumoForm.reset();
    this.selectedTipoInsumo = null;
    this.isEditing = false;
  }

  private markFormGroupTouched() {
    Object.keys(this.tipoInsumoForm.controls).forEach(key => {
      const control = this.tipoInsumoForm.get(key);
      if (control) {
        control.markAsTouched();
      }
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
  getFieldError(fieldName: string): string {
    const field = this.tipoInsumoForm.get(fieldName);
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
    const field = this.tipoInsumoForm.get(fieldName);
    return !!(field?.invalid && field.touched);
  }

  getEstadoLabel(activo: number): string {
    return activo === 1 ? 'Activo' : 'Inactivo';
  }

  getEstadoSeverity(activo: number): string {
    return activo === 1 ? 'success' : 'danger';
  }
}
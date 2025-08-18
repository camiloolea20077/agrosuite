import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService, LazyLoadEvent } from 'primeng/api';
import { IFilterTableSupplier, Supplier, SupplierTableModelDto } from '../../../domain/dto/inventory.interface';
import { InventoryCatalogService } from '../../../infraestructure/catalog.service';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
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
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-proveedores',
  standalone: true,
  templateUrl: './proveedores.component.html',
  styleUrls: ['./proveedores.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
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
export class ProveedoresComponent implements OnInit {
  
  proveedores: SupplierTableModelDto[] = [];
  selectedProveedor: Supplier | null = null;
  displayDialog = false;
  displayViewDialog = false;
  proveedorForm!: FormGroup;
  isEditing = false;
  loading = false;
  submitting = false;
  
  // Paginación y filtros
  totalRecords = 0;
  pageSize = 10;
  currentPage = 0;
  globalFilter = '';
  filtroEstado: number | null = null;
  filtersTable!: IFilterTable<IFilterTableSupplier>;
    public rowSize = 10;
    public loadingTable = true;
  // Opciones para dropdowns
  estadoOptions = [
    { label: '✓ Activo', value: 1, icon: 'pi pi-check-circle' },
    { label: '✗ Inactivo', value: 0, icon: 'pi pi-times-circle' }
  ];

  filtroEstadoOptions = [
    { label: 'Todos', value: null },
    { label: '✓ Activos', value: 1 },
    { label: '✗ Inactivos', value: 0 }
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
    this.loadTable;
  }

  private initForm() {
    this.proveedorForm = this.fb.group({
      nit: ['', [Validators.required, Validators.maxLength(20)]],
      nombre: ['', [Validators.required, Validators.maxLength(200)]],
      direccion: ['', Validators.maxLength(300)],
      contacto: ['', [Validators.required, Validators.maxLength(100)]],
      cargo: ['', Validators.maxLength(100)],
      telefono: ['', Validators.maxLength(20)],
      email: ['', [Validators.email, Validators.maxLength(100)]],
      observaciones: ['', Validators.maxLength(1000)],
      activo: [1, Validators.required]
    });
  }

  // Computed properties para estadísticas
  get totalProveedores(): number {
    return this.totalRecords;
  }

  get proveedoresActivos(): number {
    return this.proveedores.filter(p => p.activo === 1).length;
  }

async loadTable(lazyTable: TableLazyLoadEvent): Promise<void> {
  this.loadingTable = true;
  this.filtersTable = this.prepareTableParams(lazyTable);

  try {
    const response = await lastValueFrom(
      this.inventoryCatalogService.pageSuppliers(this.filtersTable)
    );
    this.proveedores = response.data?.content ?? [];
    this.totalRecords = response.data?.totalElements ?? 0;
  } catch (error) {
    this.proveedores = [];
    this.totalRecords = 0;
  } finally {
    this.loadingTable = false;
  }
}

private prepareTableParams(
  lazyTable: TableLazyLoadEvent
): IFilterTable<IFilterTableSupplier> {
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
  filterGlobal(event: Event) {
    this.loadTable({
      first: 0,
      rows: this.rowSize,
      globalFilter: (event.target as HTMLInputElement)?.value ?? '',
    })
  }
  private searchTimeout: any;
  private debounceSearch() {
    clearTimeout(this.searchTimeout);
    this.searchTimeout = setTimeout(() => {
      this.loadTable;
    }, 300);
  }

  onFiltroEstadoChange() {
    this.currentPage = 0;
    this.loadTable;
  }

  clearFilter() {
    this.globalFilter = '';
    this.currentPage = 0;
    this.loadTable;
  }

  openNew() {
    this.isEditing = false;
    this.selectedProveedor = null;
    this.proveedorForm.reset();
    this.proveedorForm.patchValue({ activo: 1 });
    this.displayDialog = true;
  }

  editProveedor(proveedor: Supplier) {
    this.isEditing = true;
    this.selectedProveedor = { ...proveedor };
    this.proveedorForm.patchValue({
      nit: proveedor.nit,
      cargo: proveedor.cargo,
      observaciones: proveedor.observaciones,
      nombre: proveedor.nombre,
      direccion: proveedor.direccion,
      contacto: proveedor.contacto,
      telefono: proveedor.telefono,
      email: proveedor.email,
      activo: proveedor.activo
    });
    this.displayDialog = true;
  }

  viewProveedor(proveedor: Supplier) {
    this.selectedProveedor = { ...proveedor };
    this.displayViewDialog = true;
  }

  editFromView() {
    this.hideViewDialog();
    if (this.selectedProveedor) {
      this.editProveedor(this.selectedProveedor);
    }
  }

  deleteProveedor(proveedor: Supplier) {
    this.confirmationService.confirm({
      message: `¿Está seguro que desea eliminar el proveedor "${proveedor.nombre}"?<br><br>
                <strong>Contacto:</strong> ${proveedor.contacto || 'No especificado'}`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptButtonStyleClass: 'p-button-danger',
      rejectButtonStyleClass: 'p-button-secondary',
      accept: () => {
        this.performDelete(proveedor.id ?? 0);
      }
    });
  }

  private performDelete(id: number) {
    this.inventoryCatalogService.deleteSupplier(id).subscribe({
      next: (response) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Proveedor eliminado correctamente'
        });
        this.loadTable;
      },
      error: (error) => {
        console.error('Error deleting proveedor:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: error.error?.message || 'No se pudo eliminar el proveedor'
        });
      }
    });
  }

  saveProveedor() {
    if (this.proveedorForm.invalid) {
      this.markFormGroupTouched();
      this.messageService.add({
        severity: 'warn',
        summary: 'Formulario Incompleto',
        detail: 'Por favor complete todos los campos requeridos'
      });
      return;
    }

    this.submitting = true;
    const formValue = this.proveedorForm.value;

    if (this.isEditing && this.selectedProveedor && this.selectedProveedor.id !== undefined) {
      this.inventoryCatalogService.updateSupplier(this.selectedProveedor.id, formValue).subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Proveedor actualizado correctamente'
          });
          this.displayDialog = false;
          this.loadTable;
          this.submitting = false;
        },
        error: (error) => {
          console.error('Error updating proveedor:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: error.error?.message || 'No se pudo actualizar el proveedor'
          });
          this.submitting = false;
        }
      });
    } else {
      this.inventoryCatalogService.createSupplier(formValue).subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Proveedor registrado correctamente'
          });
          this.displayDialog = false;
          this.loadTable;
          this.submitting = false;
        },
        error: (error) => {
          console.error('Error creating proveedor:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: error.error?.message || 'No se pudo registrar el proveedor'
          });
          this.submitting = false;
        }
      });
    }
  }

  hideDialog() {
    this.displayDialog = false;
    this.proveedorForm.reset();
    this.selectedProveedor = null;
    this.isEditing = false;
  }

  hideViewDialog() {
    this.displayViewDialog = false;
    this.selectedProveedor = null;
  }

  exportProveedores() {
    // Implementar exportación a Excel
    this.messageService.add({
      severity: 'info',
      summary: 'Exportación',
      detail: 'Funcionalidad de exportación en desarrollo'
    });
  }

  private markFormGroupTouched() {
    Object.keys(this.proveedorForm.controls).forEach(key => {
      const control = this.proveedorForm.get(key);
      if (control) {
        control.markAsTouched();
      }
    });
  }

  getFieldError(fieldName: string): string {
    const field = this.proveedorForm.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) {
        return `${this.getFieldLabel(fieldName)} es requerido`;
      }
      if (field.errors['maxlength']) {
        return `${this.getFieldLabel(fieldName)} debe tener máximo ${field.errors['maxlength'].requiredLength} caracteres`;
      }
      if (field.errors['email']) {
        return 'Ingrese un email válido';
      }
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      nit: 'NIT',
      nombre: 'Nombre de la empresa',
      direccion: 'Dirección',
      contacto: 'Nombre del contacto',
      cargo: 'Cargo',
      telefono: 'Teléfono',
      email: 'Email',
      observaciones: 'Observaciones',
      activo: 'Estado'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.proveedorForm.get(fieldName);
    return !!(field?.invalid && field.touched);
  }

  getEstadoLabel(activo: number): string {
    return activo === 1 ? 'Activo' : 'Inactivo';
  }

  getEstadoSeverity(activo: number): string {
    return activo === 1 ? 'success' : 'danger';
  }

  getEstadoIcon(activo: number): string {
    return activo === 1 ? 'pi pi-check-circle' : 'pi pi-times-circle';
  }
}
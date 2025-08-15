import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormsModule,
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Subject, firstValueFrom, lastValueFrom } from 'rxjs';
import { takeUntil, finalize } from 'rxjs/operators';

import { TableModule, TableLazyLoadEvent, Table } from 'primeng/table'; // Added Table import
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CheckboxModule } from 'primeng/checkbox';
import { TagModule } from 'primeng/tag';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { CardModule } from 'primeng/card';
import { PaginatorModule, PaginatorState } from 'primeng/paginator'; // Added PaginatorState

import { MessageService, ConfirmationService } from 'primeng/api';
import {
  EstadoInventario,
  Inventory,
  InventoryTableDto,
  TipoInsumo,
  CreateInventoryDto,
} from '../../../domain/dto/inventory.interface';
import { InventoryService } from '../../../infraestructure/inventory.service';
import { InventoryCatalogService } from '../../../infraestructure/catalog.service';

import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { IInventoryFilterTable } from '../../../domain/models/inventory-filter-table.models';
import { TooltipModule } from 'primeng/tooltip';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';

@Component({
  selector: 'app-inventory-management',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
    DialogModule,
    ToastModule,
    ConfirmDialogModule,
    InputNumberModule,
    InputTextareaModule,
    CheckboxModule,
    TagModule,
    ProgressSpinnerModule,
    CardModule,
    PaginatorModule,
    TooltipModule,
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './inventory-management.component.html',
  styleUrls: ['./inventory-management.component.scss'],
})
export class InventoryManagementComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  // Tabla (lazy)
  inventory: InventoryTableDto[] = [];
  totalRecords = 0;
  loadingTable = false;
  rowSize = 10;
  filtersTable!: IFilterTable<IInventoryFilterTable>;
  // Missing pagination properties
  first = 0;
  rows = 10;
  // Missing filter property
  globalFilterValue = '';
  // Catálogos
  tiposInsumos: TipoInsumo[] = [];
  estadosInventario: EstadoInventario[] = [];
  // Formulario
  inventoryForm: FormGroup;
  isFormVisible = false;
  isEditing = false;
  isLoading = false;
  selectedItem: Inventory | null = null;
  unidadesMedida = [
    { label: 'Kilogramos', value: 'kg' },
    { label: 'Gramos', value: 'g' },
    { label: 'Toneladas', value: 't' },
    { label: 'Onzas', value: 'oz' },
    { label: 'Libras', value: 'lb' },
    // Volumen
    { label: 'Litros', value: 'L' },
    { label: 'Mililitros', value: 'mL' },
    { label: 'Centímetros cúbicos', value: 'cm³' },
    { label: 'Galones', value: 'gal' },
    { label: 'Barriles', value: 'bbl' }
  ];
  unidadesCompra = [
    { label: 'Sacos', value: 'sacos' },
    { label: 'Cajas', value: 'cajas' },
    { label: 'Paquetes', value: 'paquetes' },
    { label: 'Bultos', value: 'bultos' },
    { label: 'Rollos', value: 'rollos' },
    { label: 'Botellas', value: 'botellas' },
    { label: 'Latas', value: 'latas' },
    { label: 'Frascos', value: 'frascos' },
    { label: 'Bidones', value: 'bidones' },
    { label: 'Tambores', value: 'tambores' },
    { label: 'Blisters', value: 'blisters' },
    { label: 'Tubos', value: 'tubos' },
    { label: 'Jarras', value: 'jarras' },
    { label: 'Sobre', value: 'sobres' },
    { label: 'Estuches', value: 'estuches' },
    { label: 'Palets', value: 'palets' },
    { label: 'Contenedores', value: 'contenedores' }
  ]
  // ViewChild for table reference
  @ViewChild('dt') table!: Table;

  constructor(
    private fb: FormBuilder,
    private inventoryService: InventoryService,
    private catalogService: InventoryCatalogService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {
    this.inventoryForm = this.createForm();
  }

  async ngOnInit(): Promise<void> {
    await this.loadTable({
      first: 0,
      rows: this.rowSize,
      sortField: 'id',
      sortOrder: 1,
      globalFilter: '',
    } as TableLazyLoadEvent);

    this.loadCatalogs();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private createForm(): FormGroup {
    return this.fb.group({
      id: [null],
      codigoInterno: ['', [Validators.required, Validators.maxLength(50)]],
      nombreInsumo: ['', [Validators.required, Validators.maxLength(100)]],
      descripcion: ['', Validators.maxLength(500)],
      marca: ['', Validators.maxLength(50)],
      tipoInsumoId: [null, Validators.required],
      unidadMedida: ['', [Validators.required, Validators.maxLength(20)]],
      unidadCompra: ['', [Validators.required, Validators.maxLength(20)]],
      factorConversion: [1, [Validators.required, Validators.min(0.01)]],
      cantidadActual: [0, [Validators.required, Validators.min(0)]],
      cantidadMinima: [0, [Validators.required, Validators.min(0)]],
      puntoReorden: [0, [Validators.required, Validators.min(0)]],
      cantidadReservada: [0, [Validators.min(0)]],
      ubicacionAlmacen: ['', Validators.maxLength(100)],
      esPeligroso: [false],
      requiereAprobacion: [false], // ← Campo agregado
      esAutomatico: [false],       // ← Campo agregado
      requiereCuidadoEspecial: [false],
      notas: ['', Validators.maxLength(1000)],
      estadoId: [null, Validators.required],
    });
  }
  private loadCatalogs(): void {
    // Cargar tipos de insumos activos
    this.catalogService
      .pageTiposInsumos()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (r) => {
          if (!r.error) {
            this.tiposInsumos = r.data ?? [];
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: r.message || 'Error cargando tipos de insumos',
            });
          }
        },
        error: () =>
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error cargando tipos de insumos',
          }),
      });

    // Cargar estados de inventario activos
    this.catalogService
      .pageEstadosInventario()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (r) => {
          if (!r.error) {
            this.estadosInventario = r.data ?? [];
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: r.message || 'Error cargando estados de inventario',
            });
          }
        },
        error: () =>
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error cargando estados de inventario',
          }),
      });
  }

  /** ====== TABLA (LAZY LOAD) ====== */
  async loadTable(lazyTable: TableLazyLoadEvent): Promise<void> {
    this.loadingTable = true;
    this.filtersTable = this.prepareTableParams(lazyTable);

    try {
      const response = await lastValueFrom(
        this.inventoryService.pageInventory(this.filtersTable)
      );
      this.inventory = response.data?.content ?? [];
      this.totalRecords = response.data?.totalElements ?? 0;
    } catch (error) {
      this.inventory = [];
      this.totalRecords = 0;
    } finally {
      this.loadingTable = false;
    }
  }

  private prepareTableParams(
    lazyTable: TableLazyLoadEvent
  ): IFilterTable<IInventoryFilterTable> {
    this.rowSize = lazyTable.rows ?? this.rowSize;
    const currentPage = lazyTable.first
      ? Math.floor(lazyTable.first / this.rowSize)
      : 0;

    // Update pagination properties
    this.first = lazyTable.first ?? 0;
    this.rows = lazyTable.rows ?? this.rowSize;

    // Nota: algunos PrimeNG ponen el filtro global dentro de filters['global'].
    // Usamos el campo directo si viene, sino vacío.
    const globalSearch =
      (lazyTable as any).globalFilter ?? this.globalFilterValue ?? '';

    return {
      page: currentPage,
      rows: this.rowSize,
      search: globalSearch,
      order: lazyTable.sortOrder === -1 ? 'desc' : 'asc',
      order_by: lazyTable.sortField ?? 'id',
    };
  }

  // Missing global filter method
  onGlobalFilter(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.globalFilterValue = target.value;
    if (this.table) {
      this.table.filterGlobal(this.globalFilterValue, 'contains');
    }
  }

  // Missing clear method
  clear(): void {
    if (this.table) {
      this.table.clear();
    }
    this.globalFilterValue = '';
  }

  // Missing pagination method
  onPageChange(event: PaginatorState): void {
    this.first = event.first ?? 0;
    this.rows = event.rows ?? this.rowSize;

    // Trigger table reload with new pagination
    const lazyEvent: TableLazyLoadEvent = {
      first: this.first,
      rows: this.rows,
      sortField: this.filtersTable?.order_by ?? 'id',
      sortOrder: this.filtersTable?.order === 'desc' ? -1 : 1,
      globalFilter: this.globalFilterValue,
    };

    this.loadTable(lazyEvent);
  }

  // Missing stock status methods
  getStockLabel(cantidad: number, minimo: number): string {
    if (cantidad <= 0) return 'Sin stock';
    if (cantidad <= minimo) return 'Stock bajo';
    if (cantidad <= minimo * 2) return 'Stock medio';
    return 'Stock alto';
  }

  getStockSeverity(
    cantidad: number,
    minimo: number
  ): 'success' | 'warning' | 'danger' | 'info' {
    if (cantidad <= 0) return 'danger';
    if (cantidad <= minimo) return 'warning';
    if (cantidad <= minimo * 2) return 'info';
    return 'success';
  }

  /** ====== CRUD ====== */
  showAddDialog(): void {
    this.isEditing = false;
    this.inventoryForm.reset({
      cantidadActual: 0,
      cantidadMinima: 0,
      puntoReorden: 0,
      cantidadReservada: 0,
      factorConversion: 1,
      esPeligroso: false,
      requiereCuidadoEspecial: false,
    });
    this.isFormVisible = true;
  }

  showEditDialog(item: InventoryTableDto): void {
    this.isLoading = true;
    this.inventoryService
      .getInventoryById(item.id)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => (this.isLoading = false))
      )
      .subscribe({
        next: (response) => {
          if (!response.error) {
            this.isEditing = true;
            this.selectedItem = response.data;
            this.inventoryForm.patchValue(response.data);
            this.isFormVisible = true;
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail:
                response.message || 'Error cargando detalle del inventario',
            });
          }
        },
        error: () =>
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error cargando detalle del inventario',
          }),
      });
  }

  async onSubmit(): Promise<void> {
    if (this.inventoryForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isLoading = true;
    const dto = this.inventoryForm.value as CreateInventoryDto & {
      id?: number;
    };

    try {
      if (this.isEditing && dto.id != null) {
        // UPDATE
        const resp: ResponseModel<boolean> = await firstValueFrom(
          this.inventoryService.updateInventory(dto.id, dto)
        );

        if (resp.error) {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: resp.message || 'Error guardando inventario',
          });
          return;
        }

        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Inventario actualizado',
        });
      } else {
        // CREATE
        const resp: ResponseModel<Inventory> = await firstValueFrom(
          this.inventoryService.createInventory(dto)
        );

        if (resp.error) {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: resp.message || 'Error guardando inventario',
          });
          return;
        }

        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Inventario creado',
        });
      }

      // Cerrar modal
      this.isFormVisible = false;

      // Recargar tabla manteniendo página/orden (el filtro global es opcional aquí)
      const first =
        (this.filtersTable?.page ?? 0) *
        (this.filtersTable?.rows ?? this.rowSize);
      const rows = this.filtersTable?.rows ?? this.rowSize;

      await this.loadTable({
        first,
        rows,
        sortField: this.filtersTable?.order_by ?? 'id',
        sortOrder: this.filtersTable?.order === 'desc' ? -1 : 1,
      } as TableLazyLoadEvent);
    } catch {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Error guardando inventario',
      });
    } finally {
      this.isLoading = false;
    }
  }
  confirmDelete(item: InventoryTableDto): void {
    const nombre =
      (item as any).nombreInsumo ?? (item as any).nombre_insumo ?? 'el insumo';

    this.confirmationService.confirm({
      message: `¿Está seguro de eliminar el insumo "${nombre}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      accept: () => this.deleteItem(item.id),
    });
  }

  private async deleteItem(id: number): Promise<void> {
    try {
      const resp = await lastValueFrom(
        this.inventoryService.deleteInventory(id)
      );
      if (!resp.error) {
        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Inventario eliminado correctamente',
        });
        // Recargar manteniendo filtros/sort/página
        await this.loadTable({
          first:
            (this.filtersTable?.page ?? 0) *
            (this.filtersTable?.rows ?? this.rowSize),
          rows: this.filtersTable?.rows ?? this.rowSize,
          sortField: this.filtersTable?.order_by ?? 'id',
          sortOrder: this.filtersTable?.order === 'desc' ? -1 : 1,
          globalFilter: this.filtersTable?.search ?? '',
        } as TableLazyLoadEvent);
      } else {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: resp.message || 'Error eliminando inventario',
        });
      }
    } catch {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Error eliminando inventario',
      });
    }
  }

  hideDialog(): void {
    this.isFormVisible = false;
    this.inventoryForm.reset();
    this.selectedItem = null;
  }

  private markFormGroupTouched(): void {
    Object.keys(this.inventoryForm.controls).forEach((key) =>
      this.inventoryForm.get(key)?.markAsTouched()
    );
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.inventoryForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(fieldName: string): string {
    const field = this.inventoryForm.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) return 'Este campo es requerido';
      if (field.errors['maxlength'])
        return `Máximo ${field.errors['maxlength'].requiredLength} caracteres`;
      if (field.errors['min'])
        return `Valor mínimo: ${field.errors['min'].min}`;
    }
    return '';
  }

  // Helpers (catálogos)
  getTipoInsumoName(id: number): string {
    const tipo = this.tiposInsumos.find((t) => t.id === id);
    return tipo?.nombre || 'N/A';
  }

  getEstadoInventarioName(id: number): string {
    const estado = this.estadosInventario.find((e) => e.id === id);
    return estado?.nombre || 'N/A';
  }
}

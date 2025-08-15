import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormsModule,
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { firstValueFrom, lastValueFrom, Subject } from 'rxjs';
import { takeUntil, finalize } from 'rxjs/operators';

// PrimeNG
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CalendarModule } from 'primeng/calendar';
import { TagModule } from 'primeng/tag';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { CardModule } from 'primeng/card';
import { TabViewModule } from 'primeng/tabview';
import { ChipModule } from 'primeng/chip';
import { TooltipModule } from 'primeng/tooltip';

import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';

import {
  EstadoMovimiento,
  IFilterTableMovimientos,
  InventoryListDto,
  InventoryMovement,
  InventoryMovementTableModelDto,
  InventoryTableDto,
  TipoMovimiento,
} from '../../../domain/dto/inventory.interface';
import { InventoryMovementService } from '../../../infraestructure/inventory-movement.service';
import { InventoryService } from '../../../infraestructure/inventory.service';
import { InventoryCatalogService } from '../../../infraestructure/catalog.service';

import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { IInventoryFilterTable } from '../../../domain/models/inventory-filter-table.models';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';
import { InputSwitchModule } from 'primeng/inputswitch';
// Nuevas importaciones para las mejoras visuales
import { AvatarModule } from 'primeng/avatar';
import { SpeedDialModule } from 'primeng/speeddial';
import { StepsModule } from 'primeng/steps';
import { PanelModule } from 'primeng/panel';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { RippleModule } from 'primeng/ripple';
import { DividerModule } from 'primeng/divider';
import { SkeletonModule } from 'primeng/skeleton';
import { BadgeModule } from 'primeng/badge'
import { CheckboxModule } from 'primeng/checkbox';
import { EmployeesService } from 'src/app/core/services/employees.service';
import { EmployeeList } from 'src/app/modules/employees/domain/models/employee-list.models';
@Component({
  selector: 'app-inventory-movements',
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
    CalendarModule,
    InputSwitchModule,
    SpeedDialModule,
    OverlayPanelModule,
    RippleModule,
    DividerModule,
    SkeletonModule,
    BadgeModule,
    ChipModule,
    TagModule,
    ProgressSpinnerModule,
    CardModule,
    TabViewModule,
    ChipModule,
    TooltipModule,
    StepsModule,
    PanelModule,
    AvatarModule,
    CheckboxModule
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './inventory-movements.component.html',
  styleUrls: ['./inventory-movements.component.scss'],
})
export class InventoryMovementsComponent implements OnInit, OnDestroy {
  private readonly destroy$ = new Subject<void>();
formSteps: MenuItem[] = [];
activeStepIndex: number = 0;
  // Data
  movements: InventoryMovement[] = [];
  inventoryItems: InventoryMovementTableModelDto[] = [];
  tiposMovimientos: TipoMovimiento[] = [];
  estadosMovimientos: EstadoMovimiento[] = [];
  pendingApprovals: InventoryMovement[] = [];
  // Form
  movementForm: FormGroup;
  isFormVisible = false;
  isEditing = false;
  isLoading = false;

  // Filters
  selectedInventoryId: number | null = null;
  startDate: Date | null = null;
  endDate: Date | null = null;
  selectedStatus: number | null = null;
  selectedType: number | null = null;
  inventoryList: InventoryListDto[] = [];
  employeesList: EmployeeList[] = [];
  // Tabs
  activeTab = 0;
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
  constructor(
    private readonly fb: FormBuilder,
    private readonly movementService: InventoryMovementService,
    private readonly inventoryService: InventoryService,
    private readonly catalogService: InventoryCatalogService,
    private readonly messageService: MessageService,
    private readonly confirmationService: ConfirmationService,
    private readonly employeesService: EmployeesService,
  ) {
    this.movementForm = this.createForm();
  }

  ngOnInit(): void {
    this.endDate = new Date()
    this.getEmployees();
    this.getInventory();
    this.loadCatalogs();
    this.loadInventoryItems();
    this.loadMovements();
    this.loadPendingApprovals();
    this.updateAvailableUnits();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private createForm(): FormGroup {
    return this.fb.group({
      id: [null],
      inventoryId: [null, Validators.required],
      tipoMovimientoId: [null, Validators.required],
      estadoId: [null, Validators.required],
      cantidad: [0, [Validators.required, Validators.min(0.01)]],
      unidadMedida: ['', Validators.required],
      fechaMovimiento: [new Date(), Validators.required],
      fechaProgramada: [null],
      employeeId: [null],
      numeroDocumento: [''],
      observaciones: [''],
      notas: [''],
      ubicacionOrigen: [''],
      ubicacionDestino: [''],
      requiereAprobacion: [false],
      esAutomatico: [false],
    });
  }

  /** ===== Catálogos ===== */
  private loadCatalogs(): void {
    // Tipos de movimientos
    this.catalogService
      .pageTiposMovimientos()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (resp) => {
          if (!resp.error) {
            this.tiposMovimientos = resp.data ?? [];
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: resp.message || 'Error cargando tipos de movimientos',
            });
          }
        },
        error: () =>
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error cargando tipos de movimientos',
          }),
      });

    // Estados de movimientos
    this.catalogService
      .pageEstadosMovimientos()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (resp) => {
          if (!resp.error) {
            this.estadosMovimientos = resp.data ?? [];
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: resp.message || 'Error cargando estados de movimientos',
            });
          }
        },
        error: () =>
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error cargando estados de movimientos',
          }),
      });
  }

  /** ===== Inventario para dropdown ===== */
  private loadInventoryItems(): void {
    const pageRequest: IFilterTable<IFilterTableMovimientos> = {
      page: 0,
      rows: 1000,
      search: '',
      order: 'asc',
      order_by: 'id',
    };

    this.movementService
      .pageInventory(pageRequest)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (resp) => {
          if (!resp.error) {
            this.inventoryItems = resp.data?.content ?? [];
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: resp.message || 'Error cargando inventario',
            });
          }
        },
        error: () =>
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error cargando inventario',
          }),
      });
  }

  /** ===== Movimientos ===== */
private loadMovements(): void {
  this.isLoading = true;
  let start = this.startDate ? new Date(this.startDate) : null;
  let end   = this.endDate   ? new Date(this.endDate)   : null;
  if (start && !end) end = new Date(start);
  if (!start && end) start = new Date(end);

  if (!start && !end) {
    end = new Date();
    start = new Date(end);
    start.setDate(end.getDate() - 30);
  }
  const toISOStart = (d: Date) =>
    new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate(), 0, 0, 0, 0)).toISOString();
  const toISOEnd = (d: Date) =>
    new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate(), 23, 59, 59, 999)).toISOString();
  let obs$;
  if (start && end) {
    obs$ = this.movementService.getByDateRange(
      toISOStart(start),
      toISOEnd(end)
    );
  } else if (this.selectedInventoryId != null) {
    obs$ = this.movementService.getByInventory(this.selectedInventoryId);
  } else {
    return;
  }
obs$
  .pipe(takeUntil(this.destroy$), finalize(() => (this.isLoading = false)))
  .subscribe({
    next: (movements: InventoryMovement[]) => {
      this.movements = Array.isArray(movements) ? movements : [];
      this.applyLocalFilters();
    },
    error: () => {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Error cargando movimientos',
      });
    },
  });
}


  private loadPendingApprovals(): void {
    this.movementService
      .getPendingApprovals()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (list: InventoryMovement[]) =>
          (this.pendingApprovals = Array.isArray(list) ? list : []),
        error: () => {
          /* opcional: toast */
        },
      });
  }

  private applyLocalFilters(): void {
    let data = [...this.movements];
    if (this.selectedStatus != null) {
      data = data.filter((m) => m.estadoId === this.selectedStatus);
    }
    if (this.selectedType != null) {
      data = data.filter((m) => m.tipoMovimientoId === this.selectedType);
    }
    this.movements = data;
  }

  onFilterChange(): void {
    this.loadMovements();
  }
  clearFilters(): void {
    this.selectedInventoryId = null;
    this.selectedType = null;
    this.startDate = null;
    this.endDate = null;
    this.loadMovements();
  }

  /** ===== CRUD ===== */
  showAddDialog(): void {
    this.isEditing = false;
    this.movementForm.reset();
    this.movementForm.patchValue({
      fechaMovimiento: new Date(),
      cantidad: 0,
      requiereAprobacion: false,
      esAutomatico: false,
    });
    this.isFormVisible = true;
  }


async showEditDialog(movement: InventoryMovement): Promise<void> {
  if (!movement.id) {
    console.warn('Movement ID is missing');
    return;
  }

  this.isLoading = true;
  
  try {
    let data; 
    try {
      const response = await lastValueFrom(this.movementService.getById(movement.id));
      data = response.data;
      if (!data || typeof data !== 'object' || Object.keys(data).length === 0) {
        console.warn('Datos del servidor inválidos, usando datos del listado');
        data = movement;
      }
    } catch (serviceError) {
      console.warn('Error en el servicio getById, usando datos del listado:', serviceError);
      data = movement;
    }
    const formData = {
      id: data.id || null,
      inventoryId: data.inventoryId || null,
      tipoMovimientoId: data.tipoMovimientoId || null,
      estadoId: data.estadoId || null,
      cantidad: data.cantidad || 0,
      unidadMedida: data.unidadMedida || '',
      fechaMovimiento: this.parseDate(data.fechaMovimiento),
      fechaProgramada: this.parseDate(data.fechaProgramada),
      employeeId: data.employeeId || null,
      numeroDocumento: data.numeroDocumento || '',
      observaciones: data.observaciones || '',
      notas: data.notas || '',
      ubicacionOrigen: data.ubicacionOrigen || '',
      ubicacionDestino: data.ubicacionDestino || '',
      requiereAprobacion: Boolean(data.requiereAprobacion),
      esAutomatico: Boolean(data.esAutomatico)
    };
    this.movementForm.reset();
    this.movementForm.patchValue(formData);
    this.isEditing = true;
    this.isFormVisible = true;

  } catch (error) {
    console.error('Error cargando detalle del movimiento:', error);
    this.messageService.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Error cargando detalle del movimiento',
    });
  } finally {
    this.isLoading = false;
  }
}
// Método auxiliar para parsear fechas
private parseDate(dateValue: any): Date | null {
  if (!dateValue) return null;
  
  if (dateValue instanceof Date) {
    return dateValue;
  }
  
  if (typeof dateValue === 'string') {
    const parsed = new Date(dateValue);
    return isNaN(parsed.getTime()) ? null : parsed;
  }
  
  return null;
}
  async onSubmit(): Promise<void> {
    if (this.movementForm.invalid) {
      this.markFormGroupTouched();
      return;
    }
    this.isLoading = true;
    const dto = this.movementForm.value as InventoryMovement & { id?: number };
    // Copia para enviar al backend (fechas como string)
    const dtoParaEnviar: any = { ...dto };
    // Convertir fechas sin problemas de zona horaria
    if (dto.fechaMovimiento instanceof Date) {
      dtoParaEnviar.fechaMovimiento = this.dateToISOStringLocal(dto.fechaMovimiento);
    }
    if (dto.fechaProgramada instanceof Date) {
      dtoParaEnviar.fechaProgramada = this.dateToISOStringLocal(dto.fechaProgramada);
    }

    try {
      if (this.isEditing && dto.id != null) {
        const resp: boolean = await firstValueFrom(
          this.movementService.update(dtoParaEnviar)
        );

        if (!resp) {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error guardando movimiento',
          });
          return;
        }

        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Movimiento actualizado',
        });
      } else {
        const resp: InventoryMovement = await firstValueFrom(
          this.movementService.create(dtoParaEnviar)
        );

        if (!resp) {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error guardando movimiento',
          });
          return;
        }

        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Movimiento creado',
        });
      }

      // Cerrar modal y recargar listas
      this.isFormVisible = false;
      await this.loadMovements();
      await this.loadPendingApprovals();
    } catch {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Error guardando movimiento',
      });
    } finally {
      this.isLoading = false;
    }
  }
  private dateToISOStringLocal(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    
    // Formato: YYYY-MM-DDTHH:mm:ss (sin la Z para evitar conversión UTC)
    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  }
  approveMovement(movement: InventoryMovement): void {
    if (movement.id == null) return;

    this.confirmationService.confirm({
      message: '¿Está seguro de aprobar este movimiento?',
      header: 'Confirmar aprobación',
      icon: 'pi pi-check-circle',
      acceptLabel: 'Sí, aprobar',
      rejectLabel: 'Cancelar',
      accept: () => {
        if (movement.id !== undefined) {
          this.movementService
            .approve(movement.id)
            .pipe(takeUntil(this.destroy$))
            .subscribe({
              next: (ok: boolean) => {
                if (ok) {
                  this.messageService.add({
                    severity: 'success',
                    summary: 'Éxito',
                    detail: 'Movimiento aprobado',
                  });
                  this.loadPendingApprovals();
                  this.loadMovements();
                } else {
                  this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'No fue posible aprobar el movimiento',
                  });
                }
              },
              error: () =>
                this.messageService.add({
                  severity: 'error',
                  summary: 'Error',
                  detail: 'Error aprobando movimiento',
                }),
            });
        }
      },
    });
  }

  rejectMovement(movement: InventoryMovement): void {
    if (movement.id == null) return;

    this.confirmationService.confirm({
      message: '¿Está seguro de rechazar este movimiento?',
      header: 'Confirmar rechazo',
      icon: 'pi pi-times-circle',
      acceptLabel: 'Sí, rechazar',
      rejectLabel: 'Cancelar',
      accept: () => {
        if (movement.id !== undefined) {
          this.movementService
            .reject(movement?.id, 'Rechazado por el usuario')
            .pipe(takeUntil(this.destroy$))
            .subscribe({
              next: (ok: boolean) => {
                if (ok) {
                  this.messageService.add({
                    severity: 'info',
                    summary: 'Movimiento rechazado',
                    detail: 'El movimiento ha sido rechazado',
                  });
                  this.loadPendingApprovals();
                  this.loadMovements();
                } else {
                  this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'No fue posible rechazar el movimiento',
                  });
                }
              },
              error: () =>
                this.messageService.add({
                  severity: 'error',
                  summary: 'Error',
                  detail: 'Error rechazando movimiento',
                }),
            });
        }
      },
    });
  }

  confirmDelete(movement: InventoryMovement): void {
    if (movement.id == null) return;

    this.confirmationService.confirm({
      message: '¿Está seguro de eliminar este movimiento?',
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      accept: () => this.deleteMovement(movement.id!),
    });
  }

  private deleteMovement(id: number): void {
    this.movementService
      .delete(id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (ok: boolean) => {
          if (ok) {
            this.messageService.add({
              severity: 'success',
              summary: 'Éxito',
              detail: 'Movimiento eliminado',
            });
            this.loadMovements();
            this.loadPendingApprovals();
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'No fue posible eliminar el movimiento',
            });
          }
        },
        error: () =>
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error eliminando movimiento',
          }),
      });
  }

  hideDialog(): void {
    this.isFormVisible = false;
    this.movementForm.reset();
  }

  private markFormGroupTouched(): void {
    Object.keys(this.movementForm.controls).forEach((key) =>
      this.movementForm.get(key)?.markAsTouched()
    );
  }

  // Utils
  getInventoryName(id: number): string {
    return this.invNameMap.get(id) ?? `#${id}`;
  }

  getTipoMovimientoName(id: number): string {
    const tipo = this.tiposMovimientos.find((t) => t.id === id);
    return tipo?.nombre ?? 'N/A';
  }

  getEstadoMovimientoName(id: number): string {
    const estado = this.estadosMovimientos.find((e) => e.id === id);
    return estado?.nombre ?? 'N/A';
  }

  getMovementTypeSeverity(tipoId: number): string {
    const tipo = this.tiposMovimientos.find((t) => t.id === tipoId);
    if (!tipo) return 'info';
    if ((tipo as any).esEntrada) return 'success';
    if ((tipo as any).esSalida) return 'warning';
    return 'info';
  }

  getMovementTypeLabel(tipoId: number): string {
    const tipo = this.tiposMovimientos.find((t) => t.id === tipoId);
    if (!tipo) return 'N/A';
    if ((tipo as any).esEntrada) return 'ENTRADA';
    if ((tipo as any).esSalida) return 'SALIDA';
    return 'OTRO';
  }

  formatDate(date: Date | string | null): string {
    if (!date) return 'N/A';
    const d = typeof date === 'string' ? new Date(date) : date;
    return d.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'short',
      day: '2-digit',
    });
  }

  onTabChange(event: { index: number }): void {
    this.activeTab = event.index;
    if (this.activeTab === 1) this.loadPendingApprovals();
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.movementForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(fieldName: string): string {
    const field = this.movementForm.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) return 'Este campo es requerido';
      if (field.errors['min'])
        return `Valor mínimo: ${field.errors['min'].min}`;
    }
    return '';
  }
  private invNameMap = new Map<number, string>();
  getInventory(): void {
    this.inventoryService.getInventory().subscribe((res) => {
      this.inventoryList = res.data ?? [];
      // construir mapa una vez
      this.invNameMap.clear();
      for (const it of this.inventoryList) {
        if (typeof it.id === 'number' && it.nombre?.trim()) {
          this.invNameMap.set(it.id, it.nombre.trim());
        }
      }
      this.updateAvailableUnits();
    });
  }
  getEmployees():void {
    this.employeesService.getEmployees().subscribe((res) => {
      this.employeesList = res.data
    })
  }
  private updateAvailableUnits(): void {
    this.unidadesMedida = this.availableUnits;
  }
  onInventoryChange(inventoryId: number): void {
    if (!inventoryId) {
      this.movementForm.patchValue({ 
        unidadMedida: '',
        inventoryId: null 
      });
      return;
    }
    const selectedInventory = this.inventoryList.find(item => item.id === inventoryId);
    if (selectedInventory && selectedInventory.unidadMedida) {
      this.movementForm.patchValue({ 
        unidadMedida: selectedInventory.unidadMedida,
        inventoryId: inventoryId  // También establecer el inventoryId en el formulario
      });
    }
  }

  // También puedes crear una propiedad computed para las unidades disponibles
  get availableUnits(): Array<{label: string, value: string}> {
    // Obtener todas las unidades únicas del inventario
    const uniqueUnits = [...new Set(this.inventoryList.map(item => item.unidadMedida))];
    
    return uniqueUnits
      .filter(unit => unit && unit.trim() !== '') // Filtrar valores vacíos
      .map(unit => ({
        label: unit.charAt(0).toUpperCase() + unit.slice(1), // Capitalizar primera letra
        value: unit
      }))
      .sort((a, b) => a.label.localeCompare(b.label)); // Ordenar alfabéticamente
  }
}

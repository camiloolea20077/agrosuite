import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { Router, RouterModule } from "@angular/router";
import { ConfirmationService, MessageService } from "primeng/api";
import { ButtonModule } from "primeng/button";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { DialogModule } from "primeng/dialog";
import { DropdownModule } from "primeng/dropdown";
import { InputTextModule } from "primeng/inputtext";
import { ProgressSpinnerModule } from "primeng/progressspinner";
import { TableModule } from "primeng/table";
import { TagModule } from "primeng/tag";
import { ToastModule } from "primeng/toast";
import { lastValueFrom } from "rxjs";
import { DesteteTableDto } from "src/app/shared/dto/destete.dto";
import { MigrarTerneroDto } from "../../../domain/dto/migrar-ternero.dto";
import { HelpersService } from "src/app/shared/utils/pipes/helper.service";
import { DesteteService } from "../../../infraestructure/destete.service";
import { ColsModel } from "src/app/shared/utils/models/cols.model";
import { ResultadoMigracionDto } from "../../../domain/dto/resultado-migracion.dto";

@Component({
    selector: "app-index-destete-component",
    standalone: true,
    templateUrl: "./index-destete.component.html",
    styleUrls: ["./index-destete.component.scss"],
    providers: [MessageService, ConfirmationService],
    imports: [
        FormsModule,
        RouterModule,
        InputTextModule,
        ConfirmDialogModule,
        ButtonModule, 
        ReactiveFormsModule, 
        TableModule, 
        CommonModule, 
        ToastModule,
        TagModule,
        ProgressSpinnerModule,
        DropdownModule,
        DialogModule
    ]
})
export class IndexDesteteComponent {
     globalFilter: string = '';
    public loadingTable = true;
    public loadingMigracion = false;
    public loadingHistorial = false;
    
    // Datos principales
    terneros: DesteteTableDto[] = [];
    historialDestetes: any[] = [];
    
    // Diálogos
    showMigrationDialog = false;
    showBatchMigrationDialog = false;
    showHistorialDialog = false;
    
    // Migración individual
    selectedTernero: DesteteTableDto | null = null;
    migrationDecision: string = '';
    
    // Migración por lotes
    selectedTerneros: DesteteTableDto[] = [];
    batchMigrationDecision: string = '';
    
    // Resultados de migración
    migrationResults: ResultadoMigracionDto[] = [];
    
    // Opciones para dropdown
    decisionOptions = [
        { label: 'Criar', value: 'cria' },
        { label: 'Vender', value: 'venta' }
    ];

    cols: ColsModel[] = [
        {
            field: 'numero_cria',
            header: 'Número Cría',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 150px;',
        },
        {
            field: 'fecha_nacimiento',
            header: 'Fecha Nacimiento',
            type: 'date',
            nameClass: 'text-center',
            minWidth: 'min-width: 140px;',
        },
        {
            field: 'fecha_proxima_destete',
            header: 'Fecha Próximo Destete',
            type: 'date',
            nameClass: 'text-center',
            minWidth: 'min-width: 160px;',
        },
        {
            field: 'dias_restantes',
            header: 'Días Restantes',
            type: 'number',
            nameClass: 'text-center',
            minWidth: 'min-width: 120px;',
        }
    ];

    constructor(
        readonly _helperService: HelpersService,
        private readonly _confirmationService: ConfirmationService,
        private desteteService: DesteteService,
        private router: Router,
        private messageService: MessageService
    ) { }

    ngOnInit(): void {
        this.loadTernerosParaDestetar();
        this.loadColumnActions();
    }

    async loadTernerosParaDestetar(): Promise<void> {
        this.loadingTable = true;
        try {
            const response = await lastValueFrom(
                this.desteteService.obtenerTernerosParaDestetar()
            );
            this.terneros = response.data ?? [];
            this.loadingTable = false;
            
            if (response.message) {
                this.messageService.add({
                    severity: 'info',
                    summary: 'Información',
                    detail: response.message
                });
            }
        } catch (error) {
            this.terneros = [];
            this.loadingTable = false;
            this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Error al cargar terneros para destetar'
            });
        }
    }

    async loadColumnActions(): Promise<void> {
        const columnAction = await this._helperService.showActionsTable();
        if (columnAction) {
            this.cols.push(columnAction);
        }
    }

    // Migración individual
    openMigrationDialog(ternero: DesteteTableDto): void {
        this.selectedTernero = ternero;
        this.migrationDecision = '';
        this.showMigrationDialog = true;
    }

    async migrarTernero(): Promise<void> {
        if (!this.selectedTernero || !this.migrationDecision) {
            this.messageService.add({
                severity: 'warn',
                summary: 'Advertencia',
                detail: 'Debe seleccionar una decisión'
            });
            return;
        }

        this.loadingMigracion = true;
        
        const migrarDto: MigrarTerneroDto = {
            birthId: this.selectedTernero.id,
            decision: this.migrationDecision as 'cria' | 'venta'
        };

        try {
            const response = await lastValueFrom(
                this.desteteService.migrarTernero(migrarDto)
            );
            
            this.loadingMigracion = false;
            this.showMigrationDialog = false;
            
            if (response.data?.success) {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Éxito',
                    detail: response.data.message
                });
                this.loadTernerosParaDestetar(); // Recargar lista
            } else {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: response.data?.message || 'Error en la migración'
                });
            }
        } catch (error) {
            this.loadingMigracion = false;
            this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Error al migrar ternero'
            });
        }
    }

    // Migración por lotes
    openBatchMigrationDialog(): void {
        if (this.selectedTerneros.length === 0) {
            this.messageService.add({
                severity: 'warn',
                summary: 'Advertencia',
                detail: 'Debe seleccionar al menos un ternero'
            });
            return;
        }
        this.batchMigrationDecision = '';
        this.showBatchMigrationDialog = true;
    }

    async migrarLoteTerneros(): Promise<void> {
        if (!this.batchMigrationDecision) {
            this.messageService.add({
                severity: 'warn',
                summary: 'Advertencia',
                detail: 'Debe seleccionar una decisión'
            });
            return;
        }

        this.loadingMigracion = true;

        const migrarDtos: MigrarTerneroDto[] = this.selectedTerneros.map(ternero => ({
            birthId: ternero.id,
            decision: this.batchMigrationDecision as 'cria' | 'venta'
        }));

        try {
            const response = await lastValueFrom(
                this.desteteService.migrarLoteTerneros(migrarDtos)
            );
            
            this.loadingMigracion = false;
            this.showBatchMigrationDialog = false;
            this.migrationResults = response.data ?? [];
            
            this.messageService.add({
                severity: 'info',
                summary: 'Proceso Completado',
                detail: response.message
            });
            
            this.selectedTerneros = [];
            this.loadTernerosParaDestetar(); // Recargar lista
            
        } catch (error) {
            this.loadingMigracion = false;
            this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Error al migrar lote de terneros'
            });
        }
    }

    // Historial
    async loadHistorialDestetes(): Promise<void> {
        this.loadingHistorial = true;
        try {
            const response = await lastValueFrom(
                this.desteteService.obtenerHistorialDestetes()
            );
            this.historialDestetes = response.data ?? [];
            this.loadingHistorial = false;
            this.showHistorialDialog = true;
        } catch (error) {
            this.loadingHistorial = false;
            this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Error al cargar historial de destetes'
            });
        }
    }

    // Utilidades
    getColumnIcon(field: string): string {
        const iconMap: { [key: string]: string } = {
            'numero_cria': 'pi pi-hashtag',
            'fecha_nacimiento': 'pi pi-calendar',
            'fecha_proxima_destete': 'pi pi-calendar-plus',
            'dias_restantes': 'pi pi-clock',
        };
        return iconMap[field] || 'pi pi-circle';
    }

    getDiasRestantesSeverity(diasRestantes: number): string {
        if (diasRestantes <= 0) return 'success';
        if (diasRestantes <= 7) return 'warning';
        return 'info';
    }

    getDiasRestantesLabel(diasRestantes: number): string {
        if (diasRestantes <= 0) return 'Listo para destetar';
        if (diasRestantes === 1) return '1 día';
        return `${diasRestantes} días`;
    }

    getFilterFields(): string[] {
        return this.cols
            .filter(col => col.field !== 'actions')
            .map(col => col.field);
    }

    filterGlobal(event: Event) {
        const filterValue = (event.target as HTMLInputElement)?.value ?? '';
        this.globalFilter = filterValue;
        // Implementar filtrado local si es necesario
    }

    // Confirmaciones
    confirmMigration(ternero: DesteteTableDto): void {
        this._confirmationService.confirm({
            message: `¿Está seguro de que desea procesar el destete del ternero ${ternero.numero_cria}?`,
            header: 'Confirmar Migración',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.openMigrationDialog(ternero);
            }
        });
    }

    confirmBatchMigration(): void {
        this._confirmationService.confirm({
            message: `¿Está seguro de que desea procesar el destete de ${this.selectedTerneros.length} terneros?`,
            header: 'Confirmar Migración por Lotes',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.openBatchMigrationDialog();
            }
        });
    }
    // 2. Mapear la decisión correctamente (agregar este método)
getDecisionLabel(decision: string): string {
    const decisionMap: { [key: string]: string } = {
        'cria': 'Criar',
        'venta': 'Vender',
        'sacrificio': 'Sacrificar'
    };
    return decisionMap[decision] || decision;
}

// 3. Determinar el estado (agregar este método)
getEstadoDestete(item: any): { label: string, severity: string } {
    // Lógica para determinar el estado - ajusta según tu lógica de negocio
    const fechaDestete = new Date(item.fecha_destete);
    const hoy = new Date();
    
    if (fechaDestete <= hoy) {
        return { label: 'Completado', severity: 'success' };
    } else {
        return { label: 'Programado', severity: 'info' };
    }
}

// 4. Método para obtener la severidad del tag de decisión
getDecisionSeverity(decision: string): string {
    const severityMap: { [key: string]: string } = {
        'cria': 'info',
        'venta': 'warning', 
        'sacrificio': 'danger'
    };
    return severityMap[decision] || 'secondary';
}
}
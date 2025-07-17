import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FormBuilder, FormsModule, ReactiveFormsModule } from "@angular/forms";
import { Router, RouterModule } from "@angular/router";
import { ConfirmationService, MessageService } from "primeng/api";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { TableLazyLoadEvent, TableModule } from "primeng/table";
import { lastValueFrom } from "rxjs";
import { ICattleFilterTable } from "src/app/core/models/cattle/cattle-filter-table.model";
import { CattleTableModel } from "src/app/core/models/cattle/cattle-table.model";
import { CattleService } from "src/app/core/services/cattle.service";
import { ColsModel } from "src/app/shared/utils/models/cols.model";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { HelpersService } from "src/app/shared/utils/pipes/helper.service";
import { ConfirmDialogModule } from 'primeng/confirmdialog'
import { ToastModule } from "primeng/toast";

@Component({
    selector: 'app-index-cattle',
    standalone: true,
    templateUrl: './index-cattle.component.html',
    styleUrls: ['./index-cattle.component.scss'],
    providers: [MessageService, ConfirmationService],
    imports: [FormsModule,
        RouterModule,
        InputTextModule,
        ConfirmDialogModule,
        ButtonModule, ReactiveFormsModule, TableModule, CommonModule, ToastModule],
})
export class IndexCattleComponent {
    public rowSize = 10
    public totalRecords = 0
    public loadingTable = true
    cattles: CattleTableModel[] = []
    filtersTable!: IFilterTable<ICattleFilterTable>
    cols: ColsModel[] = [
        {
            field: 'tipo_ganado',
            header: 'Tipo Ganado',
            type: 'string',
            nameClass: 'text-center',
        },
        {
            field: 'numero_ganado',
            header: 'Numero Ganado',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 180px;',
        },
        {
            field: 'sexo',
            header: 'Sexo',
            type: 'string',
            nameClass: 'text-center',
        },
        {
            field: 'color',
            header: 'Color',
            type: 'string',
            nameClass: 'text-left',
            minWidth: 'min-width: 40px;',
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
            field: 'observaciones',
            header: 'Observaciones',
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
    ) { }
    ngOnInit(): void {
        this.loadTable
        this.loadColumnActions()
    }


    async loadTable(lazyTable: TableLazyLoadEvent): Promise<void> {
        this.loadingTable = true
        this.filtersTable = this.prepareTableParams(lazyTable)

        try {
            const response = await lastValueFrom(
                this.cattleService.pageCattle(this.filtersTable)
            )
            this.cattles = response.data?.content ?? []
            this.totalRecords = response.data?.totalElements ?? 0
            this.loadingTable = false
        } catch (error) {
            this.cattles = []
            this.totalRecords = 0
            this.loadingTable = false
        }
    }
    private prepareTableParams(
        lazyTable: TableLazyLoadEvent
    ): IFilterTable<ICattleFilterTable> {
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
    async loadColumnActions(): Promise<void> {
        const columnAction = await this._helperService.showActionsTable()
        if (columnAction) {
            this.cols.push(columnAction)
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
}
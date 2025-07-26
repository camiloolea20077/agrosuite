
import { Component, OnInit } from "@angular/core";
import { ColsModel } from "src/app/shared/utils/models/cols.model";
import { IEmployeesFilterTable } from "../../domain/models/employees-filter-table.models";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { EmployeesTableModel } from "../../domain/models/employees-table.models";
import { HelpersService } from "src/app/shared/utils/pipes/helper.service";
import { ConfirmationService, MessageService } from "primeng/api";
import { Router, RouterModule } from "@angular/router";
import { EmployeesService } from "src/app/core/services/employees.service";
import { TableLazyLoadEvent, TableModule } from "primeng/table";
import { lastValueFrom } from "rxjs";
import { InputTextModule } from "primeng/inputtext";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { ButtonModule } from "primeng/button";
import { CommonModule } from "@angular/common";
import { ToastModule } from "primeng/toast";
@Component({
    selector: "app-index-employees",
    standalone: true,
    templateUrl: "./index-employees.component.html",
    styleUrls: ["./index-employees.component.scss"],
    imports: [
        RouterModule,
        InputTextModule,
        ConfirmDialogModule,
        ButtonModule, TableModule, CommonModule, ToastModule],
    providers: [ConfirmationService, MessageService]
})
export class IndexEmployeesComponent implements OnInit  {
    public rowSize = 10
    public totalRecords = 0
    public loadingTable = true
    employees: EmployeesTableModel[] = []
    filtersTable!: IFilterTable<IEmployeesFilterTable>
    cols: ColsModel[] = [
        {
            field: 'nombre',
            header: 'Nombre Empleado',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 280px;',
        },
        {
            field: 'identificacion',
            header: 'Identificacion',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 180px;',
        },
        {
            field: 'fecha_ingreso',
            header: 'Fecha de ingreso',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 180px;',
        },
        {
            field: 'cargo',
            header: 'Cargo',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 200px;',
        },
        {
            field: 'activo',
            header: 'Activo',
            type: 'icon',
            minWidth: 'min-width: 60px;',
            nameClass: 'text-center',
        },
    ];
    constructor(
        readonly _helperService: HelpersService,
        private readonly _confirmationService: ConfirmationService,
        private readonly employeesService: EmployeesService,
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
                this.employeesService.pageEmployees(this.filtersTable)
            )
            this.employees = response.data?.content ?? []
            this.totalRecords = response.data?.totalElements ?? 0
            this.loadingTable = false
        } catch (error) {
            this.employees = []
            this.totalRecords = 0
            this.loadingTable = false
        }
    }
    private prepareTableParams(
        lazyTable: TableLazyLoadEvent
    ): IFilterTable<IEmployeesFilterTable> {
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
    async deleteEmployee(id: number): Promise<void> {
        this._confirmationService.confirm({
            message: 'Seguro que desea eliminar el registro?',
            header: 'Eliminar Registro',
            icon: 'pi pi-exclamation-triangle',
            accept: async () => {
                try {
                    await lastValueFrom(this.employeesService.deleteEmployees(id))
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Operación exitosa',
                        detail: 'Registro eliminado correctamente',
                        life: 5000,
                    })
                    this.loadTable({ first: 0, rows: this.rowSize })
                } catch (error) {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: 'No se pudo eliminar el registro',
                        life: 5000,
                    })
                }
            },
        })
    }
}

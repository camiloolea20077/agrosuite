import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { Router, RouterModule } from "@angular/router";
import { ConfirmationService, MessageService } from "primeng/api";
import { ButtonModule } from "primeng/button";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { InputTextModule } from "primeng/inputtext";
import { TableLazyLoadEvent, TableModule } from "primeng/table";
import { ToastModule } from "primeng/toast";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { IInventoryFilterTable } from "../../../domain/models/inventory-filter-table.models";
import { ColsModel } from "src/app/shared/utils/models/cols.model";
import { HelpersService } from "src/app/shared/utils/pipes/helper.service";
import { InventoryService } from "../../../infraestructure/inventory.service";
import { lastValueFrom } from "rxjs";
import { InventoryTableModel } from "../../../domain/models/inventory-table.models";

@Component({
    selector: "app-index-inventory",
    standalone: true,
    templateUrl: "./index-inventory.component.html",
    styleUrls: ["./index-inventory.component.scss"],
    imports: [
        RouterModule,
        InputTextModule,
        ConfirmDialogModule,
        ButtonModule,
        TableModule,
        CommonModule,
        ToastModule],
    providers: [ConfirmationService, MessageService]
})
export class IndexInventoryComponent {
    public rowSize = 10
    public totalRecords = 0
    public loadingTable = true
    inventory: InventoryTableModel[] = []
    filtersTable!: IFilterTable<IInventoryFilterTable>
    cols: ColsModel[] = [
        {
            field: 'nombre_insumo',
            header: 'Nombre Insumo',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 280px;',
        },
        {
            field: 'cantidad_total',
            header: 'Cantidad Total',
            type: 'number',
            nameClass: 'text-center',
        },
        {
            field: 'unidad',
            header: 'Unidad',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 180px;',
        },
        {
            field: 'descripcion',
            header: 'Descripcion',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 250px;',
        },
    ];
    constructor(
        readonly _helperService: HelpersService,
        private readonly _confirmationService: ConfirmationService,
        private readonly inventoryService: InventoryService,
        private router: Router,
        private messageService: MessageService
    ) { }
    ngOnInit(): void {
        this.loadTable
    }
    async loadTable(lazyTable: TableLazyLoadEvent): Promise<void> {
        this.loadingTable = true
        this.filtersTable = this.prepareTableParams(lazyTable)

        try {
            const response = await lastValueFrom(
                this.inventoryService.pageInventory(this.filtersTable)
            )
            this.inventory = response.data?.content ?? []
            this.totalRecords = response.data?.totalElements ?? 0
            this.loadingTable = false
        } catch (error) {
            this.inventory = []
            this.totalRecords = 0
            this.loadingTable = false
        }
    }
    private prepareTableParams(
        lazyTable: TableLazyLoadEvent
    ): IFilterTable<IInventoryFilterTable> {
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
}
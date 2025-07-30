import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
import { ConfirmationService, MessageService } from "primeng/api";
import { ButtonModule } from "primeng/button";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { InputTextModule } from "primeng/inputtext";
import { TableLazyLoadEvent, TableModule } from "primeng/table";
import { ToastModule } from "primeng/toast";
import { CattleSalesTableModel } from "../../../domain/models/cattle-sales-table.models";
import { ICattleSalesFilterTable } from "../../../domain/models/cattle-sales-filter-table.models";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ColsModel } from "src/app/shared/utils/models/cols.model";
import { HelpersService } from "src/app/shared/utils/pipes/helper.service";
import { SalesService } from "../../../infraestructure/sales.service";
import { lastValueFrom } from "rxjs";
import { InventoryModule } from "../../../../inventory/inventory.module";
import { CattleSalesModule } from "../../../cattle-sales.module";
import { AlertService } from "src/app/shared/utils/pipes/alert.service";
import { CreateCattleSaleItemDto } from "../../../domain/dto/create-cattle-sale-item.dto";

@Component({
    selector: "app-index-cattle-sales",
    standalone: true,
    templateUrl: "./index-cattle-sales.component.html",
    styleUrls: ["./index-cattle-sales.component.scss"],
    imports: [
    RouterModule,
    InputTextModule,
    ConfirmDialogModule,
    ButtonModule,
    TableModule,
    CommonModule,
    ToastModule,
    InventoryModule,
    CattleSalesModule
],
    providers: [ConfirmationService, MessageService]
})
export class IndexCattleSalesComponent {
    public rowSize = 10
    public totalRecords = 0
    public slug: string | null = 'create';
    public loadingTable = true
    sales: CattleSalesTableModel[] = []
      public selectedItems: CreateCattleSaleItemDto[] = [];
      public selectedCattleIds: number[] = [];
    filtersTable!: IFilterTable<ICattleSalesFilterTable>
cols: ColsModel[] = [
    {
        field: 'id',
        header: '# Venta',
        type: 'number',
        nameClass: 'text-center',
        minWidth: 'min-width: 70px;',
    },
    {
        field: 'tipo_venta',
        header: 'Tipo Venta',
        type: 'string',
        nameClass: 'text-center',
        minWidth: 'min-width: 120px;',
    },
    {
        field: 'tipo_origen',
        header: 'Tipo Animal',
        type: 'string',
        nameClass: 'text-center',
        minWidth: 'min-width: 120px;',
    },
    {
        field: 'fecha_venta',
        header: 'Fecha Venta',
        type: 'string',
        nameClass: 'text-center',
        minWidth: 'min-width: 150px;',
    },
    {
        field: 'destino',
        header: 'Comprador',
        type: 'string',
        nameClass: 'text-center',
        minWidth: 'min-width: 150px;',
    },
    {
        field: 'observaciones',
        header: 'Observaciones',
        type: 'string',
        nameClass: 'text-center',
        minWidth: 'min-width: 180px;',
    },
    {
        field: 'total_animales',
        header: 'Animales',
        type: 'number',
        nameClass: 'text-center',
    },
    {
        field: 'precio_kilo',
        header: 'Precio/Kilo ($)',
        type: 'number',
        nameClass: 'text-center',
    },
    {
        field: 'peso_total',
        header: 'Peso Total (kg)',
        type: 'number',
        nameClass: 'text-center',
    },
    {
        field: 'subtotal',
        header: 'Subtotal ($)',
        type: 'number',
        nameClass: 'text-center',
    },
    {
        field: 'descuentos',
        header: 'Descuentos ($)',
        type: 'number',
        nameClass: 'text-center',
    },
    {
        field: 'total_venta',
        header: 'Total Venta ($)',
        type: 'number',
        nameClass: 'text-center',
    }
];

    constructor(
        readonly _helperService: HelpersService,
        private readonly _confirmationService: ConfirmationService,
        private readonly salesService: SalesService,
        private router: Router,
        private readonly _alertService: AlertService,
        private readonly cattleSaleService: SalesService,
        private readonly _activatedRoute: ActivatedRoute,
        private messageService: MessageService
    ) { }
    ngOnInit(): void {
        this.loadTable
            this.loadColumnActions();
    }
    async loadTable(lazyTable: TableLazyLoadEvent): Promise<void> {
        this.loadingTable = true
        this.filtersTable = this.prepareTableParams(lazyTable)

        try {
            const response = await lastValueFrom(
                this.salesService.pageSales(this.filtersTable)
            )
            this.sales = response.data?.content ?? []
            this.totalRecords = response.data?.totalElements ?? 0
            this.loadingTable = false
        } catch (error) {
            this.sales = []
            this.totalRecords = 0
            this.loadingTable = false
        }
    }
    private prepareTableParams(
        lazyTable: TableLazyLoadEvent
    ): IFilterTable<ICattleSalesFilterTable> {
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
    const columnAction = await this._helperService.showActionsTable();
    if (columnAction) {
      this.cols.push(columnAction);
    }
  }
}
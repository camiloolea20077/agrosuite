import { Component } from '@angular/core';
import { ColsModel } from 'src/app/shared/utils/models/cols.model';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { CattleTransfersTableModel } from '../../../domain/models/cattle-tranfers-table.model';
import { ICattleTransfersFilterTable } from '../../../domain/models/cattle-tranfers-filter-table.model';
import { HelpersService } from 'src/app/shared/utils/pipes/helper.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { CattleTransferService } from '../../../infraestructure/cattle-transfers.service';
import { ListElementService } from 'src/app/core/services/list-element.service';
import { AlertService } from 'src/app/shared/utils/pipes/alert.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { lastValueFrom } from 'rxjs';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { InventoryModule } from 'src/app/modules/inventory/inventory.module';
import { CattleSalesModule } from 'src/app/modules/cattle-sales/cattle-sales.module';
import { TooltipModule } from 'primeng/tooltip';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { TagModule } from 'primeng/tag';
import { ProgressSpinnerModule } from 'primeng/progressspinner';


@Component({
    selector: 'app-cattle-transfers-form',
    standalone: true,
    templateUrl: './index-cattle-transfers.component.html',
    styleUrls: ['./index-cattle-transfers.component.scss'],
    imports: [
        RouterModule,
        InputTextModule,
        ConfirmDialogModule,
        ButtonModule,
        TableModule,
        TagModule,
        ProgressSpinnerModule,
        CommonModule,
        InventoryModule,
        CattleSalesModule,
        TooltipModule,
        DialogModule,
        DropdownModule,
        FormsModule
    ],
    providers: [ConfirmationService, MessageService],
})
export class IndexCattleTransfersFormComponent {
    public rowSize = 10;
    public totalRecords = 0;
    public loadingTable = true;
    filtersTable!: IFilterTable<ICattleTransfersFilterTable>;
    transfers: CattleTransfersTableModel[] = [];
    cols: ColsModel[] = [
        {
            field: 'id',
            header: '# Transferencia',
            type: 'number',
            nameClass: 'text-center',
            minWidth: 'min-width: 40px;',
        },
        {
            field: 'transfer_type',
            header: 'Tipo de Transferencia',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 120px;',
        },
        {
            field: 'origin_farm',
            header: 'Finca Origen',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 150px;',
        },
        {
            field: 'destination_farm',
            header: 'Finca Destino',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 150px;',
        },
        {
            field: 'transfer_date',
            header: 'Fecha Transferencia',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 40px;',
        },
        {
            field: 'observations',
            header: 'Observaciones',
            type: 'string',
            nameClass: 'text-center',
        },
        {
            field: 'created_by',
            header: 'Creado Por',
            type: 'string',
            nameClass: 'text-center',
        },
    ];
    constructor(
        readonly _helperService: HelpersService,
        private readonly _confirmationService: ConfirmationService,
        private readonly cattleTransferService: CattleTransferService,
        private router: Router,
        private listElementService: ListElementService,
        private readonly _alertService: AlertService,
        private readonly _activatedRoute: ActivatedRoute,
        private messageService: MessageService
    ) { }
    ngOnInit(): void {
        this.loadTable;
        this.loadColumnActions();
    }
    async loadTable(lazyTable: TableLazyLoadEvent): Promise<void> {
        this.loadingTable = true;
        this.filtersTable = this.prepareTableParams(lazyTable);

        try {
            const response = await lastValueFrom(
                this.cattleTransferService.pageTransfers(this.filtersTable)
            );
            this.transfers = response.data?.content ?? [];
            this.totalRecords = response.data?.totalElements ?? 0;
            this.loadingTable = false;
        } catch (error) {
            this.transfers = [];
            this.totalRecords = 0;
            this.loadingTable = false;
        }
    }
    private prepareTableParams(
        lazyTable: TableLazyLoadEvent
    ): IFilterTable<ICattleTransfersFilterTable> {
        this.rowSize = lazyTable.rows ?? this.rowSize;
        const currentPage = lazyTable.first
            ? Math.floor(lazyTable.first / this.rowSize)
            : 0;
        return {
            page: currentPage,
            rows: this.rowSize,
            search: lazyTable.globalFilter,
            order: lazyTable.sortOrder === -1 ? 'desc' : 'asc',
            order_by: lazyTable.sortField ?? 'id',
        };
    }
    async loadColumnActions(): Promise<void> {
        const columnAction = await this._helperService.showActionsTable();
        if (columnAction) {
            this.cols.push(columnAction);
        }
    }
}

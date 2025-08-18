import { Component } from "@angular/core";
import { BirthsTableModel } from "../../domain/models/births-table.models";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { IBirthsFilterTable } from "../../domain/models/births-filter-table.model";
import { ColsModel } from "src/app/shared/utils/models/cols.model";
import { HelpersService } from "src/app/shared/utils/pipes/helper.service";
import { Router, RouterModule } from "@angular/router";
import { BirthsService } from "src/app/core/services/births.service";
import { TableLazyLoadEvent, TableModule } from "primeng/table";
import { lastValueFrom } from "rxjs";
import { InputTextModule } from "primeng/inputtext";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ButtonModule } from "primeng/button";
import { CommonModule } from "@angular/common";
import { ToastModule } from "primeng/toast";
import { ConfirmationService, MessageService } from "primeng/api";
import { TagModule } from 'primeng/tag';
import { ProgressSpinnerModule } from 'primeng/progressspinner';


@Component({
    selector: "app-index-births",
    standalone: true,
    templateUrl: "./index-births.component.html",
    styleUrls: ["./index-births.component.scss"],
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
        ProgressSpinnerModule
    ]
})
export class IndexBirthsComponent {
    globalFilter: string = '';
    public rowSize = 10
    public totalRecords = 0
    public loadingTable = true
    births: BirthsTableModel[] = []
    filtersTable!: IFilterTable<IBirthsFilterTable>
    cols: ColsModel[] = [
        {
            field: 'numero_toro',
            header: 'De Toro',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 150px;',
        },
        {
            field: 'numero_ganado',
            header: 'De Vaca',
            type: 'string',
            nameClass: 'text-center',
            minWidth: 'min-width: 150px;',
        },
        {
            field: 'sexo',
            header: 'Sexo',
            type: 'string',
            nameClass: 'text-center',
        },
        {
            field: 'color_cria',
            header: 'Color Cria',
            type: 'string',
            nameClass: 'text-left',
            minWidth: 'min-width: 40px;',
        },
        {
            field: 'peso_cria',
            header: 'Peso Cria',
            type: 'string',
            nameClass: 'text-left',
            minWidth: 'min-width: 40px;',
        },
        {
            field: 'numero_cria',
            header: 'Numero Cria',
            type: 'string',
            nameClass: 'text-left',
            minWidth: 'min-width: 40px;',
        },
        {
            field: 'fecha_nacimiento',
            header: 'Fecha Nacimiento',
            type: 'string',
            nameClass: 'text-left',
        },
        {
            field: 'observaciones',
            header: 'Observaciones',
            type: 'string',
            nameClass: 'text-left',
        },
    ];
    constructor(
        readonly _helperService: HelpersService,
        private readonly _confirmationService: ConfirmationService,
        private birthsService: BirthsService,
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
                this.birthsService.pageBirths(this.filtersTable)
            )
            this.births = response.data?.content ?? []
            this.totalRecords = response.data?.totalElements ?? 0
            this.loadingTable = false
        } catch (error) {
            this.births = []
            this.totalRecords = 0
            this.loadingTable = false
        }
    }
    private prepareTableParams(
        lazyTable: TableLazyLoadEvent
    ): IFilterTable<IBirthsFilterTable> {
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
getColumnIcon(field: string): string {
  const iconMap: { [key: string]: string } = {
    'numero_toro': 'pi pi-hashtag',
    'numero_ganado': 'pi pi-hashtag',
    'numero_cria': 'pi pi-hashtag',
    'color_cria': 'pi pi-tags',
    'sexo': 'pi pi-venus-mars',
    'fecha_nacimiento': 'pi pi-calendar',
    'lote_ganado': 'pi pi-chart-line',
    'peso_cria': 'pi pi-chart-line',
    'observaciones': 'pi pi-comment',
    'estado': 'pi pi-circle',
    'activo': 'pi pi-power-off'
  };
  return iconMap[field] || 'pi pi-circle';
}

// Método para obtener etiqueta de estado
getEstadoLabel(activo: number): string {
  switch (activo) {
    case 1:
      return 'Activo';
    case 2:
      return 'Inactivo';
    default:
      return 'Desconocido';
  }
}

// Método para obtener severidad de estado
getEstadoSeverity(activo: number): string {
  switch (activo) {
    case 1:
      return 'success';
    case 2:
      return 'danger';
    default:
      return 'info';
  }
}
// Método para obtener campos de filtro global
getFilterFields(): string[] {
  return this.cols
    .filter(col => col.field !== 'actions')
    .map(col => col.field);
}
  filterGlobal(event: Event) {
    this.loadTable({
      first: 0,
      rows: this.rowSize,
      globalFilter: (event.target as HTMLInputElement)?.value ?? '',
    })
  }
}
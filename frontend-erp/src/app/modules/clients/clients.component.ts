import { Component, Input } from '@angular/core';
import {
    FormBuilder,
    FormGroup,
    FormsModule,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ClientDto } from 'src/app/core/models/clients/clients.model';
import { ClientesService } from 'src/app/core/services/clientes.service';
import { ColsModel } from 'src/app/shared/utils/models/cols.model';
import { TableLazyLoadEvent, TableModule } from 'primeng/table'
import { CommonModule } from '@angular/common';
import { ClientsTableModel } from 'src/app/core/models/clients/clients-table.model';
import { lastValueFrom } from 'rxjs';
import { IFilterTable } from 'src/app/shared/utils/models/filter-table';
import { IClienteFilterTable } from 'src/app/core/models/clients/cliente-filter-table.model';
import { InputTextModule } from 'primeng/inputtext';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { SidebarComponent } from "../../shared/components/sidebar/sidebar.component";

@Component({
    selector: 'app-clients',
    standalone: true,
    templateUrl: './clients.component.html',
    styleUrls: ['./clients.component.scss'],
    providers: [MessageService],
    imports: [FormsModule,
        RouterModule,
        InputTextModule,
        ButtonModule, ReactiveFormsModule, TableModule, CommonModule],
})
export class ClientsComponent {
    @Input() clientData: ClientDto | null = null;
    clientForm!: FormGroup;
    isEditMode: boolean = false;
    public rowSize = 10
    public totalRecords = 0
    public loadingTable = true
    clients: ClientsTableModel[] = []
    filtersTable!: IFilterTable<IClienteFilterTable>
    cols: ColsModel[] = [
        {
            field: 'document_type',
            header: 'Tipo Documento',
            type: 'number',
            nameClass: 'text-center',
        },
        {
            field: 'document',
            header: 'Documento',
            type: 'number',
            nameClass: 'text-center',
        },
        {
            field: 'company_name',
            header: 'Nombre Empresa',
            type: 'string',
            nameClass: 'text-center',
        },
        {
            field: 'name',
            header: 'Nombre',
            type: 'string',
            nameClass: 'text-left',
        },
        {
            field: 'email',
            header: 'Correo',
            type: 'string',
            nameClass: 'text-left',
        },
        {
            field: 'phone',
            header: 'Telefono',
            type: 'string',
            nameClass: 'text-left',
        },
    ];
    constructor(
        private fb: FormBuilder,
        private clientesService: ClientesService,
        private router: Router,
        private messageService: MessageService
    ) { }

    ngOnInit(): void {
        this.clientForm = this.fb.group({
            name: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            phone: ['', Validators.required],
            document_type: ['', Validators.required],
            document: ['', Validators.required],
        });

        if (this.clientData) {
            this.isEditMode = true;
            this.clientForm.patchValue(this.clientData);
        }
    }

    async loadTable(lazyTable: TableLazyLoadEvent): Promise<void> {
        this.loadingTable = true
        this.filtersTable = this.prepareTableParams(lazyTable)

        try {
            const response = await lastValueFrom(
                this.clientesService.pageClients(this.filtersTable)
            )
            this.clients = response.data?.content ?? []
            this.totalRecords = response.data?.totalElements ?? 0
            this.loadingTable = false
        } catch (error) {
            this.clients = []
            this.totalRecords = 0
            this.loadingTable = false
        }
    }
    private prepareTableParams(
        lazyTable: TableLazyLoadEvent
    ): IFilterTable<IClienteFilterTable> {
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

    onSubmit(): void {
        if (this.clientForm.invalid) {
            return;
        }

        const clientData: ClientDto = this.clientForm.value;

        this.clientesService.createClient(clientData).subscribe(
            () => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Cliente creado',
                    detail: 'El cliente ha sido creado exitosamente.',
                });
                this.router.navigate(['/clientes']);
            },
            (error) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'Hubo un error al crear el cliente.',
                });
            }
        );
    }
}

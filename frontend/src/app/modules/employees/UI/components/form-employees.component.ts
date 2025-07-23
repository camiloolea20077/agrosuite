import { CommonModule } from "@angular/common";
import { Component, ViewEncapsulation } from "@angular/core";
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
import { MessageService } from "primeng/api";
import { CalendarModule } from "primeng/calendar";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { DropdownModule } from "primeng/dropdown";
import { InputSwitchModule } from "primeng/inputswitch";
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from "primeng/inputtextarea";
import { ToastModule } from "primeng/toast";
import { EmployeesService } from "src/app/core/services/employees.service";
import { AlertService } from "src/app/shared/utils/pipes/alert.service";
import { CreateEmployeesDto } from "../../domain/dto/create-employees.dto";
import { UpdateEmployeesDto } from "../../domain/dto/update-employees.dto";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { EmployeesModel } from "../../domain/models/employees.model";
import { lastValueFrom } from "rxjs";

@Component({
    selector: "app-form-employees",
    standalone: true,
    templateUrl: "./form-employees.component.html",
    styleUrls: ["./form-employees.component.scss"],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule,
        InputSwitchModule,
        DropdownModule,
        RouterModule,
        CalendarModule,
        InputTextareaModule,
        InputTextModule,
        ToastModule,
        ConfirmDialogModule
    ],
    encapsulation: ViewEncapsulation.None,
})
export class FormEmployeesComponent {
    public frm!: FormGroup;
    id!: number
    slug: string | null = 'create';
    constructor(
        private readonly employeesService: EmployeesService,
        private readonly _router: Router,
        private readonly messageService: MessageService,
        private readonly _alertService: AlertService,
        private readonly formBuilder: FormBuilder,
        public readonly _activatedRoute: ActivatedRoute
    ) {
        this.slug = this._activatedRoute.snapshot.data['slug'];
        this.id = Number(this._activatedRoute.snapshot.params['id'])
    }
    async ngOnInit() {
        this.loadForm();
        if (this.slug === 'edit') {
            this.loadEmployeesById(this.id);
        }
    }

    loadForm() {
        this.frm = this.formBuilder.group({
            activo: [true],
            nombre: [null, Validators.required],
            identificacion: [null, Validators.required],
            cargo: [null, Validators.required],
            fecha_ingreso: [null, Validators.required],
        });
    }

    async buildDataEmployees(): Promise<CreateEmployeesDto | UpdateEmployeesDto> {
        const formValue = this.frm.value;
        return {
            activo: formValue.activo ? 1 : 2,
            nombre: this.frm.controls['nombre'].value,
            identificacion: this.frm.controls['identificacion'].value,
            cargo: this.frm.controls['cargo'].value,
            fecha_ingreso: this.formatDateToYYYYMMDD(this.frm.controls['fecha_ingreso'].value),
        }
    }

    async buildSaveEmployees(): Promise<void>{
        const msgSystem = 'Alerta del sistema';
        const msgText = 'Complete el formulario correctamente';

        if (this.isFormInvalid()) {
            this.markFormAsTouched();
            this._alertService.showError(msgSystem, msgText);
            return;
        }
        const data: CreateEmployeesDto | UpdateEmployeesDto = await this.buildDataEmployees();
        try {
            const response = await this.saveEmployees(data);
            if (response) this.handleResponse(response);
        } catch (error) {
            const msg = 'Error al guardar el empleado';
            this.showErrorMessage(msg);
        }
    }
    private handleResponse(response: ResponseModel<EmployeesModel | boolean>): void {
        if (response?.status === 200 || response?.status === 201) {
        const message = this.slug === 'edit' 
            ? 'Empleado actualizado correctamente'
            : 'Empleado creado correctamente';
            this.messageService.add({
                severity: 'success',
                summary: 'Operación exitosa',
                detail: message,
                life: 5000,
            });
            if (response?.status === 201) {
                this._router.navigate(['/employees']);
            }
        }
    }
    private async saveEmployees(data: CreateEmployeesDto | UpdateEmployeesDto): Promise<ResponseModel<boolean | EmployeesModel> | void> {
        if (this.slug === 'create') {
            return lastValueFrom(
                this.employeesService.createEmployees(data as CreateEmployeesDto)
            ).catch((error) => {
                this.showErrorMessage(error.message);
            });
        } if (this.slug === 'edit') {
            return await lastValueFrom(
                this.employeesService.updateEmployees(this.id, data as UpdateEmployeesDto)
            ).catch((error) => {
                this.showErrorMessage(error.message)
            })
        }
        throw new Error('Slug inválido');
    }
    private formatDateToYYYYMMDD(date: Date | string): string {
        const d = new Date(date);
        const year = d.getFullYear();
        const month = ('0' + (d.getMonth() + 1)).slice(-2);
        const day = ('0' + d.getDate()).slice(-2);
        return `${year}-${month}-${day}`;
    }
    isFormInvalid(): boolean {
        return this.frm.invalid;
    }
    markFormAsTouched(): void {
        this.frm.markAllAsTouched();
    }
    private showErrorMessage(message: string): void {
        const msgSystem = 'Alerta del sistema';
        this._alertService.showError(msgSystem, message ?? 'Ok');
    }
    resetForm() {
        this.frm.reset();
    }
    async loadEmployeesById(id:number):Promise<void>{
        const response = await lastValueFrom(
            this.employeesService.getEmployeesById(id)
        ).catch((error) => {
            this.showErrorMessage(error.message);
        })
        if (response && response.status == 200) {
            this.frm.patchValue({
                id: response.data.id,
                activo: response.data.activo === 1 ? true : false,
                nombre: response.data.nombre,
                identificacion: response.data.identificacion,
                cargo: response.data.cargo,
                fecha_ingreso: response.data.fecha_ingreso,
            })
        }
    }
}
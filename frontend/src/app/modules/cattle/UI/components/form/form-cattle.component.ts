import { Component, OnInit } from '@angular/core';
import {
    FormBuilder,
    FormGroup,
    FormsModule,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputSwitchModule } from 'primeng/inputswitch';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { lastValueFrom } from 'rxjs';
import { CattleModel } from 'src/app/core/models/cattle/cattle.models';
import { CreateCattleDto } from 'src/app/core/models/cattle/create-cattle.dto';
import { CattleService } from 'src/app/core/services/cattle.service';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';
import { AlertService } from 'src/app/shared/utils/pipes/alert.service';
import { ConfirmationService, MessageService } from 'primeng/api'
import { CommonModule } from '@angular/common';
import { UpdateCattleDto } from 'src/app/core/models/cattle/update-cattle.dto';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from "primeng/confirmdialog";

@Component({
    selector: 'app-form-cattle',
    standalone: true,
    templateUrl: './form-cattle.component.html',
    styleUrls: ['./form-cattle.component.scss'],
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
})
export class FormCattleComponent implements OnInit {
    id!: number
    public frmGanado!: FormGroup;
    slug: string | null = 'create';
    sexOptions = [
        { name: 'Macho', value: 'Macho' },
        { name: 'Hembra', value: 'Hembra' },
    ];
    constructor(
        private readonly cattleService: CattleService,
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
            this.loadCattleById(this.frmGanado.controls['id'].value);
        }
    }

    loadForm() {
        this.frmGanado = this.formBuilder.group({
            id: [this.id],
            activo: [true],
            tipo_ganado: [null, Validators.required],
            numero_ganado: [null, Validators.required],
            sexo: [null, Validators.required],
            color: [null, Validators.required],
            lote_ganado: [null, Validators.required],
            fecha_nacimiento: [null, Validators.required],
            peso: [null, Validators.required],
            observaciones: [null, Validators.required],
        });
    }
    resetForm() {
        this.frmGanado.reset();
    }
    async buildDataCattle(): Promise<CreateCattleDto | UpdateCattleDto> {
        const formValue = this.frmGanado.value;
        return {
            id: this.id,
            tipo_ganado: this.frmGanado.controls['tipo_ganado'].value,
            numero_ganado: this.frmGanado.controls['numero_ganado'].value,
            sexo: this.frmGanado.controls['sexo'].value,
            color: this.frmGanado.controls['color'].value,
            peso: this.frmGanado.controls['peso'].value,
            fecha_nacimiento: this.formatDateToYYYYMMDD(this.frmGanado.controls['fecha_nacimiento'].value),
            lote_ganado: this.frmGanado.controls['lote_ganado'].value,
            observaciones: this.frmGanado.controls['observaciones'].value,
            activo: formValue.activo ? 1 : 2,
        };
    }
    async buildsaveCattle(): Promise<void> {
        const msgSystem = 'Alerta del sistema';
        const msgText = 'Complete el formulario correctamente';

        if (this.isFormInvalid()) {
            this.markFormAsTouched();
            this._alertService.showError(msgSystem, msgText);
            return;
        }
        const data: CreateCattleDto | UpdateCattleDto = await this.buildDataCattle();
        try {
            const response = await this.saveCattle(data);
            if (response) this.handleResponse(response);
        } catch (error) {
            const msg = 'Error al guardar el ganado';
            this.showErrorMessage(msg);
        }
    }
    private formatDateToYYYYMMDD(date: Date | string): string {
        const d = new Date(date);
        const year = d.getFullYear();
        const month = ('0' + (d.getMonth() + 1)).slice(-2);
        const day = ('0' + d.getDate()).slice(-2);
        return `${year}-${month}-${day}`;
    }

    isFormInvalid(): boolean {
        return this.frmGanado.invalid;
    }
    markFormAsTouched(): void {
        this.frmGanado.markAllAsTouched();
    }
    private handleResponse(response: ResponseModel<CattleModel | boolean>): void {
        if (response?.status === 200 || response?.status === 201) {
            const message = this.slug === 'create'
                ? 'Ganado creado correctamente'
                : 'Ganado actualizado correctamente';
            this.messageService.add({
                severity: 'success',
                summary: 'Operación exitosa',
                detail: message,
                life: 5000,
            });
            if (response?.status === 201) {
                this._router.navigate(['/cattle']);
            }
        }
    }
    private async saveCattle(
        data: CreateCattleDto | UpdateCattleDto
    ): Promise<ResponseModel<boolean | CattleModel> | void> {
        if (this.slug === 'create') {
            return lastValueFrom(
                this.cattleService.createCattle(data as CreateCattleDto)
            ).catch((error) => {
                this.showErrorMessage(error.message);
            });
        } if (this.slug === 'edit') {
            return await lastValueFrom(
                this.cattleService.updateCattle(this.id, data as UpdateCattleDto)
            ).catch((error) => {
                this.showErrorMessage(error.message)
            })
        }
        throw new Error('Slug inválido');
    }
    private showErrorMessage(message: string): void {
        const msgSystem = 'Alerta del sistema';
        this._alertService.showError(msgSystem, message ?? 'Ok');
    }
    async loadCattleById(id: number): Promise<void> {
        const response = await lastValueFrom(
            this.cattleService.getCattleById(id)
        ).catch((error) => {
            this.showErrorMessage(error.message);
        })
        if (response && response.status == 200) {
            this.frmGanado.patchValue({
                id: response.data.id,
                activo: response.data.activo === 1 ? true : false,
                tipo_ganado: response.data.tipo_ganado,
                numero_ganado: response.data.numero_ganado,
                sexo: response.data.sexo,
                color: response.data.color,
                peso: response.data.peso,
                fecha_nacimiento: response.data.fecha_nacimiento,
                lote_ganado: response.data.lote_ganado,
                observaciones: response.data.observaciones,
            })
        }
    }
}

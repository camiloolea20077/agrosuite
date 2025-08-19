import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
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
import { MessageService } from 'primeng/api'
import { CommonModule } from '@angular/common';
import { UpdateCattleDto } from 'src/app/core/models/cattle/update-cattle.dto';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { PanelModule } from 'primeng/panel';
// Para el botón mejorado
import { ButtonModule } from 'primeng/button';

// Para el chip en el header
import { ChipModule } from 'primeng/chip';

// Para el separador entre secciones
import { DividerModule } from 'primeng/divider';

// Para el input numérico del peso
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from "primeng/dialog";
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
    ConfirmDialogModule,
    PanelModule,
    ButtonModule,
    ChipModule,
    DividerModule,
    InputNumberModule,
    DialogModule
],
})
export class FormCattleComponent implements OnInit {
    @Input() displayModal: boolean = false;
    @Input() cattleId: number | null = null;
    @Input() slug: string = 'create'
    @Output() modalClosed = new EventEmitter<void>();
    @Output() cattleSaved = new EventEmitter<CattleModel>();

    public frmGanado!: FormGroup;
    public isEditMode: boolean = false;
    public isSubmitting: boolean = false;
    public today: Date = new Date();
    
    // Variables para controlar la visibilidad de secciones
    public showPregnancySection: boolean = false;
    public showPregnancyDate: boolean = false;

    tipoAnimalOptions = [
        { name: 'Vaca', value: 'vaca' },
        { name: 'Toro', value: 'toro' },
        { name: 'Ternera', value: 'ternera' },
        { name: 'Ternero', value: 'ternero' },
    ];

    sexOptions = [
        { name: 'Macho', value: 'Macho' },
        { name: 'Hembra', value: 'Hembra' },
    ];

    embarazadaOptions = [
        { name: 'No embarazada', value: 0 },
        { name: 'Embarazada confirmada', value: 1 },
        { name: 'Embarazo por confirmar', value: 2 },
    ];

    constructor(
        private readonly cattleService: CattleService,
        private readonly messageService: MessageService,
        private readonly _alertService: AlertService,
        private readonly formBuilder: FormBuilder
    ) {}

    ngOnInit() {
        this.loadForm();
    }

    ngOnChanges() {
        if (this.displayModal) {
            this.isEditMode = !!this.cattleId;
            
            if (this.isEditMode && this.cattleId) {
                this.cattleService.getCattleById(this.cattleId).subscribe(
                    (response) => {
                        if (response && response.data) {
                            this.loadCattleData(response.data);
                        }
                    },
                    (error) => {
                        this._alertService.showError('Error', 'No se pudo cargar la información del ganado');
                    }
                );
            } else {
                this.resetForm();
            }
        }
    }

    loadForm() {
        this.frmGanado = this.formBuilder.group({
            id: [null],
            activo: [true],
            tipo_ganado: [null, Validators.required],
            tipo_animal: [null, Validators.required],
            numero_ganado: [null, Validators.required],
            sexo: [null, Validators.required],
            color: [null, Validators.required],
            lote_ganado: [null, Validators.required],
            fecha_nacimiento: [null, Validators.required],
            peso: [null, Validators.required],
            embarazada: [0], // Default: no embarazada
            fecha_embarazo: [null],
            observaciones: [null],
        });

        // Suscribirse a cambios en tipo_animal para mostrar/ocultar sección de embarazo
        this.frmGanado.get('tipo_animal')?.valueChanges.subscribe((value) => {
            this.updatePregnancySectionVisibility(value);
        });

        // Suscribirse a cambios en embarazada para mostrar/ocultar fecha de embarazo
        this.frmGanado.get('embarazada')?.valueChanges.subscribe((value) => {
            this.updatePregnancyDateVisibility(value);
        });
    }

    onTipoGanadoChange(event: any) {
        // Aquí podrías filtrar los tipos de animal según el tipo de ganado
        // Por ejemplo, si es bovino, mostrar solo vaca, toro, ternero, ternera
    }

    onTipoAnimalChange(event: any) {
        const tipoAnimal = event.value;
        this.updatePregnancySectionVisibility(tipoAnimal);
        
        // Si no es vaca, resetear los campos de embarazo
        if (tipoAnimal !== 'vaca') {
            this.frmGanado.patchValue({
                embarazada: 0,
                fecha_embarazo: null
            });
        }
    }

    onEmbarazadaChange(event: any) {
        const embarazadaValue = event.value;
        this.updatePregnancyDateVisibility(embarazadaValue);
        
        // Si no está embarazada, limpiar fecha de embarazo
        if (embarazadaValue === 0) {
            this.frmGanado.patchValue({
                fecha_embarazo: null
            });
        }
    }

    private updatePregnancySectionVisibility(tipoAnimal: string) {
        this.showPregnancySection = tipoAnimal === 'vaca';
    }

    private updatePregnancyDateVisibility(embarazadaValue: number) {
        this.showPregnancyDate = embarazadaValue === 1 || embarazadaValue === 2;
    }

    loadCattleData(cattle: CattleModel) {
        this.frmGanado.patchValue({
            id: cattle.id,
            activo: cattle.activo === 1,
            tipo_ganado: cattle.tipo_ganado,
            tipo_animal: cattle.tipo_animal || 'vaca',
            numero_ganado: cattle.numero_ganado,
            sexo: cattle.sexo,
            color: cattle.color,
            peso: cattle.peso,
            fecha_nacimiento: new Date(cattle.fecha_nacimiento),
            lote_ganado: cattle.lote_ganado,
            embarazada: cattle.embarazada || 0,
            fecha_embarazo: cattle.fecha_embarazo ? new Date(cattle.fecha_embarazo) : null,
            observaciones: cattle.observaciones,
        });

        // Actualizar visibilidad de secciones
        this.updatePregnancySectionVisibility(cattle.tipo_animal || 'vaca');
        this.updatePregnancyDateVisibility(cattle.embarazada || 0);
    }

    resetForm() {
        this.frmGanado.reset({
            activo: true,
            embarazada: 0
        });
        this.showPregnancySection = false;
        this.showPregnancyDate = false;
    }

    async buildDataCattle(): Promise<CreateCattleDto | UpdateCattleDto> {
        const formValue = this.frmGanado.value;
        
        const baseData = {
            id: formValue.id,
            tipo_ganado: formValue.tipo_ganado,
            tipo_animal: formValue.tipo_animal,
            numero_ganado: formValue.numero_ganado,
            sexo: formValue.sexo,
            color: formValue.color,
            peso: formValue.peso,
            fecha_nacimiento: this.formatDateToYYYYMMDD(formValue.fecha_nacimiento),
            lote_ganado: formValue.lote_ganado,
            observaciones: formValue.observaciones,
            activo: formValue.activo ? 1 : 0,
        }
        if (formValue.tipo_animal === 'vaca') {
            return {
                ...baseData,
                embarazada: formValue.embarazada || 0,
                fecha_embarazo: formValue.fecha_embarazo && formValue.embarazada > 0 
                    ? (formValue.fecha_embarazo !== null ? this.formatDateToYYYYMMDD(formValue.fecha_embarazo) : null)
                    : null
            }
        }
        return {
            ...baseData,
            embarazada: 0,
            fecha_embarazo: null,
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

        this.isSubmitting = true;
        const data: CreateCattleDto | UpdateCattleDto = await this.buildDataCattle();
        
        try {
            const response = await this.saveCattle(data);
            if (response) {
                this.handleResponse(response);
            }
        } catch (error: any) {
            const msg = 'Error al guardar el ganado';
            this.showErrorMessage(msg);
        } finally {
            this.isSubmitting = false;
        }
    }

    private formatDateToYYYYMMDD(date: Date | string): string {
        if (!date) return '';
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
            const message = this.isEditMode
                ? 'Ganado actualizado correctamente'
                : 'Ganado creado correctamente';
            
            this.messageService.add({
                severity: 'success',
                summary: 'Operación exitosa',
                detail: message,
                life: 5000,
            });

            // Emitir evento de ganado guardado
            if (response.data && typeof response.data === 'object') {
                this.cattleSaved.emit(response.data as CattleModel);
            }

            this.closeModal();
        }
    }

    private async saveCattle(
        data: CreateCattleDto | UpdateCattleDto
    ): Promise<ResponseModel<boolean | CattleModel> | void> {
        if (this.isEditMode) {
            return await lastValueFrom(
                this.cattleService.updateCattle(data.id!, data as UpdateCattleDto)
            ).catch((error) => {
                this.showErrorMessage(error.message);
            });
        } else {
            return lastValueFrom(
                this.cattleService.createCattle(data as CreateCattleDto)
            ).catch((error) => {
                this.showErrorMessage(error.message);
            });
        }
    }

    private showErrorMessage(message: string): void {
        const msgSystem = 'Alerta del sistema';
        this._alertService.showError(msgSystem, message ?? 'Error desconocido');
    }

    closeModal() {
        this.displayModal = false;
        this.resetForm();
        this.modalClosed.emit();
    }

    onModalHide() {
        this.closeModal();
    }
}

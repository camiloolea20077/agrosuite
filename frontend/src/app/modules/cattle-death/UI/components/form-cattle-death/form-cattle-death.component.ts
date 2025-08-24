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
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';
import { AlertService } from 'src/app/shared/utils/pipes/alert.service';
import { MessageService } from 'primeng/api'
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { ChipModule } from 'primeng/chip';
import { DividerModule } from 'primeng/divider';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from "primeng/dialog";
import { CattleDeathModel } from '../../../domain/models/cattle-death.model';
import { CreateCattleDeathDto } from '../../../domain/dto/crate-cattle-death.dto';
import { UpdateCattleDeathDto } from '../../../domain/dto/update-cattle-death.dto';
import { CattleDeathService } from '../../../infraestructure/cattle-death.service';
import { TagModule } from "primeng/tag";
import { BirthsService } from 'src/app/core/services/births.service';
import { CattleService } from 'src/app/core/services/cattle.service';
import { BirthsList } from 'src/app/modules/births/domain/models/births-list.model';
import { CattleList } from 'src/app/core/models/cattle/cattle-list.dto';


@Component({
    selector: 'app-form-cattle-death',
    standalone: true,
    templateUrl: './form-cattle-death.component.html',
    styleUrls: ['./form-cattle-death.component.scss'],
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
    DialogModule,
    TagModule
],
})
export class FormCattleDeathComponent implements OnInit {
    @Input() displayModal: boolean = false;
    @Input() cattleDeathId: number | null = null;
    @Input() cattleId: number | null = null; // ID del ganado que murió
    @Input() birthId: number | null = null; // ID del nacimiento relacionado
    @Input() slug: string = 'create'
    @Output() modalClosed = new EventEmitter<void>();
    @Output() cattleDeathSaved = new EventEmitter<CattleDeathModel>();
    public tipoMuerte: string = 'ganado'
    public frmCattleDeath!: FormGroup;
    public isEditMode: boolean = false;
    public isSubmitting: boolean = false;
    public today: Date = new Date();
    births: BirthsList[] = [];
    cattles: CattleList [] = [];

    tipoMuerteOptions = [
        { name: 'Ganado Adulto', value: 'ganado' },
        { name: 'Ternero/Cría', value: 'ternero' }
    ];
    // Opciones para los dropdowns
    motivoMuerteOptions = [
        { name: 'Enfermedad', value: 'enfermedad' },
        { name: 'Accidente', value: 'accidente' },
        { name: 'Parto complicado', value: 'parto_complicado' },
        { name: 'Vejez', value: 'vejez' },
        { name: 'Depredador', value: 'depredador' },
        { name: 'Intoxicación', value: 'intoxicacion' },
        { name: 'Desconocido', value: 'desconocido' },
        { name: 'Otro', value: 'otro' }
    ];

    causaCategoriaOptions = [
        { name: 'Natural', value: 'natural' },
        { name: 'Accidental', value: 'accidental' },
        { name: 'Enfermedad', value: 'enfermedad' },
        { name: 'Reproductiva', value: 'reproductiva' },
        { name: 'Nutricional', value: 'nutricional' },
        { name: 'Infecciosa', value: 'infecciosa' },
        { name: 'Externa', value: 'externa' },
        { name: 'Desconocida', value: 'desconocida' }
    ];

    constructor(
        private readonly cattleDeathService: CattleDeathService, // Servicio específico para muertes
        private readonly messageService: MessageService,
        private readonly _alertService: AlertService,
        private readonly formBuilder: FormBuilder,
        private readonly birthsService: BirthsService,
        private readonly cattleService: CattleService
    ) {}

    ngOnInit() {
        this.getBirth();
        this.getCattle()
        this.loadForm();
    }

ngOnChanges() {
    if (this.displayModal) {
        this.isEditMode = !!this.cattleDeathId;
        
        if (this.isEditMode && this.cattleDeathId) {
            // Cargar datos existentes
            this.cattleDeathService.getCattleDeathById(this.cattleDeathId).subscribe(
                (response) => {
                    if (response && response.data) {
                        this.loadCattleDeathData(response.data);
                    }
                },
                (error) => {
                    this._alertService.showError('Error', 'No se pudo cargar la información de la muerte del ganado');
                }
            );
        } else {
            // Nuevo registro - determinar el tipo basándose en los parámetros de entrada
            if (this.cattleId) {
                this.tipoMuerte = 'ganado';
            } else if (this.birthId) {
                this.tipoMuerte = 'ternero';
            } else {
                this.tipoMuerte = 'ganado'; // valor por defecto
            }
            
            this.resetForm();
            
            // Establecer los valores iniciales si están disponibles
            if (this.cattleId) {
                this.frmCattleDeath.patchValue({ cattleId: this.cattleId });
            }
            if (this.birthId) {
                this.frmCattleDeath.patchValue({ birthId: this.birthId });
            }
        }
    }
}
  getBirth():void {
    this.birthsService.getAllBirths().subscribe((response) => {
        this.births = response.data
    })
  }
    getCattle():void {
    this.cattleService.getAllCattle().subscribe((response) => {
        this.cattles = response.data
    })
  }
    loadForm() {
        this.frmCattleDeath = this.formBuilder.group({
            id: [null],
            cattleId: [this.cattleId, Validators.required],
            birthId: [this.birthId, Validators.required],
            fechaMuerte: [null, Validators.required],
            motivoMuerte: [null, Validators.required],
            descripcionDetallada: [null, [Validators.required, Validators.minLength(10)]],
            pesoMuerte: [null, [Validators.required, Validators.min(1)]],
            causaCategoria: [null, Validators.required]
        });
                // Configurar validaciones dinámicas
        this.updateValidationsByType(this.tipoMuerte);
    }

    loadCattleDeathData(cattleDeath: CattleDeathModel) {
    
    // Determinar el tipo basándose en qué ID tiene valor
    if (cattleDeath.cattleId && cattleDeath.cattleId !== null) {
        this.tipoMuerte = 'ganado';
    } else if (cattleDeath.birthId && cattleDeath.birthId !== null) {
        this.tipoMuerte = 'ternero';
    }
    
    const formData = {
        id: cattleDeath.id,
        cattleId: cattleDeath.cattleId,
        birthId: cattleDeath.birthId,
        fechaMuerte: new Date(cattleDeath.fechaMuerte),
        motivoMuerte: cattleDeath.motivoMuerte,
        descripcionDetallada: cattleDeath.descripcionDetallada,
        pesoMuerte: parseFloat(cattleDeath.pesoMuerte),
        causaCategoria: cattleDeath.causaCategoria
    };
    this.frmCattleDeath.patchValue(formData);
    this.updateValidationsByType(this.tipoMuerte);
    }

    private updateValidationsByType(tipo: string): void {
        const cattleIdControl = this.frmCattleDeath.get('cattleId');
        const birthIdControl = this.frmCattleDeath.get('birthId');

        // Limpiar todas las validaciones primero
        cattleIdControl?.clearValidators();
        birthIdControl?.clearValidators();

        if (tipo === 'ganado') {
            cattleIdControl?.setValidators([Validators.required]);
            // Solo limpiar el valor si NO estamos en modo edición
            if (!this.isEditMode) {
                this.frmCattleDeath.patchValue({ birthId: null });
            }
        } else if (tipo === 'ternero') {
            birthIdControl?.setValidators([Validators.required]);
            // Solo limpiar el valor si NO estamos en modo edición
            if (!this.isEditMode) {
                this.frmCattleDeath.patchValue({ cattleId: null });
            }
        }

        // Actualizar validaciones
        cattleIdControl?.updateValueAndValidity();
        birthIdControl?.updateValueAndValidity();
    }

    // CAMBIO: Método para manejar cambios desde el template
    onTipoMuerteChange(tipo: string): void {
        this.tipoMuerte = tipo;
        
        // Actualizar validaciones
        this.updateValidationsByType(tipo);
    }
    resetForm() {
        this.frmCattleDeath.reset({
            cattleId: this.cattleId,
            birthId: this.birthId
        });
    }

    async buildDataCattleDeath(): Promise<CreateCattleDeathDto | UpdateCattleDeathDto> {
        const formValue = this.frmCattleDeath.value;
        
        const baseData = {
            id: formValue.id,
            cattleId: this.isEditMode ? formValue.cattleId : (this.tipoMuerte === 'ganado' ? formValue.cattleId : null),
            birthId: this.isEditMode ? formValue.birthId : (this.tipoMuerte === 'ternero' ? formValue.birthId : null),
            fechaMuerte: this.formatDateToYYYYMMDD(formValue.fechaMuerte),
            motivoMuerte: formValue.motivoMuerte,
            descripcionDetallada: formValue.descripcionDetallada,
            pesoMuerte: formValue.pesoMuerte.toString(),
            causaCategoria: formValue.causaCategoria
        };
        
        return baseData;
    }

    async buildSaveCattleDeath(): Promise<void> {
            // Evitar múltiples ejecuciones si ya se está procesando
        if (this.isSubmitting) {
            return;
        }

        const msgSystem = 'Alerta del sistema';
        const msgText = 'Complete el formulario correctamente';

        if (this.isFormInvalid()) {
            this.markFormAsTouched();
            this._alertService.showError(msgSystem, msgText);
            return;
        }

        this.isSubmitting = true;
        const data: CreateCattleDeathDto | UpdateCattleDeathDto = await this.buildDataCattleDeath();
        
        try {
            const response = await this.saveCattleDeath(data);
            if (response) {
                this.handleResponse(response);
            }
        } catch (error: any) {
            const msg = 'Error al registrar la muerte del ganado';
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
        return this.frmCattleDeath.invalid;
    }

    markFormAsTouched(): void {
        this.frmCattleDeath.markAllAsTouched();
    }

    private handleResponse(response: ResponseModel<CattleDeathModel | boolean>): void {
        if (response?.status === 200 || response?.status === 201) {
            const message = this.isEditMode
                ? 'Registro de muerte actualizado correctamente'
                : 'Muerte de ganado registrada correctamente';
            // Emitir evento de muerte guardada
            if (response.data && typeof response.data === 'object') {
                this.cattleDeathSaved.emit(response.data as CattleDeathModel);
            }

            this.closeModal();
        }
    }

    private async saveCattleDeath(
        data: CreateCattleDeathDto | UpdateCattleDeathDto
    ): Promise<ResponseModel<boolean | CattleDeathModel> | void> {
            return await lastValueFrom(
                this.cattleDeathService.createCattleDeath(data as CreateCattleDeathDto)
            ).catch((error) => {
                this.showErrorMessage(error.message);
            });
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

    // Métodos de utilidad para obtener mensajes de error
    getFieldError(fieldName: string): string {
        const field = this.frmCattleDeath.get(fieldName);
        if (field?.errors && field.touched) {
            if (field.errors['required']) {
                return `El campo ${fieldName} es requerido`;
            }
            if (field.errors['minlength']) {
                return `El campo ${fieldName} debe tener al menos ${field.errors['minlength'].requiredLength} caracteres`;
            }
            if (field.errors['min']) {
                return `El peso debe ser mayor a 0`;
            }
        }
        return '';
    }

    isFieldInvalid(fieldName: string): boolean {
        const field = this.frmCattleDeath.get(fieldName);
        return !!(field?.invalid && field.touched);
    }
}
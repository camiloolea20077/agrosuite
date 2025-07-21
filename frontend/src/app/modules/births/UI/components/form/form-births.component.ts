import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
import { ConfirmationService, MessageService } from 'primeng/api'
import { CalendarModule } from "primeng/calendar";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { DropdownModule } from "primeng/dropdown";
import { InputSwitchModule } from "primeng/inputswitch";
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from "primeng/inputtextarea";
import { ToastModule } from "primeng/toast";
import { BirthsService } from "src/app/core/services/births.service";
import { ListElementService } from "src/app/core/services/list-element.service";
import { ListElementModel } from "src/app/shared/utils/models/list-element.model";
import { AlertService } from "src/app/shared/utils/pipes/alert.service";
import { UpdateBirthsDto } from "../../../domain/dto/update-births.dto";
import { CreateBirthsDto } from "../../../domain/dto/create-births.dto";
import { BirthsModel } from "../../../domain/models/births.model";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { lastValueFrom } from "rxjs";

@Component({
    selector: "app-form-births",
    standalone: true,
    templateUrl: "./form-births.component.html",
    styleUrls: ["./form-births.component.scss"],
    providers: [MessageService,ConfirmationService],
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
export class FormBirthsComponent {
    id!: number
    cattleFemales: ListElementModel[] = [];
    cattleMales: ListElementModel[] = [];
    public frm!: FormGroup;
    slug: string | null = 'create';
    sexOptions = [
        { name: 'Macho', value: 'Macho' },
        { name: 'Hembra', value: 'Hembra' },
    ];
    constructor(
        private readonly birthsService: BirthsService,
        private readonly _router: Router,
        private listElementService: ListElementService,
        private readonly messageService: MessageService,
        private readonly _alertService: AlertService,
        private readonly formBuilder: FormBuilder,
        public readonly _activatedRoute: ActivatedRoute
    ) {
        this.slug = this._activatedRoute.snapshot.data['slug'];
        this.id = Number(this._activatedRoute.snapshot.params['id'])
    }
    async ngOnInit() {
        this.getFemaleCattle();
        this.loadForm();
        this.getMaleCattle();
        if (this.slug === 'edit') {
            this.loadBirthById(this.id);
        }
    }
    loadForm() {
        this.frm = this.formBuilder.group({
            tipo_vaca: [null, Validators.required],
            nombre_toro: [null, Validators.required],
            fecha_nacimiento: [null, Validators.required],
            numero_cria: [null, Validators.required],
            sexo: [null, Validators.required],
            color_cria: [null, Validators.required],
            observaciones: [null, Validators.required],
        });
    }
    async buildDataBirth(): Promise<CreateBirthsDto | UpdateBirthsDto> {
        const formValue = this.frm.value;
        return {
            tipo_vaca: this.frm.controls['tipo_vaca'].value,
            nombre_toro: this.frm.controls['nombre_toro'].value,
            fecha_nacimiento: this.formatDateToYYYYMMDD(this.frm.controls['fecha_nacimiento'].value),
            numero_cria: this.frm.controls['numero_cria'].value,
            sexo: this.frm.controls['sexo'].value,
            color_cria: this.frm.controls['color_cria'].value,
            observaciones: this.frm.controls['observaciones'].value,
        };
    }
    async buildsaveBirth(): Promise<void> {
        const msgSystem = 'Alerta del sistema';
        const msgText = 'Complete el formulario correctamente';

        if (this.isFormInvalid()) {
            this.markFormAsTouched();
            this._alertService.showError(msgSystem, msgText);
            return;
        }
        const data: CreateBirthsDto | UpdateBirthsDto = await this.buildDataBirth();
        try {
            const response = await this.saveBirth(data);
            if (response) this.handleResponse(response);
        } catch (error) {
            const msg = 'Error al guardar el ganado';
            this.showErrorMessage(msg);
        }
    }
    private async saveBirth(
        data: CreateBirthsDto | UpdateBirthsDto
    ): Promise<ResponseModel<boolean | BirthsModel> | void> {
        if (this.slug === 'create') {
            return lastValueFrom(
                this.birthsService.createBirth(data as CreateBirthsDto)
            ).catch((error) => {
                this.showErrorMessage(error.message);
            });
        } if (this.slug === 'edit') {
            return await lastValueFrom(
                this.birthsService.updateBirth(this.id, data as UpdateBirthsDto)
            ).catch((error) => {
                this.showErrorMessage(error.message)
            })
        }
        throw new Error('Slug inválido');
    }
    private handleResponse(response: ResponseModel<BirthsModel | boolean>): void {
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
                this._router.navigate(['/births']);
            }
        }
    }
    async loadBirthById(id: number): Promise<void> {
        const response = await lastValueFrom(
            this.birthsService.getBirthById(id)
        ).catch((error) => {
            this.showErrorMessage(error.message);
        })
        if (response && response.status == 200) {
            this.frm.patchValue({
                tipo_vaca: response.data.tipo_vaca,
                nombre_toro: response.data.nombre_toro,
                fecha_nacimiento: response.data.fecha_nacimiento,
                numero_cria: response.data.numero_cria,
                sexo: response.data.sexo,
                color_cria: response.data.color_cria,
                observaciones: response.data.observaciones
            })
        }
    }
    private showErrorMessage(message: string): void {
        const msgSystem = 'Alerta del sistema';
        this._alertService.showError(msgSystem, message ?? 'Ok');
    }
    resetForm() {
        this.frm.reset();
    }
    isFormInvalid(): boolean {
        return this.frm.invalid;
    }
    markFormAsTouched(): void {
        this.frm.markAllAsTouched();
    }

    private formatDateToYYYYMMDD(date: Date | string): string {
        const d = new Date(date);
        const year = d.getFullYear();
        const month = ('0' + (d.getMonth() + 1)).slice(-2);
        const day = ('0' + d.getDate()).slice(-2);
        return `${year}-${month}-${day}`;
    }
    getFemaleCattle():void{
        this.listElementService.forListById().subscribe((response) => {
            this.cattleFemales = response.data;
        });
    }
    getMaleCattle():void{
        this.listElementService.forListByMale().subscribe((response) => {
            this.cattleMales = response.data;
        });
    }
}
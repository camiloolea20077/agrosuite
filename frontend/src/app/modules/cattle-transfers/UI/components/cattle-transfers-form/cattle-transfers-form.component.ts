import { CommonModule } from '@angular/common';
import { Component, ViewEncapsulation } from '@angular/core';
import {
    FormBuilder,
    FormGroup,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { TableModule } from 'primeng/table';
import { CreateCattleSaleItemDto } from 'src/app/modules/cattle-sales/domain/dto/create-cattle-sale-item.dto';
import { SelectCattleModalComponent } from 'src/app/modules/cattle-sales/UI/components/select-cattle-modal/select-cattle-modal.component';
import {
    CreateCattleTransferDto,
    CreateCattleTransferItemDto,
    TransferType,
} from '../../../domain/dto/create-cattle-transfer.dto';
import { lastValueFrom } from 'rxjs';
import { CattleTransferService } from '../../../infraestructure/cattle-transfers.service';
import { ListElementService } from 'src/app/core/services/list-element.service';
import { ListElementFarmsModes } from 'src/app/shared/utils/models/list-element-farms.model';
import { IndexDBService } from 'src/app/core/services/index-db.service';
import { InputTextModule } from 'primeng/inputtext';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { DividerModule } from 'primeng/divider';
import { PanelModule } from 'primeng/panel';
import { TagModule } from 'primeng/tag';
import { ChipModule } from 'primeng/chip';
import { MessagesModule } from 'primeng/messages';

@Component({
    selector: 'app-cattle-transfers-form',
    standalone: true,
    templateUrl: './cattle-transfers-form.component.html',
    styleUrls: ['./cattle-transfers-form.component.scss'],
    encapsulation: ViewEncapsulation.None,
    imports: [
        RouterModule,
        CommonModule,
        ReactiveFormsModule,
        CalendarModule,
        MessagesModule,
        DropdownModule,
        ChipModule,
        TagModule,
        InputTextareaModule,
        DividerModule,
        TableModule,
        ButtonModule,
        InputTextModule,
        PanelModule,
        SelectCattleModalComponent,
    ],
})
export class CattleTransfersFormComponent {
    loadingTable: boolean = false;
    frm!: FormGroup;
    farmsModels: ListElementFarmsModes[] = [];
    modalVisible = false;
    public slug: string | null = 'create';
    selectedSaleLikeItems: CreateCattleSaleItemDto[] = [];
    selectedItems: CreateCattleSaleItemDto[] = [];
    transferTypes = [
        { label: 'Ganado (CATTLE)', value: 'GANADO' as TransferType },
        { label: 'Nacimientos (BIRTH)', value: 'TERNERO' as TransferType },
    ];
    constructor(
        private fb: FormBuilder,
        private transferSvc: CattleTransferService,
        private message: MessageService,
        private indexDB: IndexDBService,
        private router: Router,
        private listElementService: ListElementService,
        private readonly _activatedRoute: ActivatedRoute
    ) { }
    async ngOnInit(): Promise<void> {
        this.loadForms()
        this._activatedRoute.params.subscribe(async (params) => {
            const id = params['id'];
            if (id) {
                this.slug = 'view';
                await this.loadTransfersById(+id);
                this.frm.disable();
            }
        })
        await this.prefillOriginFarm()
        this.watchOriginChange()
    }
    loadForms(): void {
        const hoy = new Date().toISOString().slice(0, 10);
        this.frm = this.fb.group({
            id: [0],
            transferType: ['GANADO', Validators.required],
            originFarmId: [null, Validators.required],
            destinationFarmId: [null, Validators.required],
            transferDate: [hoy, Validators.required],
            observations: [''],
        });
        this.frm.valueChanges.subscribe(v => {
            const same = v.originFarmId && v.destinationFarmId && v.originFarmId === v.destinationFarmId;
            if (same) this.frm.get('destinationFarmId')?.setErrors({ sameFarm: true });
            else this.frm.get('destinationFarmId')?.updateValueAndValidity({ emitEvent: false });
        });
        this.frm.valueChanges.subscribe((v) => {
            const same =
                v.originFarmId &&
                v.destinationFarmId &&
                v.originFarmId === v.destinationFarmId;
            if (same)
                this.frm.get('destinationFarmId')?.setErrors({ sameFarm: true });
            else
                this.frm
                    .get('destinationFarmId')
                    ?.updateValueAndValidity({ emitEvent: false });
        });
    }
    /** Carga farms y setea origin con el farmId de IndexedDB */
    private async prefillOriginFarm(): Promise<void> {
        try {
            const [farmId, farmsRes] = await Promise.all([
                this.indexDB.getFarmId(),
                lastValueFrom(this.listElementService.forListByFarms())
            ])
            this.farmsModels = farmsRes.data ?? [];
            if (farmId) {
                this.frm.patchValue({ originFarmId: farmId });
                this.frm.get('originFarmId')?.disable({ emitEvent: false });
            }
        } catch (err) {
            console.error('Error prefill origin farm', err);
        }
    }
    openModal(): void {
        this.modalVisible = true;
    }
    handleSelected(items: CreateCattleSaleItemDto[]): void {
        this.selectedSaleLikeItems = items;
        this.modalVisible = false;
    }

    removeRow(idx: number) {
        this.selectedSaleLikeItems.splice(idx, 1);
        this.selectedSaleLikeItems = [...this.selectedSaleLikeItems];
    }

    private buildTransferItems(): CreateCattleTransferItemDto[] {
        const type = this.frm.get('transferType')?.value as TransferType;
        return this.selectedSaleLikeItems.map((it) => {
            const isCattle = type === 'GANADO' || it.tipoOrigen === 'GANADO';
            return {
                cattleId: isCattle ? it.idOrigen : null,
                birthId: isCattle ? null : it.idOrigen,
                numero_ganado: it.numero_ganado ? String(it.numero_ganado) : null,
                peso: isCattle ? it.pesoVenta : null,
            };
        });
    }

    private buildPayload(): CreateCattleTransferDto {
        const v = this.frm.getRawValue();
        return {
            transferType: v.transferType,
            originFarmId: v.originFarmId,
            destinationFarmId: v.destinationFarmId,
            transferDate:
                typeof v.transferDate === 'string'
                    ? v.transferDate
                    : new Date(v.transferDate).toISOString().slice(0, 10),
            observations: v.observations || null,
            items: this.buildTransferItems(),
        };
    }

    async submit(): Promise<void> {
        if (this.frm.invalid || this.selectedSaleLikeItems.length === 0) return;

        const payload = this.buildPayload();
        try {
            await lastValueFrom(this.transferSvc.createTransfer(payload));
            this.message.add({
                severity: 'success',
                summary: 'Traslado creado',
                detail: 'Se registró correctamente.',
            });
            this.router.navigateByUrl('/transfers');
            this.selectedSaleLikeItems = [];
        } catch (e: any) {
            this.message.add({
                severity: 'error',
                summary: 'Error',
                detail: e?.message ?? 'No fue posible registrar el traslado',
            });
        }
    }
    getFarms(): void {
        this.listElementService.forListByFarms().subscribe((response) => {
            this.farmsModels = response.data;
        });
    }
    // Getter: siempre devuelve farms SIN la finca origen
    get farmsDestination(): Array<{ id: number; nombre: string }> {
    const originId = this.frm?.get('originFarmId')?.value;
    return (this.farmsModels ?? []).filter(f => f.id !== originId);
    }

    // Si cambian el origen, limpia destino si quedó igual
    private watchOriginChange(): void {
    const originCtrl = this.frm.get('originFarmId')
    const destCtrl = this.frm.get('destinationFarmId')
    originCtrl?.valueChanges.subscribe((originId) => {
        if (destCtrl?.value === originId) {
        destCtrl?.setValue(null, { emitEvent: false })
        }
    })
    }
    async loadTransfersById(id: number): Promise<void> {
    try {
        const { data } = await lastValueFrom(this.transferSvc.getCattleTransferById(id));

        // Cabecera del formulario
        this.frm.patchValue({
        id: data.id,
        transferType: data.transferType,
        originFarmId: data.originFarmId,
        destinationFarmId: data.destinationFarmId,
        transferDate: data.transferDate,        
        observations: data.observations ?? ''
        });
        this.selectedSaleLikeItems = this.mapTransferItemsToSaleLike(data.items || []);
    } catch (error) {
        console.error(error);
    }
    }
    private mapTransferItemsToSaleLike(items: CreateCattleTransferItemDto[]): CreateCattleSaleItemDto[] {
    return items.map((it): CreateCattleSaleItemDto => {
        const isCattle = it.cattleId != null;
        return {
        tipoOrigen: isCattle ? 'GANADO' : 'TERNERO',
        idOrigen: (isCattle ? it.cattleId : it.birthId)!,
        // valores por defecto para que compile y pinte; puedes luego enriquecer
        pesoVenta: it.peso ?? 0,
        precioKilo: 0,
        precioTotal: 0,
        numero_ganado: it.numero_ganado || '—'
        };
    });
    }
        /**
     * Calcula el peso total de los animales seleccionados
     */
    getTotalWeightFromSelected(): string {
    if (!this.selectedSaleLikeItems || this.selectedSaleLikeItems.length === 0) {
        return '0.00 kg';
    }
    
    const totalWeight = this.selectedSaleLikeItems.reduce((total, item) => {
        return total + (item.pesoVenta || 0);
    }, 0);
    
    return `${totalWeight.toFixed(2)} kg`;
    }

    /**
     * Obtiene la fecha actual formateada
     */
    getFormattedCurrentDate(): string {
    const today = new Date();
    const day = today.getDate().toString().padStart(2, '0');
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const year = today.getFullYear();
    
    return `${day}/${month}/${year}`;
    }
}

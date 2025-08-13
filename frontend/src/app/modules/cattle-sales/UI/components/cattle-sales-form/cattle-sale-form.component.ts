import { Component, ViewChild, ViewEncapsulation } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CreateCattleSaleItemDto } from '../../../domain/dto/create-cattle-sale-item.dto';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AlertService } from 'src/app/shared/utils/pipes/alert.service';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CreateCattleSaleDto } from '../../../domain/dto/create-cattle-sale.dto';
import { lastValueFrom } from 'rxjs';
import { TableModule } from 'primeng/table';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { SalesService } from '../../../infraestructure/sales.service';
import { SelectCattleModalComponent } from '../select-cattle-modal/select-cattle-modal.component';
import { CattleSalesModule } from '../../../cattle-sales.module';
import { HelpersService } from 'src/app/shared/utils/pipes/helper.service';
import { DropdownModule } from 'primeng/dropdown';
import { TerceroService } from 'src/app/core/services/terceros.service';
import { AutoCompleteModelDto, FilterTerceroDto, IAutoComplete } from 'src/app/shared/dto/autocomplete-terceros.model';
import { AutoCompleteCompleteEvent, AutoCompleteModule, AutoCompleteOnSelectEvent } from 'primeng/autocomplete';
import { CattleSalePdfService } from 'src/app/core/services/pdf.service';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { ChipModule } from 'primeng/chip';
import { DividerModule } from 'primeng/divider';
import { TagModule } from 'primeng/tag'; 


@Component({
  selector: 'app-cattle-sale-form',
  templateUrl: './cattle-sale-form.component.html',
  styleUrls: ['./cattle-sale-form.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    InputTextModule,
    CalendarModule,
    InputNumberModule,
    InputTextareaModule,
    RouterModule,
    ToastModule,
    ConfirmDialogModule,
    TableModule,
    SelectCattleModalComponent,
    CattleSalesModule,
    DropdownModule,
    AutoCompleteModule,
    PanelModule,
    ButtonModule,
    ChipModule,
    DividerModule,
    TagModule,
  ],
  encapsulation: ViewEncapsulation.None,
})
export class CattleSaleFormComponent {
  modalVisible = false;
  loadingTable: boolean = false;
  public frm!: FormGroup;
  isSubmitting = false;
  public slug: string | null = 'create';
  public selectedItems: CreateCattleSaleItemDto[] = [];
  public selectedCattleIds: number[] = [];
  formasPago = [
  { label: 'Contado', value: 'Contado' },
  { label: 'Crédito', value: 'Crédito' },
  { label: 'Transferencia', value: 'Transferencia' },
  { label: 'Efectivo', value: 'Efectivo' },
];
  tercerosSugeridos: AutoCompleteModelDto[] = [];
  constructor(
    private readonly fb: FormBuilder,
    private readonly _router: Router,
    private readonly pdfService: CattleSalePdfService,
    readonly _helperService: HelpersService,
    private terceroService: TerceroService,
    private readonly messageService: MessageService,
    private readonly _alertService: AlertService,
    private readonly cattleSaleService: SalesService,
    private readonly _activatedRoute: ActivatedRoute
  ) {}
  async ngOnInit() {
    this.loadForm();
            this._activatedRoute.params.subscribe(async (params) => {
            const id = params['id'];
            if (id) {
                this.slug = 'view';
                await this.loadSaleById(+id);
                this.frm.disable();
            }
            });
    this.frm.statusChanges.subscribe(() => {
      this.updateTableLoadingState();
    });
    this.frm.get('precioKilo')?.valueChanges.subscribe((nuevoPrecio) => {
      this.recalcularTotales(nuevoPrecio);
    });
  }

  loadForm(): void {
      const now = new Date();
      const horaActual = now.toTimeString().substring(0, 5)
      const fechaActual = now.toISOString().substring(0, 10)

    this.frm = this.fb.group({
      terceroId: [null],
      numeroFactura: ['', Validators.required],
      fechaVenta: [fechaActual, Validators.required],
      horaEmision: [horaActual, Validators.required],
      moneda: ['COP', Validators.required],
      formaPago: ['', Validators.required],

      // Datos del cliente (tercero)
      terceroIdentificacion: ['', Validators.required],
      tipoIdentificacion: [{ value: '', disabled: true }],
      nombreRazonSocial: [{ value: '', disabled: true }],
      telefono: [{ value: '', disabled: true }],
      direccion: [{ value: '', disabled: true }],
      destinatario :[null, Validators.required],
      // Totales (deshabilitados desde el inicio)
      precioKilo: [null, Validators.required],
      pesoTotal: [{ value: 0, disabled: true }],
      subtotal: [{ value: 0, disabled: true }],
      iva: [{ value: 0, disabled: true }],
      descuentos: [{ value: 0, disabled: true }],
      precioTotal: [{ value: 0, disabled: true }],

      observaciones: ['']
    });

  }
async buildData(): Promise<CreateCattleSaleDto> {
  const value = this.frm.getRawValue();

  const subtotal = value.precioTotal;
  const iva = 0;
  const descuentos = 0;
  const total = subtotal + iva - descuentos;
  const tipoVenta: 'INDIVIDUAL' | 'LOTE' = this.selectedCattleIds.length === 1 ? 'INDIVIDUAL' : 'LOTE';

  return {
    tipoVenta,
    fechaVenta: this.formatDateToYYYYMMDD(value.fechaVenta),
    horaEmision: value.horaEmision,
    numeroFactura: value.numeroFactura,
    precioKilo: value.precioKilo,
    pesoTotal: value.pesoTotal,
    subtotal,
    iva,
    descuentos,
    total,
    moneda: value.moneda,
    formaPago: value.formaPago,
    destino: value.destinatario,
    observaciones: value.observaciones,
    terceroId: value.terceroId ?? 0, 
    cattleIds: this.selectedCattleIds,
    items: this.selectedItems,
  };
}

  async submitForm(): Promise<void> {
    if (this.frm.invalid || this.selectedItems.length === 0) {
      this.markFormAsTouched();
      this._alertService.showError(
        'Alerta del sistema',
        'Complete todos los campos y seleccione al menos un animal'
      );
      this.isSubmitting = false;
      return;
    }

    const data: CreateCattleSaleDto = await this.buildData();

    try {
      const response = await lastValueFrom(
        this.cattleSaleService.createCattleSale(data)
      );

      if (response?.status === 201 || response?.status === 200) {
        this.messageService.add({
          severity: 'success',
          summary: 'Operación exitosa',
          detail: 'Venta registrada correctamente',
          life: 2000,
        });
        this._router.navigate(['/sales']);
      }
    } catch (error: any) {
      this._alertService.showError(
        'Alerta del sistema',
        error?.message ?? 'Error al registrar la venta'
      );
    }
  }
  private markFormAsTouched(): void {
    this.frm.markAllAsTouched();
  }
  private formatDateToYYYYMMDD(date: Date | string): string {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }
  // Llamado desde la modal para agregar animales
  setSelectedAnimals(items: CreateCattleSaleItemDto[], ids: number[]): void {
    this.selectedItems = items;
    this.selectedCattleIds = ids;
  }
  updateTableLoadingState(): void {
    this.loadingTable = this.frm.invalid || this.selectedItems.length === 0;
  }
  handleSelected(selected: CreateCattleSaleItemDto[]) {
    console.log('🐄 Animales seleccionados desde modal:', selected);
    const precioKilo = this.frm.get('precioKilo')?.value;

    // Calcular items
    const items = selected.map((animal) => {
      const peso = animal.pesoVenta ?? 0;
      const precioTotal = precioKilo * peso;

      return {
        ...animal,
        precioKilo,
        precioTotal,
      };
    });

    // Calcular totales
    const pesoTotal = items.reduce((sum, item) => sum + item.pesoVenta, 0);
    const precioTotal = items.reduce((sum, item) => sum + item.precioTotal, 0);
    // Asignar a tabla
    this.selectedItems = items as CreateCattleSaleItemDto[];
    this.selectedCattleIds = items.map((item) => item.idOrigen);
    this.loadingTable = false;
    this.frm.patchValue({
      pesoTotal,
      precioTotal,
    });
  }
  searchTerceros(event: AutoCompleteCompleteEvent): void {
  const payload: IAutoComplete<FilterTerceroDto> = {
    search: event.query,
    params: undefined
  };

  this.terceroService.autoCompleteTerceros(payload).subscribe({
    next: (res) => {
      this.tercerosSugeridos = res.data;
    },
    error: (err) => {
      console.error('Error en autocomplete de terceros', err);
    }
  });
}
seleccionarTercero(event: AutoCompleteOnSelectEvent): void {
  const tercero = event.value as AutoCompleteModelDto;
  if (!tercero) return;

  this.frm.patchValue({
    terceroId: tercero.id,
    terceroIdentificacion: tercero.numeroIdentificacion,
    tipoIdentificacion: tercero.tipoIdentificacion,
    nombreRazonSocial: tercero.nombreRazonSocial,
    telefono: tercero.telefono,
    direccion: tercero.direccion,
  });
}



  openModal() {
    this.modalVisible = true;
  }
  private recalcularTotales(precioKilo: number) {
    let pesoTotal = 0;
    let precioTotal = 0;

    const nuevosItems = this.selectedItems.map((item) => {
      const nuevoPrecioTotal = item.pesoVenta * precioKilo;
      pesoTotal += item.pesoVenta;
      precioTotal += nuevoPrecioTotal;

      return {
        ...item,
        precioKilo,
        precioTotal: nuevoPrecioTotal,
      };
    });

    this.selectedItems = nuevosItems;

    this.frm.patchValue({
      pesoTotal,
      precioTotal,
    });
  }
  async loadSaleById(id: number): Promise<void> {
    try {
      const response = await lastValueFrom(this.cattleSaleService.getCattleSaleById(id));
      const data = response.data;
      // Validación de integridad
    if (!data.terceroId) {
      this._alertService.showError(
        'Error de datos',
        'La venta no tiene asociado un tercero válido'
      );
      return;
    }
      // Buscar tercero por número de identificación
    const payload: IAutoComplete<FilterTerceroDto> = {
      search: data.numeroIdentificacion,
      params: undefined,
    };
      const terceroResponse = await lastValueFrom(
        this.terceroService.autoCompleteTerceros(payload)
      );

      const tercero = terceroResponse.data.find((t) => t.id === data.terceroId);

      if (tercero) {
        this.tercerosSugeridos = [tercero];

        this.frm.patchValue({
          terceroId: tercero.id,
          terceroIdentificacion: tercero.numeroIdentificacion,
          tipoIdentificacion: tercero.tipoIdentificacion,
          nombreRazonSocial: tercero.nombreRazonSocial,
          telefono: tercero.telefono,
          direccion: tercero.direccion,
        });
      }

      // Setear datos generales de la venta
      this.frm.patchValue({
        numeroFactura: data.numeroFactura,
        fechaVenta: new Date(data.fechaVenta),
        horaEmision: data.horaEmision,
        moneda: data.moneda,
        formaPago: data.formaPago,
        destinatario: data.destino,
        observaciones: data.observaciones,
        precioKilo: data.precioKilo,
        pesoTotal: data.pesoTotal,
        precioTotal: data.precioTotal,
      });

      this.selectedItems = data.items;
      this.selectedCattleIds = data.items.map((i: any) => i.idOrigen);
    } catch (err) {
      this._alertService.showError('Error al cargar la venta', (err as any).message ?? '');
    }
  }
  exportarPdf(): void {
  const value = this.frm.getRawValue();

  this.pdfService.exportarFactura(
    {
      nombre: value.nombreRazonSocial,
      direccion: value.direccion,
      telefono: value.telefono,
      fecha: this.formatDateToYYYYMMDD(value.fechaVenta),
      formaPago: value.formaPago
    },
    value.precioKilo,
    this.selectedItems,
    value.precioTotal
  );
}


}

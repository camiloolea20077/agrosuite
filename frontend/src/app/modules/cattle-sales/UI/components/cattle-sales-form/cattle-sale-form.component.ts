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
  ],
  encapsulation: ViewEncapsulation.None,
})
export class CattleSaleFormComponent {
  modalVisible = false;
  loadingTable: boolean = false;
  public frm!: FormGroup;
  public slug: string | null = 'create';
  public selectedItems: CreateCattleSaleItemDto[] = [];
  public selectedCattleIds: number[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly _router: Router,
    readonly _helperService: HelpersService,
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
    this.frm = this.fb.group({
      fechaVenta: [null, Validators.required],
      precioKilo: [null, [Validators.required, Validators.min(1)]],
      pesoTotal: [null, [Validators.required, Validators.min(1)]],
      precioTotal: [null, [Validators.required, Validators.min(1)]],
      destino: [null, Validators.required],
      comprador: [null, Validators.required],
      observaciones: [null, Validators.required],
    });
  }
  async buildData(): Promise<CreateCattleSaleDto> {
    const value = this.frm.value;
    return {
      observaciones: value.observaciones || '',
      fechaVenta: this.formatDateToYYYYMMDD(value.fechaVenta),
      pesoTotal: value.pesoTotal,
      precioKilo: value.precioKilo,
      precioTotal: value.precioTotal,
      destino: value.destino,
      comprador: value.comprador,
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
          life: 5000,
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
    const precioKilo = this.frm.get('precioKilo')?.value;

    // Calcular items
    const items = selected.map((animal) => {
      const peso = animal.pesoVenta ?? 0;
      const precioTotal = precioKilo * peso;

      return {
        tipoOrigen: animal.tipoOrigen,
        idOrigen: animal.idOrigen,
        pesoVenta: peso,
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
  async loadSaleById(id: number) {
    try {
      const response = await lastValueFrom(
        this.cattleSaleService.getCattleSaleById(id)
      );
      const data = response.data;

      // Carga el formulario
      this.frm.patchValue({
        comprador: data.comprador,
        destino: data.destino,
        fechaVenta: new Date(data.fechaVenta),
        observaciones: data.observaciones,
        precioKilo: data.precioKilo,
        pesoTotal: data.pesoTotal,
        precioTotal: data.precioTotal,
      });

      this.selectedItems = data.items;
      this.selectedCattleIds = data.items.map((item: any) => item.idOrigen);
    } catch (error) {
      this._alertService.showError(
        'Error al cargar la venta',
        (error as { message: string }).message || ''
      );
    }
  }
}

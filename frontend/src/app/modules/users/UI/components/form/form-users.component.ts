import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { CalendarModule } from 'primeng/calendar';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DropdownModule } from 'primeng/dropdown';
import { InputSwitchModule } from 'primeng/inputswitch';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ToastModule } from 'primeng/toast';
import { UsersService } from 'src/app/core/services/users.service';
import { AlertService } from 'src/app/shared/utils/pipes/alert.service';
import { CreateUsersDto } from '../../../domain/dto/create-users.dto';
import { UpdateUsersDto } from '../../../domain/dto/update-users.dto';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';
import { UsersModels } from '../../../domain/models/users.model';
import { lastValueFrom } from 'rxjs';
import { ListElementService } from 'src/app/core/services/list-element.service';
import { ListElementFarmsModes } from 'src/app/shared/utils/models/list-element-farms.model';

@Component({
  selector: 'app-form-users',
  standalone: true,
  templateUrl: './form-users.component.html',
  styleUrls: ['./form-users.component.scss'],
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
  ],
})
export class FormUsersComponent {
  public frm!: FormGroup;
  id!: number;
  slug: string | null = 'create';
  farmsModels: ListElementFarmsModes[] = [];
  rolesModels: ListElementFarmsModes[] = [];
  constructor(
    private readonly usersService: UsersService,
    private readonly _router: Router,
    private listElementService: ListElementService,
    private readonly messageService: MessageService,
    private readonly _alertService: AlertService,
    private readonly formBuilder: FormBuilder,
    public readonly _activatedRoute: ActivatedRoute
  ) {
    this.slug = this._activatedRoute.snapshot.data['slug'];
    this.id = Number(this._activatedRoute.snapshot.params['id']);
  }
  async ngOnInit() {
    this.getRoles();
    this.getFarms();
    this.loadForm();
    // if (this.slug === 'edit') {
    //   this.loadEmployeesById(this.id);
    // }
  }
    getFarms():void{
        this.listElementService.forListByFarms().subscribe((response) => {
            this.farmsModels = response.data;
        });
    }
    getRoles():void{
        this.listElementService.forListByRoles().subscribe((response) => {
          const ignoreId = 1
            this.rolesModels = response.data.filter((role) => role.id !== ignoreId);
        });
    }
  loadForm() {
    this.frm = this.formBuilder.group({
      active: [true],
      name: [null, Validators.required],
      email: [null, Validators.required],
      password: [null, Validators.required],
      role_id: [null, Validators.required],
      farmId: [null, Validators.required],
      username: [null, Validators.required],
    });
  }

  async buildDataUsers(): Promise<CreateUsersDto | UpdateUsersDto> {
    const formValue = this.frm.value;
    return {
        username: this.frm.controls['username'].value,
      active: formValue.active ? 1 : 2,
      name: this.frm.controls['name'].value,
      email: this.frm.controls['email'].value,
      password: this.frm.controls['password'].value,
      role_id: this.frm.controls['role_id'].value,
      farmId: this.frm.controls['farmId'].value,
    };
  }
  async buildSaveUsers(): Promise<void> {
        const msgSystem = 'Alerta del sistema';
        const msgText = 'Complete el formulario correctamente';
    if(this.isFormInvalid()){
      this.markFormAsTouched();
      this._alertService.showError(msgSystem, msgText);
      return;
    }
    const data: CreateUsersDto | UpdateUsersDto = await this.buildDataUsers();
    try {
      const response = await this.saveUsers(data);
      if (response) this.handleResponse(response);
    } catch (error) {
      const msg = 'Error al guardar el empleado';
      this.showErrorMessage(msg);
    }

  }
  private saveUsers(data: CreateUsersDto | UpdateUsersDto): Promise<ResponseModel<boolean | UsersModels>| void> {
      if(this.slug === 'create'){
          return lastValueFrom(
              this.usersService.createUsers(data as CreateUsersDto)
        ).catch((error) => {
          this.showErrorMessage(error.message);
        })
      } if(this.slug === 'edit'){
        return lastValueFrom(
            this.usersService.updateUsers(this.id, data as UpdateUsersDto)
        ).catch((error) => {
          this.showErrorMessage(error.message)
        })
      }
      throw new Error('Slug inválido');
  }
  private handleResponse(response: ResponseModel<UsersModels | boolean>): void {
    if (response?.status === 200 || response?.status === 201) {
      const message = this.slug === 'edit'
        ? 'Usuario actualizado correctamente'
        : 'Usuario creado correctamente';
      this.messageService.add({
        severity: 'success',
        summary: 'Operación exitosa',
        detail: message,
        life: 5000,
      });
      if (response?.status === 201) {
        this._router.navigate(['/users']);
      }
    }
  }
  markFormAsTouched(): void {
    this.frm.markAllAsTouched();
  }
  isFormInvalid(): boolean {
    return this.frm.invalid;
  }
  private showErrorMessage(message: string): void {
    const msgSystem = 'Alerta del sistema';
    this._alertService.showError(msgSystem, message ?? 'Ok');
  }
}

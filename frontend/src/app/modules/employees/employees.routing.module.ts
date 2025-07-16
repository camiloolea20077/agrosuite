import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./UI/page/index-employees.component').then(m => m.IndexEmployeesComponent)
    },
    {
        path: 'create',
        data:{
            slug: 'create',
            title: 'Nuevo Empleado',
        },
        loadComponent: () =>import('./UI/components/form-employees.component').then(
            (c) => c.FormEmployeesComponent
        )
    },
    {
        path: 'edit/:id',
        data: {
            slug: 'edit',
            title: 'Editar Empleado',
        },
        loadComponent: () =>import('./UI/components/form-employees.component').then(
            (c) => c.FormEmployeesComponent
        )
    },
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EmployeesRoutingModule { }
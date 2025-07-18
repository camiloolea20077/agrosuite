import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes : Routes = [
    {
        path: '',
        loadComponent: () =>import('./UI/page/index/index-users.component').then(m => m.IndexUsersComponent)
    },
    {
        path: 'create',
        data:{
            slug: 'create',
            title: 'Nuevo Usuario',
        },
        loadComponent: () =>import('./UI/components/form/form-users.component').then(m => m.FormUsersComponent)
    },
    {
        path: 'edit/:id',
        data: {
            slug: 'edit',
            title: 'Editar Empleado',
        },
        loadComponent: () =>import('./UI/components/form/form-users.component').then(m => m.FormUsersComponent)
    }
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }
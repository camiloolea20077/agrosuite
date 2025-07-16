import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./UI/page/index-birhs.component').then(m => m.IndexBirthsComponent)
    },
    {
        path: 'create',
        data:{
            slug: 'create',
            title: 'Nuevo ganado',
        },
        loadComponent: () => import('./UI/components/form/form-births.component').then(m => m.FormBirthsComponent)
    },
    {
        path: 'edit/:id',
        data:{
            slug: 'edit',
            title: 'Editar ganado',
        },
        loadComponent: () => import('./UI/components/form/form-births.component').then(m => m.FormBirthsComponent)
    }
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BirthsRoutingModule { }
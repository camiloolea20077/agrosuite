import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
    {
        path: '',
        loadComponent: () =>import('./UI/page/index-cattle/index-cattle.component').then(
            (c) => c.IndexCattleComponent
        )
    },
    {
        path: 'create',
        data:{
            slug: 'create',
            title: 'Nuevo ganado',
        },
        loadComponent: () =>import('./UI/components/form/form-cattle.component').then(
            (c) => c.FormCattleComponent
        )
    },{
        path: 'edit/:id',
        data:{
            slug: 'edit',
            title: 'Editar ganado',
        },
        loadComponent: () =>import('./UI/components/form/form-cattle.component').then(
            (c) => c.FormCattleComponent
        )
    }
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CattleRoutingModule {}
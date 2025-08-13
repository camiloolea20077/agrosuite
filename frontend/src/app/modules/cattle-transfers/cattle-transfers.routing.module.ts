import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./UI/pages/index-cattle-transfers/index-cattle-transfers.component').then(m => m.IndexCattleTransfersFormComponent)
    },
    {
        path: 'create',
        loadComponent: () => import('./UI/components/cattle-transfers-form/cattle-transfers-form.component').then(m => m.CattleTransfersFormComponent)
    },
    {
        path: 'view/:id',
        loadComponent: () => import('./UI/components/cattle-transfers-form/cattle-transfers-form.component').then(m => m.CattleTransfersFormComponent)
    }
]
@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class CattleTransfersRoutingModule {}
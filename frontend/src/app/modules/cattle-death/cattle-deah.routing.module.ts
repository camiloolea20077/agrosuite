import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./UI/pages/index-cattle-death/index-cattle-death.component').then(m => m.IndexCattleDeathComponent)
    }
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CattleDeathRoutingModule { }
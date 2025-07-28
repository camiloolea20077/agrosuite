import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./UI/page/index-cattle-sales/index-cattle-sales.component').then(
        (m) => m.IndexCattleSalesComponent
      ),
  },
  {
    path: 'create',
    loadComponent: () =>
      import('./UI/components/cattle-sales-form/cattle-sale-form.component').then(
        (m) => m.CattleSaleFormComponent
      ),
  },
  {
    path: 'view/:id',
    loadComponent: () =>
      import('./UI/components/cattle-sales-form/cattle-sale-form.component').then(
        (m) => m.CattleSaleFormComponent
      ),
  }
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CattleSalesRoutingModule { }

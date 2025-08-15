import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./UI/pages/index-inventory/index-inventory.component').then(m => m.IndexInventoryComponent)
    },
    {
    path: 'management',
    loadComponent: () => import('./UI/components/inventory-management/inventory-management.component')
      .then(c => c.InventoryManagementComponent),
    data: {
      title: 'Gestión de Inventario',
      breadcrumb: 'Gestión'
    }
  },
    {
    path: 'movements',
    loadComponent: () => import('./UI/components/inventory-movements/inventory-movements.component')
      .then(c => c.InventoryMovementsComponent),
    data: {
      title: 'Movimientos de Inventario',
      breadcrumb: 'Movimientos'
    }
  },
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InventoryRoutingModule { }
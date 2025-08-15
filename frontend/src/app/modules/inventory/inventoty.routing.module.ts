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
  {
    path: 'catalogs/tipos-insumos',
    loadComponent: () => import('./UI/components/tipos-insumos/tipos-insumos.component')
      .then(c => c.TiposInsumosComponent),
    data: {
      title: 'Tipos de Insumos',
      breadcrumb: 'Catálogos'
    }
  },
  {
    path: 'catalogs/estados-inventario',
    loadComponent: () => import('./UI/components/estados-inventario/estados-inventario.component')
      .then(c => c.EstadosInventarioComponent),
    data: {
      title: 'Estados de Inventario',
      breadcrumb: 'Catálogos'
    },
  },
  {
    path: 'catalogs/tipos-movimientos',
    loadComponent: () => import('./UI/components/tipos-movimientos/tipos-movimientos.component')
      .then(c => c.TiposMovimientosComponent),
    data: {
      title: 'Tipos de Movimientos',
      breadcrumb: 'Catálogos'
    }
  },
  {
    path: 'catalogs/suppliers',
    loadComponent: () => import('./UI/components/proveedores/proveedores.component')
      .then(c => c.ProveedoresComponent),
    data: {
      title: 'Estados de Movimientos',
      breadcrumb: 'Catálogos'
    }
  }
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InventoryRoutingModule { }
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './modules/auth/auth.guard';
import { DummyComponent } from './shared/components/dummy-component/dummy.component';
//Como esta
const routes: Routes = [
  {
    path: '',
    redirectTo: 'redirector',
    pathMatch: 'full',
  },
  {
    path: 'redirector',
    canActivate: [AuthGuard],
    component: DummyComponent,
  },
  {
    path: '',
    loadComponent: () =>
      import('./shared/components/layout/layout.component').then(
        (c) => c.LayoutComponent
      ),
    children: [
      {
        path: 'clients',
        loadComponent: () =>
          import('./modules/clients/clients.component').then(
            (m) => m.ClientsComponent
          ),
          canActivate: [AuthGuard],
      },
      {
        path: 'cattle',
            loadChildren: () =>
          import('./modules/cattle/cattle.module').then(
            (m) => m.CattleModule
          ),
          canActivate: [AuthGuard],
      },
      {
        path: 'births',
        loadChildren: () =>
          import('./modules/births/births.module').then(
            (m) => m.BirthsModule
          ),
          canActivate: [AuthGuard],
      },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./modules/dashboard/dashboard.component').then(
            (m) => m.DashboardComponent
          ),
          canActivate: [AuthGuard],
      },
      {
        path: 'employees',
        loadChildren: () =>
          import('./modules/employees/employees.module').then(
            (m) => m.EmployeesModule
          ),
          canActivate: [AuthGuard],
      },
      {
        path: 'users',
        loadChildren: () =>
          import('./modules/users/users.module').then(
            (m) => m.UsersModule
          ),
          canActivate: [AuthGuard],
      },
            {
        path: 'inventory',
        loadChildren: () =>
          import('./modules/inventory/inventory.module').then(
            (m) => m.InventoryModule
          ),
          canActivate: [AuthGuard],
      },
      {
        path: 'sales',
        loadChildren: () =>
          import('./modules/cattle-sales/cattle-sales.module').then(
            (m) => m.CattleSalesModule
          ),
          canActivate: [AuthGuard],
      },
      {
        path: 'transfers',
        loadChildren: () =>
          import('./modules/cattle-transfers/cattle-transfers.module').then(
            (m) => m.CattleTransfersModule
          ),
          canActivate: [AuthGuard],
      },
      {
        path: 'destete',
        loadChildren: () =>
          import('./modules/destete/destete.module').then(
            (m) => m.DesteteModule
          ),
          canActivate: [AuthGuard],
      }
    ],
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./shared/components/login/login.component').then(
        (c) => c.LoginComponent
      ),
    canActivate: [AuthGuard],
  },
  {
  path: '**',
  loadComponent: () =>
    import('./shared/components/not-found/not-found.component').then(
      (m) => m.NotFoundComponent
    ),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }

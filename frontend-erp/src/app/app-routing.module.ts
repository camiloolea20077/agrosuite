import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./shared/components/layout/layout.component').then(
        (c) => c.LayoutComponent
      ),
      children: [
        {
          path: 'modules/',
          loadComponent: () =>
            import('./modules/modules.routing.module').then(
              (c) => c.ModulesRoutingModule
            ),
        },
      ],
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./shared/components/login/login.component').then(
        (c) => c.LoginComponent
      ),
  },
  // {
  //   path: 'clients',
  //   loadComponent: () =>
  //     import('./modules/clients/clients.component').then(
  //       (c) => c.ClientsComponent
  //     ),
  // }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

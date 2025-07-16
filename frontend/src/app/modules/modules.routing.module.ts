import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
    {
        path: 'clients',
        loadComponent: () =>
            import('./clients/clients.component').then(
                (c) => c.ClientsComponent
            ),
    },
    {
        path: 'dashboard',
        loadComponent: () =>
            import('./dashboard/dashboard.component').then(
                (c) => c.DashboardComponent
            ),
    },
    {
        path: 'cattle',
        loadComponent: () =>
                        import('./cattle/UI/components/form/form-cattle.component').then(
                            (c) => c.FormCattleComponent
                        ),
            children: [
                {
                    path: 'create',
                    loadComponent: () =>
                        import('./cattle/UI/components/form/form-cattle.component').then(
                            (c) => c.FormCattleComponent
                        ),
                },
            ],
    }
]
@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class ModulesRoutingModule {}
import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
    {
        path: 'clients',
        loadComponent: () =>
            import('./clients/clients.component').then(
                (c) => c.ClientsComponent
            ),
    }
]
@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class ModulesRoutingModule {}
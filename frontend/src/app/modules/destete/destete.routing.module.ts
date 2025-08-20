import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./UI/page/index-destete-components/index-destete.component').then(m => m.IndexDesteteComponent)
    }
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DesteteRoutingModule { }
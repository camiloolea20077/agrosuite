import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { EmployeesRoutingModule } from "./employees.routing.module";

@NgModule({
    imports: [EmployeesRoutingModule],
    exports: [
        CommonModule
    ],
})
export class EmployeesModule {}
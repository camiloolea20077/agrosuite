import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { InventoryRoutingModule } from "./inventoty.routing.module";

@NgModule({
    imports: [InventoryRoutingModule],
    exports: [
        CommonModule
    ],
})
export class InventoryModule { }
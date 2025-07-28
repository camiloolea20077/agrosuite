import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { InventoryRoutingModule } from "./inventoty.routing.module";
import { CurrencyCopPipe } from "src/app/shared/utils/pipes/currency-cop.pipe";

@NgModule({
    imports: [InventoryRoutingModule],
    exports: [
        CommonModule,
    ],
})
export class InventoryModule { }
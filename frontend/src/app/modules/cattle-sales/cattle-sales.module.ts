import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { CattleSalesRoutingModule } from "./cattle-sales.routing.module";
import { CurrencyCopPipe } from "src/app/shared/utils/pipes/currency-cop.pipe";

@NgModule({
    declarations: [CurrencyCopPipe], // 👈 Agrega el pipe aquí
    imports: [CattleSalesRoutingModule],
    exports: [
        CommonModule,
        CurrencyCopPipe 
    ],
})
export class CattleSalesModule {}
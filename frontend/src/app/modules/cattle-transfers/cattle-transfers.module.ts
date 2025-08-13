import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { CattleTransfersRoutingModule } from "./cattle-transfers.routing.module";

@NgModule({
    imports: [CattleTransfersRoutingModule],
    exports: [CommonModule],
})
export class CattleTransfersModule {}
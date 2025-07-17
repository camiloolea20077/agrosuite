import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { UsersRoutingModule } from "./users.routing.module";

@NgModule({
    imports: [UsersRoutingModule],
    exports: [
        CommonModule
    ],
})
export class UsersModule { }
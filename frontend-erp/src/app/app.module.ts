import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { SidebarComponent } from "./shared/components/sidebar/sidebar.component";
import { NavbarComponent } from "./shared/components/navbar/navbar.component";
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    SidebarComponent,
    NavbarComponent
],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

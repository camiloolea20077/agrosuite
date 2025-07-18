import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { SidebarComponent } from "./shared/components/sidebar/sidebar.component";
import { NavbarComponent } from "./shared/components/navbar/navbar.component";
import { LayoutComponent } from "./shared/components/layout/layout.component";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MessageService } from 'primeng/api';
import { ToastModule } from "primeng/toast";
import { GlobalInterceptor } from './modules/auth/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    SidebarComponent,
    NavbarComponent,
    LayoutComponent,
    ToastModule
],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: GlobalInterceptor, multi: true },MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }

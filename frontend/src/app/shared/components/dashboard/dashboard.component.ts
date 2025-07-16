import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NavbarComponent } from "../navbar/navbar.component";
import { SidebarComponent } from "../sidebar/sidebar.component";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  imports: [CommonModule, NavbarComponent, SidebarComponent],
})
export class DashboardComponent {
  cards = [
    { title: 'Clientes', icon: 'pi pi-users', value: 124 },
    { title: 'Ventas', icon: 'pi pi-shopping-cart', value: 87 },
    { title: 'Inventario', icon: 'pi pi-box', value: 453 },
    { title: 'Ingresos', icon: 'pi pi-dollar', value: '$12,300' },
  ];
}
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { MenuItem } from '../../dto/menu-interface';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [RouterLink, CommonModule, ToastModule],
})
export class SidebarComponent {
  isVisible = true;
  showAdminChildren = false; // controla el desplegable

  items: MenuItem[] = [
    { label: 'Dashboard', icon: 'pi pi-home', route: '/dashboard' },
    { label: 'Ganado', icon: 'pi pi-users', route: '/cattle' },
    { label: 'Inventario', icon: 'pi pi-database', route: '/inventario' },
    { label: 'Nacimientos', icon: 'pi pi-heart-fill', route: '/births' },
    { label: 'Empleados', icon: 'pi pi-users', route: '/employees' },
    { label: 'Contabilidad', icon: 'pi pi-chart-line', route: '/contabilidad' },
    { label: 'Nómina', icon: 'pi pi-wallet', route: '/nomina' },
    {
      label: 'Administración',
      icon: 'pi pi-building',
      children: [
        { label: 'Usuarios', icon: 'pi pi-users', route: '/users' },
        { label: 'Roles', icon: 'pi pi-id-card', route: '/admin/roles' },
        { label: 'Fincas', icon: 'pi pi-sitemap', route: '/admin/farms' },
      ],
    },
  ];
  toggleSidebar() {
    this.isVisible = !this.isVisible;
  }
  toggleAdminChildren() {
    this.showAdminChildren = !this.showAdminChildren;
  }
}

import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterModule } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { MenuItem } from '../../dto/menu-interface';
import { PermissionService } from 'src/app/core/services/permission.service';
import { AppPermissions } from '../../dto/permissions.enum';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [RouterLink, CommonModule, ToastModule,RouterModule],
})
export class SidebarComponent implements OnInit{
  isVisible = true;
  showAdminChildren = false;
  items: MenuItem[] = [];

  constructor(
    private authService: AuthService,
    private permissionService: PermissionService
  ) {}

  ngOnInit(): void {
    this.authService.getAuthResponse().subscribe((auth) => {
      const permisos = auth?.user?.permisos ?? [];
      this.permissionService.setPermissions(permisos);
      // Construye el menú base
      this.items = [
        { label: 'Dashboard', icon: 'pi pi-home', route: '/dashboard' },
        { label: 'Ganado', icon: 'pi pi-users', route: '/cattle' },
        { label: 'Inventario', icon: 'pi pi-database', route: '/inventory' },
        { label: 'Nacimientos', icon: 'pi pi-heart-fill', route: '/births' },
        { label: 'Empleados', icon: 'pi pi-users', route: '/employees' },
        { label: 'Contabilidad', icon: 'pi pi-chart-line', route: '/contabilidad' },
        { label: 'Nómina', icon: 'pi pi-wallet', route: '/nomina' },
      ];

      // Solo si tiene permisos reales se muestra Administración
      if (
        this.permissionService.hasPermission(AppPermissions.ADMIN_ACCESS) ||
        this.permissionService.hasPermission(AppPermissions.SECRETARY_ACCESS)
      ) {
        this.items.push({
          label: 'Administración',
          icon: 'pi pi-building',
          children: [
            { label: 'Usuarios', icon: 'pi pi-users', route: '/users' },
            { label: 'Roles', icon: 'pi pi-id-card', route: '/admin/roles' },
            { label: 'Fincas', icon: 'pi pi-sitemap', route: '/admin/farms' },
          ],
        });
      }
    });
  }

  toggleSidebar() {
    this.isVisible = !this.isVisible;
  }

  toggleAdminChildren() {
    this.showAdminChildren = !this.showAdminChildren;
  }
}

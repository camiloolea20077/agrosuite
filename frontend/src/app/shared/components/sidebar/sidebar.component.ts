import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { RouterLink, RouterModule } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { TooltipModule } from 'primeng/tooltip';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { MenuItem } from '../../dto/menu-interface';
import { PermissionService } from 'src/app/core/services/permission.service';
import { AppPermissions } from '../../dto/permissions.enum';
import { AuthService } from 'src/app/core/services/auth.service';
import { SidebarService } from '../../services/sidebar.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [
    RouterLink, 
    CommonModule, 
    ToastModule, 
    RouterModule, 
    ButtonModule, 
    RippleModule,
    TooltipModule
  ],
  animations: [
    trigger('slideDown', [
      transition(':enter', [
        style({ height: '0', opacity: 0 }),
        animate('200ms ease-in-out', style({ height: '*', opacity: 1 }))
      ]),
      transition(':leave', [
        animate('200ms ease-in-out', style({ height: '0', opacity: 0 }))
      ])
    ])
  ]
})
export class SidebarComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  
  items: MenuItem[] = [];
  expandedItems: Record<string, boolean> = {};
  isMobile = false;

  constructor(
    private authService: AuthService,
    private permissionService: PermissionService,
    public sidebarService: SidebarService
  ) {
    this.checkScreenSize();
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.checkScreenSize();
  }

  ngOnInit(): void {
    this.loadMenuItems();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private checkScreenSize(): void {
    this.isMobile = window.innerWidth <= 768;
    if (this.isMobile && this.sidebarService.isVisible) {
      this.sidebarService.closeSidebar();
    }
  }

  private loadMenuItems(): void {
    this.authService.getAuthResponse()
      .pipe(takeUntil(this.destroy$))
      .subscribe((auth) => {
        const permisos = auth?.user?.permisos ?? [];
        this.permissionService.setPermissions(permisos);
        this.buildMenu();
      });
  }

  private buildMenu(): void {
    // Menú base
    this.items = [
      { 
        label: 'Dashboard', 
        icon: 'pi pi-home', 
        route: '/dashboard',
        exact: true 
      },
      { 
        label: 'Ganado', 
        icon: 'pi pi-users', 
        route: '/cattle' 
      },
      { 
        label: 'Inventario', 
        icon: 'pi pi-database', 
        route: '/inventory' 
      },
      { 
        label: 'Nacimientos', 
        icon: 'pi pi-heart-fill', 
        route: '/births' 
      },
      { 
        label: 'Empleados', 
        icon: 'pi pi-users', 
        route: '/employees' 
      },
      { 
        label: 'Contabilidad', 
        icon: 'pi pi-chart-line', 
        route: '/contabilidad' 
      },
      { 
        label: 'Nómina', 
        icon: 'pi pi-wallet', 
        route: '/nomina' 
      },
    ];

    // Menú de operaciones (con permisos)
    if (this.hasOperationPermissions()) {
      this.items.push({
        label: 'Operaciones de Ganado',
        icon: 'pi pi-briefcase',
        children: [
          {
            label: 'Traslado de Ganado',
            icon: 'pi pi-directions',
            route: '/transfers',
          },
          {
            label: 'Venta de Ganado',
            icon: 'pi pi-shopping-cart',
            route: '/sales',
          },
        ],
      });
    }

    // Menú de administración (con permisos)
    if (this.hasAdminPermissions()) {
      this.items.push({
        label: 'Administración',
        icon: 'pi pi-cog',
        children: [
          { 
            label: 'Usuarios', 
            icon: 'pi pi-users', 
            route: '/users' 
          },
          { 
            label: 'Roles', 
            icon: 'pi pi-id-card', 
            route: '/admin/roles' 
          },
          { 
            label: 'Fincas', 
            icon: 'pi pi-sitemap', 
            route: '/admin/farms' 
          },
        ],
      });
    }
  }

  private hasOperationPermissions(): boolean {
    return this.permissionService.hasPermission(AppPermissions.ADMIN_ACCESS) ||
           this.permissionService.hasPermission(AppPermissions.SECRETARY_ACCESS);
  }

  private hasAdminPermissions(): boolean {
    return this.permissionService.hasPermission(AppPermissions.ADMIN_ACCESS) ||
           this.permissionService.hasPermission(AppPermissions.SECRETARY_ACCESS);
  }

  toggleSidebar(): void {
    this.sidebarService.toggleSidebar();
  }

  closeSidebar(): void {
    if (this.isMobile) {
      this.sidebarService.closeSidebar();
    }
  }

  toggleExpanded(): void {
    this.sidebarService.toggleCollapsed();
    if (this.sidebarService.isCollapsed) {
      // Colapsar todos los submenús cuando se colapsa el sidebar
      this.expandedItems = {};
    }
  }

  toggleItem(label: string): void {
    if (this.sidebarService.isCollapsed) {
      this.sidebarService.toggleCollapsed();
    }
    this.expandedItems[label] = !this.expandedItems[label];
  }

  isItemExpanded(label: string): boolean {
    return !!this.expandedItems[label];
  }

  // Funciones para optimización de rendimiento
  trackByFn(index: number, item: MenuItem): string {
    return item.label;
  }

  trackByChildFn(index: number, child: MenuItem): string {
    return child.label;
  }
}
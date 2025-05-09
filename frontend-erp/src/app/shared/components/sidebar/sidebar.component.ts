import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

interface MenuItem {
    label: string;
    icon: string;
    route: string;
}

@Component({
    selector: 'app-sidebar',
    standalone: true,
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss'],
    imports: [RouterLink,CommonModule],
})
export class SidebarComponent {
    items: MenuItem[] = [
        { label: 'Dashboard', icon: 'pi pi-home', route: '/dashboard' },
        { label: 'Clientes', icon: 'pi pi-users', route: '/modules/clients' },
        { label: 'Productos', icon: 'pi pi-box', route: '/productos' },
        { label: 'Ventas', icon: 'pi pi-shopping-cart', route: '/ventas' },
        { label: 'Compras', icon: 'pi pi-briefcase', route: '/compras' },
        { label: 'Inventario', icon: 'pi pi-database', route: '/inventario' },
        { label: 'Contabilidad', icon: 'pi pi-chart-line', route: '/contabilidad' },
        { label: 'Nómina', icon: 'pi pi-wallet', route: '/nomina' },
    ];
}

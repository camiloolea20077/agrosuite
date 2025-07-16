import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ToastModule } from "primeng/toast";

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
    imports: [RouterLink, CommonModule, ToastModule],
})
export class SidebarComponent {
    items: MenuItem[] = [
        { label: 'Dashboard', icon: 'pi pi-home', route: '/dashboard' },
        { label: 'Ganado', icon: 'pi pi-users', route: '/cattle' },
        { label: 'Inventario', icon: 'pi pi-database', route: '/inventario' },
        { label: 'Nacimientos', icon: 'pi-heart-fill', route: '/births' },
        { label: 'Empleados', icon: 'pi pi-users', route: '/employees' },
        { label: 'Inventario', icon: 'pi pi-database', route: '/inventario' },
        { label: 'Contabilidad', icon: 'pi pi-chart-line', route: '/contabilidad' },
        { label: 'Nómina', icon: 'pi pi-wallet', route: '/nomina' },
    ];
}

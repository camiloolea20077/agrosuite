import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import * as localForage from 'localforage';
import { AuthResponse } from 'src/app/core/models/auth.model';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  imports: [CommonModule, ButtonModule],
})
export class NavbarComponent implements OnInit{
  userName: string = '';
  roleName : string = '';
  farmsNames : string = '';
  constructor(private router: Router) {}

  ngOnInit(): void {
    localForage.getItem('authResponse').then((authResponse) => {
      if (authResponse) {
        this.userName = (authResponse as AuthResponse).user.name;
        this.roleName = (authResponse as AuthResponse).user.role;
        this.farmsNames = (authResponse as AuthResponse).user.farm_name;
      }
    }).catch((err) => {
      console.error('Error retrieving authResponse from IndexedDB', err);
    });
  }

  logout() {
    // Limpiar los datos de IndexedDB
    localForage.removeItem('authResponse').then(() => {
      console.log('User logged out, authResponse removed from IndexedDB');
      // Redirigir al login
      this.router.navigate(['/login']);
    }).catch((err) => {
      console.error('Error during logout', err);
    });
  }
}

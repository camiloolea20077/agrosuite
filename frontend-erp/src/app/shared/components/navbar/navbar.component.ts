import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  imports: [CommonModule, ButtonModule],
})
export class NavbarComponent implements OnInit{
  userName: string = '';

  constructor(private router: Router) {}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      this.userName = JSON.parse(user).name;
    }
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}

import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}


canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
  return this.authService.getAuthResponse().toPromise().then((authResponse) => {
    const isLoginRoute = state.url === '/login';
    
    if (authResponse) {
      const token = authResponse.token;
      const isExpired = this.authService.isTokenExpired(token);
      
      if (isExpired) {
        console.log('Token expired, redirecting to login');
        this.router.navigate(['/login']);
        return false;
      }
      
      // Si el token es válido y está intentando acceder a login, redirigir a dashboard
      if (isLoginRoute) {
        this.router.navigate(['/dashboard']);
        return false;
      }
      
      // Permite el acceso a otras rutas protegidas
      return true;
    } else {
      // Si no hay authResponse y no está en login, redirigir a login
      if (!isLoginRoute) {
        this.router.navigate(['/login']);
        return false;
      }
      
      // Permite el acceso a login si no está autenticado
      return true;
    }
  });
}
}

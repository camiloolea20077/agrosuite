import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, from, throwError } from 'rxjs';
import { switchMap, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AlertService } from 'src/app/shared/utils/pipes/alert.service';
import { IndexDBService } from 'src/app/core/services/index-db.service';
import { AuthResponse } from 'src/app/core/models/auth.model';

@Injectable()
export class GlobalInterceptor implements HttpInterceptor {
  constructor(
    private readonly indexDBService: IndexDBService,
    private readonly router: Router,
    private readonly alertService: AlertService
  ) {}

  intercept<T>(request: HttpRequest<T>, next: HttpHandler): Observable<HttpEvent<T>> {
    return from(this.indexDBService.loadDataAuthDB()).pipe(
      switchMap((auth: AuthResponse | null) => {
        let headers = request.headers;

        if (auth?.token) {
          headers = headers
            .set('Authorization', `Bearer ${auth.token}`)
            .set('farmId', String(auth.user.farm))
            .set('user', String(auth.user.id))
            .set('X-localization', 'es')
            .set('Accept', '*/*');

          // Si algún día agregas company al modelo, descomenta:
          // headers = headers.set('company', String(auth.user.company ?? ''));
        }

        // Manejo para solicitudes con FormData (archivos)
        if (request.body instanceof FormData) {
          request = request.clone({
            headers: headers.set('enctype', 'multipart/form-data'),
          });
        } else {
          request = request.clone({ headers });
        }

        return next.handle(request).pipe(
          catchError((error: HttpErrorResponse) => {
            if (error.status === 401) {
              this.alertService.showError(
                'Mensaje del sistema',
                error.error?.message || 'Sesión no autorizada. Por favor, vuelva a iniciar sesión.'
              );
              this.indexDBService.deleteDataAuthDB();
              this.router.navigate(['/login']);
            }
            return throwError(() => error.error || error);
          })
        );
      })
    );
  }
}

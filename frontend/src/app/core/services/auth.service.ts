import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthResponse, LoginDto } from '../models/auth.model';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';
import * as localForage from 'localforage';
import { Router } from '@angular/router';
import { environment } from 'src/app/environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private readonly apiUrl = environment.apiUrl;

    constructor(private http: HttpClient, private router: Router) {
        localForage.config({
            driver: localForage.INDEXEDDB, 
            name: 'config', 
            version: 1.0,
            storeName: 'authData',
            description: 'Token storage and user data', 
        });
    }

    // Método para iniciar sesión
    login(credentials: LoginDto): Observable<ResponseModel<AuthResponse>> {
        return this.http.post<ResponseModel<AuthResponse>>(
            `${this.apiUrl}security/login`,
            credentials
        ).pipe(
            tap((response) => {
                localForage.setItem('authResponse', response.data).then(() => {
                    this.router.navigate(['/dashboard']);
                }).catch((err) => {
                    console.error('Error saving token to IndexedDB', err);
                });
            })
        )
    }

    // Método para cerrar sesión
    logout(): void {
        localForage.removeItem('authResponse').then(() => {
        });
    }

    // Método para verificar si el usuario está autenticado
    isAuthenticated(): Observable<boolean> {
        return new Observable<boolean>((observer) => {
            localForage.getItem('authResponse').then((authResponse) => {
                observer.next(!!authResponse)
                observer.complete();
            }).catch(() => {
                observer.next(false);
                observer.complete();
            });
        });
    }

    // Método para obtener el token
    getToken(): Observable<string | null> {
        return new Observable<string | null>((observer) => {
            localForage.getItem('token').then((token) => {
                observer.next(token as string | null)
                observer.complete();
            }).catch(() => {
                observer.next(null);
                observer.complete();
            });
        });
    }
    // Método para obtener el AuthResponse completo
    getAuthResponse(): Observable<AuthResponse | null> {
        return new Observable<AuthResponse | null>((observer) => {
            localForage.getItem('authResponse').then((authResponse) => {
                observer.next(authResponse as AuthResponse )
                observer.complete();
            }).catch(() => {
                observer.next(null);
                observer.complete();
            });
        });
    }
    // Verificar si el token ha expirado
    isTokenExpired(token: string): boolean {
        const payload = this.decodeToken(token);
        const expiry = payload.exp
        const now = Math.floor(Date.now() / 1000)

        return expiry < now
    }

    // Método para decodificar el token JWT
    private decodeToken(token: string): any {
        const payload = token.split('.')[1]
        return JSON.parse(atob(payload))
    }
}

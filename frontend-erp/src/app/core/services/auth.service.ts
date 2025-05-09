import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthResponse, LoginDto } from '../models/auth.model';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private readonly apiUrl = 'http://localhost:9001/';

    constructor(private http: HttpClient) { }

    login(credentials: LoginDto): Observable<ResponseModel<AuthResponse>> {
        return this.http.post<ResponseModel<AuthResponse>>(
            `${this.apiUrl}security/login`,
            credentials
        );
    }
    logout(): void {
        localStorage.removeItem('token');
    }

    isAuthenticated(): boolean {
        return !!localStorage.getItem('token');
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }
}

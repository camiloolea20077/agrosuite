import { Injectable } from "@angular/core";
import { AuthService } from "../services/auth.service";
import { AuthResponse, LoginDto } from "../models/auth.model";
import { Observable } from "rxjs";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";

@Injectable({
    providedIn: 'root',
})
export class AuthRepositoryImpl{

    constructor(private readonly authService: AuthService){}


    login(credentials: LoginDto): Observable<ResponseModel<AuthResponse>> {
        return this.authService.login(credentials);
    }

}
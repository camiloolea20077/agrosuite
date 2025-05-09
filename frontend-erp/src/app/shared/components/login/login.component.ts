import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';

@Component({
    selector: 'app-login',
    standalone: true,
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
    imports: [
        FormsModule,
        ButtonModule,
        CheckboxModule,
        ReactiveFormsModule,
        CommonModule],
    providers: [MessageService]
})
export class LoginComponent implements OnInit {
    loginForm!: FormGroup;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private messageService: MessageService
    ) { }

    ngOnInit(): void {
        this.loginForm = this.fb.group({
            email: ['', [Validators.required, Validators.email]],
            password: ['', Validators.required]
        });
    }

    onSubmit(): void {
        if (this.loginForm.invalid) {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Datos inválidos' })
            return;
        }

        const { email, password } = this.loginForm.value

        this.authService.login({ email, password }).subscribe({
            next: (res) => {
                localStorage.setItem('user', JSON.stringify(res.data.user));
                localStorage.setItem('token', res.data.token)
                this.router.navigate(['/'])
            },
            error: (err) => {
                this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Credenciales incorrectas' })
            }
        });
    }
}

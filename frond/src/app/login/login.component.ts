
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Login } from '../interface/login';
import { Token } from '../interface/token';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from '../authentication/authentication.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  errorMessage: string = '';

    loginForm = this.fb.group({
        email: [''],
        password: ['']
      });
    
      constructor(private fb: FormBuilder, 
        private httpClient: HttpClient,
        private authService: AuthenticationService,
        private router: Router) {}
        save() {
          const login: Login = {
            email: this.loginForm.get('email')?.value ?? '',
            password: this.loginForm.get('password')?.value ?? '',
          };
        
          this.httpClient.post<Token>('http://localhost:8080/users/login', login).subscribe({
            next: (response) => {
              console.log(response.token);
              this.authService.saveToken(response.token);
              this.router.navigate(['/productos']);
            },
            error: (err) => {
              if (err.status === 403) {
                this.errorMessage = 'El usuario o la contraseña son incorrectos';
              }
            }
          });
        }
}

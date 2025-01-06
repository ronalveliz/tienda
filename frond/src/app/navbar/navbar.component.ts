import { Component, inject, Inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CarritoService } from '../services/carrito.service';
import { CommonModule } from '@angular/common';
import { AuthenticationService } from '../authentication/authentication.service';
import { User } from '../interface/user';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  public carritoService = inject(CarritoService);

  title = 'frontend'
  userId: string | null = null;
  isLoggedin = false;
  userEmail = '';
  isAdmin = false;
  isTienda = false;
  user: User | undefined;
  avatarUrl = '';

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private httpClient: HttpClient,
    ){

    this.authService.isLoggedin.subscribe(isLoggedin => {
      this.isLoggedin = isLoggedin;
      if(this.isLoggedin) {
        this.httpClient.get<User>('http://localhost:8080/users/account')
          .subscribe(user => {
            this.user = user;
            if (this.user.imgUser.startsWith('http')) {
              this.avatarUrl = user.imgUser;
            } else {
              this.avatarUrl = 'http://localhost:8080/files/' + user.imgUser;
            }
          });
      }
    } );
    this.authService.userEmail.subscribe(userEmail => this.userEmail = userEmail);
    this.authService.isAdmin.subscribe(isAdmin => this.isAdmin = isAdmin);
    this.authService.isTienda.subscribe(isTienda => this.isTienda = isTienda);
    this.authService.userId.subscribe(userId => this.userId = userId);
    this.authService.avatarUrl.subscribe(avatarUrl => {
      if (avatarUrl.startsWith('http')) {
        this.avatarUrl = avatarUrl;
      } else {
        this.avatarUrl = 'http://localhost:8080/files/' + avatarUrl;
      }
    
    });
  }

  getUserAvatar() {
    if(this.user) {
      return this.user.imgUser;
    } else {
      return '';
    }
  }

  ngOnInit(): void {

    
    
  }

  logout() {

    this.authService.removeToken();
    this.router.navigate(['/home']);
  }
  

}

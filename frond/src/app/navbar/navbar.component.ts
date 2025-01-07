import { Component, inject} from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CarritoService } from '../services/carrito.service';
import { CommonModule } from '@angular/common';
import { AuthenticationService } from '../authentication/authentication.service';


@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  public carritoService = inject(CarritoService);

  title = 'frontend';
  isLoggedIn = false;
  userEmail = '';
  isAdmin = false;

  constructor(
    private authService: AuthenticationService,
    private router: Router
    ) {
    this.authService.isLoggedIn.subscribe(isLoggedIn => this.isLoggedIn = isLoggedIn);
    this.authService.userEmail.subscribe(userEmail => this.userEmail = userEmail);
    this.authService.isAdmin.subscribe(isAdmin => this.isAdmin = isAdmin);
  }

  logout() {
    this.authService.removeToken();
    this.router.navigate(['/login']);
  }

}

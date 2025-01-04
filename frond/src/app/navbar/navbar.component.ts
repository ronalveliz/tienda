import { Component, inject, Inject } from '@angular/core';
import { RouterLink } from '@angular/router';
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

  isLoggedIn = false;
  constructor(private authService: AuthenticationService) {
    this.authService.isloggedIn.subscribe(isLoggedIn => this.isLoggedIn = isLoggedIn);
  }


  public carritoService = inject(CarritoService);

}

import { Component, inject, Inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CarritoService } from '../services/carrito.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  public carritoService = inject(CarritoService);
}

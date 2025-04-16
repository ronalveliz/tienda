import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { Producto } from '../interface/productos';
import { CarritoService } from '../services/carrito.service';
import { User } from '../interface/user';
import { AuthenticationService } from '../authentication/authentication.service';
import { TiendaFormComponent } from '../tienda-form/tienda-form.component';
import { Store } from '../interface/store';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, NgbAlertModule, HttpClientModule],

  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{

  productos: Producto[] = [];

  isLoggedin = false;
  user: User | undefined;
  authService: AuthenticationService | undefined;

  private carritoService = inject(CarritoService);

  constructor(private httpClient: HttpClient,
              authService: AuthenticationService,
              router: Router) {
                this.authService = authService;
                if (this.authService) {
                  this.authService.isLoggedin.subscribe(isLoggedin => this.isLoggedin = isLoggedin);
                }
              }

  ngOnInit(): void {
    this.loadTineda();
  }

  loadTineda(){
    const url = 'http://localhost:8080/productos';
    this.httpClient.get<Producto[]>(url)
    .subscribe(productos => {
      this.productos = productos;
    });

  }

  agregarProducto(item :Producto){
    this.carritoService.agregar(item);
  }

}

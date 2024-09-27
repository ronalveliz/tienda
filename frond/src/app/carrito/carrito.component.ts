import { Component, inject, OnInit } from '@angular/core';
import { CarritoService } from '../services/carrito.service';
import { Carrito } from '../model/carrito';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [],
  templateUrl: './carrito.component.html',
  styleUrl: './carrito.component.css'
})
export class CarritoComponent  implements OnInit{

  private carritoService = inject(CarritoService);

  listaCarrito: Carrito [] = [];


  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }
  
  getListaCarrito(){
    this.listaCarrito = this.carritoService.getCarrito();
  }

}

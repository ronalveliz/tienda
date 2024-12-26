import { Component, inject, OnInit } from '@angular/core';
import { CarritoService } from '../services/carrito.service';
import { Carrito } from '../interface/carrito';
import { RouterLink } from '@angular/router';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './carrito.component.html',
  styleUrl: './carrito.component.css'
})
export class CarritoComponent  implements OnInit{

  public carritoService = inject(CarritoService);

  listaCarrito: Carrito [] = [];


  ngOnInit(): void {
   this.getListaCarrito();
  }
  
  getListaCarrito(){
    this.listaCarrito = this.carritoService.getCarrito();
  }

  eliminarItem(index : number){
    this.carritoService.eliminar(index);
    this.getListaCarrito();
  }
  onkeyDown(event : any){
    event.preventDefault();
  }

}

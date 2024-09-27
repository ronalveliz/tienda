import { Injectable } from '@angular/core';
import { Carrito } from '../model/carrito';
import { Producto } from '../model/productos.model';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {

  private listCarrito : Carrito [] = [];

  getCarrito(){
    return this.listCarrito;
  }

  agregar(producto : Producto, cantidad : number = 1){

    const index = this.listCarrito.findIndex(item => item.producto.id == producto.id)

    if(index == -1){
      const item = new Carrito(producto , cantidad);
      this.listCarrito.push(item);
    }else{
      this.actualizar(index , this.listCarrito[index].cantidad + cantidad)
    }
  }
  actualizar(index: number, cantidad: number){
    if(index >= 0 && index <= this.listCarrito.length){
      this.listCarrito[index].cantidad = cantidad;
    }
  }

  constructor() { }
}

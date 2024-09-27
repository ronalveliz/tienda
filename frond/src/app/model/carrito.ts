import { Producto } from "./productos.model";

export class Carrito {
    producto: Producto;
    cantidad: number;

    constructor (producto : Producto , cantidad = 1 ){
        this.producto = producto;
        this.cantidad = cantidad;
    }
}
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Producto } from '../interface/productos';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private apiUrl = 'http://localhost:8080/productos';

  constructor(private http: HttpClient) { }

  getProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl);
  }

  getProductosDisponibles(): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrl}/disponible`);
  }

  getProductosPublicados(publicado: boolean): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrl}/published`, {
      params: { published: publicado.toString() }
    });
  }

  getProductosPorRangoPrecio(min: number, max: number): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrl}/price-range`, {
      params: { minPrice: min.toString(), maxPrice: max.toString() }
    });
  }

  buscarProductos(nombre: string): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrl}/search`, {
      params: { name: nombre }
    });
  }

}

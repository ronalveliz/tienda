import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { Producto } from '../model/productos.model';
import { CarritoService } from '../services/carrito.service';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, NgbAlertModule,HttpClientModule],
  
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{
  productos: Producto[] = [];
  filteredProducts: Producto[] = [];
  searchTerm: string = '';
  selectedCategory: string = '';
  categoria: string[] = []; // Añade tus categorías aquí o recupera desde el backend
  private carritoService = inject(CarritoService);

  constructor(private httpClient: HttpClient) {}

  ngOnInit(): void {
    const url = 'http://localhost:8080/productos';

    this.httpClient.get<Producto[]>(url).subscribe(productos => {
      this.productos = productos;
      this.filteredProducts = productos;
      this.loadCategories();
    });
  }


  filterProducts(): void {
    if (this.selectedCategory) {
      // Llama al backend para obtener productos de una categoría específica
      const url = `http://localhost:8080/productos/categoria/${this.selectedCategory}`;
      this.httpClient.get<Producto[]>(url).subscribe(filtered => {
        this.filteredProducts = filtered.filter(producto =>
          producto.name.toLowerCase().includes(this.searchTerm.toLowerCase())
        );
      });
    } else {
      // Si no hay categoría seleccionada, muestra todos los productos
      this.filteredProducts = this.productos.filter(producto =>
        producto.name.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
  }

  loadCategories(): void {
    // Aquí puedes cargar las categorías desde el backend o definirlas manualmente
    this.categoria = [...new Set(this.productos.map(p => p.categoria))];
  }
  
  agregarProducto(item :Producto){
    this.carritoService.agregar(item);
  }

}
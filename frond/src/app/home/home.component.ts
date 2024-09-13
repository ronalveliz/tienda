import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { Producto } from '../model/productos.model';


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
  categories: string[] = []; // Añade tus categorías aquí o recupera desde el backend

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
    this.filteredProducts = this.productos.filter(producto => {
      const matchesSearchTerm = producto.name.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchesCategory = this.selectedCategory ? producto.category === this.selectedCategory : true;
      return matchesSearchTerm && matchesCategory;
    });
  }

  loadCategories(): void {
    // Aquí puedes cargar las categorías desde el backend o definirlas manualmente
    this.categories = [...new Set(this.productos.map(p => p.category))];
  }
}
import { Component, inject, OnInit } from '@angular/core';
import { Producto } from '../interface/productos';
import { Router, RouterLink } from '@angular/router';
import { CarritoService } from '../services/carrito.service';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../services/producto.service';

@Component({
  selector: 'app-home-usuarios',
  standalone: true,
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './home-usuarios.component.html',
  styleUrl: './home-usuarios.component.css'
})
export class HomeUsuariosComponent implements OnInit {
  productos: Producto[] = [];
  productosFiltrados: Producto[] = [];
  searchTerm: string = '';
  filtroActivo: string = 'todos';
  precioMin?: number;
  precioMax?: number;
  isLoading = true;

  constructor(
    private productoService: ProductoService,
    private router: Router,
    private carritoService: CarritoService
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.isLoading = true;
    this.productoService.getProductos().subscribe({
      next: (data) => {
        this.productos = data;
        this.productosFiltrados = [...data];
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar productos', err);
        this.isLoading = false;
      }
    });
  }

  buscarProductos(): void {
    if (this.searchTerm.trim()) {
      this.productosFiltrados = this.productos.filter(producto =>
        producto.nombre.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        producto.description.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    } else {
      this.resetearFiltros();
    }
  }

  filtrarPor(tipo: string): void {
    this.filtroActivo = tipo;

    switch(tipo) {
      case 'disponibles':
        this.productoService.getProductosDisponibles().subscribe(
          productos => this.productosFiltrados = productos
        );
        break;

      case 'ofertas':
        this.productosFiltrados = this.productos.filter(p => p.descuento);
        break;

      case 'publicados':
        this.productoService.getProductosPublicados(true).subscribe(
          productos => this.productosFiltrados = productos
        );
        break;

      default: // 'todos'
        this.resetearFiltros();
    }
  }

  filtrarPorPrecio(): void {
    if (this.precioMin !== undefined && this.precioMax !== undefined) {
      this.productoService.getProductosPorRangoPrecio(this.precioMin, this.precioMax).subscribe(
        productos => this.productosFiltrados = productos
      );
    }
  }

  resetearFiltros(): void {
    this.filtroActivo = 'todos';
    this.searchTerm = '';
    this.precioMin = undefined;
    this.precioMax = undefined;
    this.productosFiltrados = [...this.productos];
  }

  agregarProducto(producto: Producto): void {
    this.carritoService.agregar(producto);
  }

  verDetalle(id: number): void {
    this.router.navigate(['/productos', id]);
  }

  getImageUrl(photoUrl: string): string {
    if (photoUrl.startsWith('http')) {
      return photoUrl;
    }
    return photoUrl ? `http://localhost:8080/files/${photoUrl}` : 'assets/default-product.png';
  }
}

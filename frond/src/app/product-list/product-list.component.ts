import { Component, TemplateRef } from '@angular/core';
import { Producto } from '../interface/productos';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from '../authentication/authentication.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { RouterLink } from '@angular/router';
import { NgbAlert } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [RouterLink,NgbAlert],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent {

  productos: Producto [] = [];
  showDeletedBookMessage: boolean = false;
  isAdmin = false;
  showSpinner = true;



  constructor(
    private httpClient: HttpClient,
    private authService: AuthenticationService,
    private modalService: NgbModal
  ) {
      this.authService.isAdmin.subscribe(isAdmin => this.isAdmin = isAdmin);
    }

  ngOnInit(): void {
    this.loadProduct();
  }
  delete(productos: Producto) {
    const url = 'http://localhost:8080/productos/' + productos.id;
    this.httpClient.delete(url).subscribe(response => {
      this.loadProduct();
      this.showDeletedBookMessage = true;
    }); // recarga los libros despues de borrar
  }
  hideDeletedBookMessage() {
    this.showDeletedBookMessage = false;
  }
  loadProduct() {
    const url = 'http://localhost:8080/productos';
    this.httpClient.get<Producto[]>(url).subscribe(productos => {
      this.productos = productos;
    }
  );
  }
  openModal(modal: TemplateRef<any>, productos: Producto) {
    this.modalService.open(modal, {
      centered: true
    }).result.then(result => {
      if (result === 'Aceptar') {
        this.delete(productos);
      }
    });
  }
}

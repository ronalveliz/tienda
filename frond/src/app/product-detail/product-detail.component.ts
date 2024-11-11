import { Component, OnInit } from '@angular/core';
import { Producto } from '../model/productos';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [HttpClientModule,RouterLink,NgbAlertModule],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductoDetailComponent implements OnInit {

  productos: Producto | undefined;

  constructor(
    private activatedRoute: ActivatedRoute, 
    private httpClient : HttpClient
    ){}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params =>{
      const id = params['id'];

      if(!id) return;

      const url = 'http://localhost:8080/productos/'+ id;

      this.httpClient.get<Producto>(url).subscribe( b => this.productos = b);

    });
  }
}

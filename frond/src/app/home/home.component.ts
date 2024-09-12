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
  productos: Producto [] =[]
  

  constructor(private httpClient: HttpClient){}

 ngOnInit(): void {
  const url = 'http://localhost:8080/productos';

    this.httpClient.get<Producto[]>(url).subscribe(productos => this.productos = productos);
 }
}

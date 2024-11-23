import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from '../model/category';

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [],
  templateUrl: './categoria.component.html',
  styleUrl: './categoria.component.css'
})
export class CategoriaComponent {

  categoryform = new FormGroup({
    id: new FormControl<number>(0),
    name: new FormControl<string>('')
  });
  categoria: Category | undefined;

  constructor( private httpCliente: HttpClient,
    private router: Router, 
    private activatedRouter: ActivatedRoute){}
  
    ngOnInit(): void {
      this.activatedRouter.params.subscribe(params => {
        const id = params['id'];
        if(!id) return;
           // Cargar las categorías y tiendas desde el backend
          this.httpCliente.get<Category>('http://localhost:8080/category/'+ id).subscribe(categoria =>{ 
            this.categoryform.reset(categoria);
            this.categoria =categoria; 
          });
      });
    }
}

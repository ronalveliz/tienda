import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from '../interface/category';
import { AuthenticationService } from '../authentication/authentication.service';
import { Producto } from '../interface/productos';

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [],
  templateUrl: './categoria.component.html',
  styleUrl: './categoria.component.css'
})
export class CategoriaComponent implements OnInit {

  categoria: Category | undefined;
  tienda: Storage | undefined;

  isUpdate: boolean = false;

  photoFile: File | undefined;
  photoPreview: string | undefined;
  isAdmin = false;
  isLoggedin = false;

  categoryform = new FormGroup({
    id: new FormControl<number>(0),
    name: new FormControl<string>('')
  });



  constructor(
    private httpClient: HttpClient,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private authService: AuthenticationService
  ){}


    ngOnInit(): void {

      this.activatedRoute.params.subscribe(params =>{
        const id = params['id'];
        if (!id){
          return;
        }
        const tiendaUrl = 'http://localhost:8080/tiendas/' + id;
        this.httpClient.get<Storage>(tiendaUrl)
        .subscribe(tienda => {
          this.tienda = tienda;
          this.categoryform.patchValue({
            tienda: this.tienda
          });
        });

        this.httpClient.get<Category>('http://localhost:8080/category/' + id).subscribe(category =>{
          this.categoryform.reset(category);
          this.categoryform.get('Tienda')?.setValue(category.tienda);

      } );

    }





    save(){
      const formData = new FormData();
      formData.append('name', this.categoryform.get('name')?.value ?? '');

      const id = this.categoryform.get('id')?.value;
      if (id && this.isUpdate){
        this.httpClient.put<Category>(`http://localhost:8080/productos/${id}`, formData)
        .subscribe(category =>{
          this.router.navigate(['/categoria', category.id, 'detail']);
        });
      }else{
        this.httpClient.post<Category>('http://localhost:8080/productos', formData)
        .subscribe(category =>{
          this.router.navigate(['/categoria', category.id, 'detail']);
        })
      }
    }




}





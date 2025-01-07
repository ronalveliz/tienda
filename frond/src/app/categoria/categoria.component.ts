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
  )
    {
      this.authService.isAdmin.subscribe(isAdmin => this.isAdmin = isAdmin);
      this.authService.isLoggedIn.subscribe(isLoggedIn => this.isLoggedin = isLoggedIn)
    }

    ngOnInit(): void {
      this.activatedRoute.params.subscribe(params =>{
        const id = params['id'];
        if(id){
          this.isUpdate = true;
          this.loadCategory(id);
        } else{
          this.isUpdate = false;
        }
      });
    }
    
    
    
    loadCategory( id: string) :void{
      this.httpClient.get<Producto>(`http://localhost:8080/productos/${id}`)
      .subscribe(producto =>{
        this.categoryform.patchValue(producto);
      });
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





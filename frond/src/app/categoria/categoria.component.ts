import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../authentication/authentication.service';
import { Category } from '../interface/category';
import { Store } from '../interface/store';

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [],
  templateUrl: './categoria.component.html',
  styleUrl: './categoria.component.css'
})
export class CategoriaComponent implements OnInit {

  categoria: Category | undefined;
  tienda: Store | undefined;

  isUpdate: boolean = false;

  photoFile: File | undefined;
  photoPreview: string | undefined;
  isAdmin = false;
  isLoggedin = false;

  categoryform = new FormGroup({
    id: new FormControl(0),
    name: new FormControl(''),
    tienda: new FormControl(),

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
        this.httpClient.get<Store>(tiendaUrl)
        .subscribe(tienda => {
          this.tienda = tienda;
          this.categoryform.patchValue({
            tienda: this.tienda
          });
        });

        this.httpClient.get<Category>('http://localhost:8080/category/' + id).subscribe(category =>{
          this.categoryform.reset(category);
          this.categoryform.get('tienda')?.setValue(category.tienda);
          this.categoria = category;
          this.isUpdate = true;
      } );
    });
  }
  save(){
    const categorys: Category = this.categoryform.value as Category;

    let formData = new FormData();
    formData.append('id', this.categoryform.get('id')?.value?.toString() ?? '0');
    formData.append('name', this.categoryform.get('name')?.value ?? '');
    formData.append('tienda', this.categoryform.get('tienda')?.value?.toString() ?? '0');

    if (this.isUpdate){
      this.httpClient.put<Category>('http://localhost:8080/category/' + categorys.id, formData)
      .subscribe(response => {this.router.navigate(['/categorias'])});
    }else{
      this.httpClient.post<Category>('http://localhost:8080/category', formData)
      .subscribe(response => {this.router.navigate(['/categorias'])});
    }
  }
  compareObjects(o1: any, o2: any): boolean {

    if (o1 && o2) {
      return o1.id == o2.id;
    }

    return o1 == o2;
  }
}

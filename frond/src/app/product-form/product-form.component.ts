import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Producto } from '../model/productos';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [ReactiveFormsModule, HttpClientModule],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css'
})
export class ProductFormComponent implements OnInit{
  
  producForm= new FormGroup ({
    id: new FormControl<number>(0),
    name:new FormControl<string>(''),
    description: new FormControl<string>(''),
    price: new FormControl<number>(0.0),
    photoUrl: new FormControl<string>(''),
    category: new FormGroup({
      id: new FormControl<number | null>(null),
      name: new FormControl<string>('')
    }),
    store: new FormGroup({
      id: new FormControl<number | null>(null),
      name: new FormControl<string>(''),
      location: new FormControl<string>('')
    })
  });

    product: Producto | undefined;
    isUpdate : boolean = false;
    photoFile : File |undefined;
    photoPreview : string | undefined;

    constructor( private httpCliente: HttpClient,
                 private router: Router, 
                 private activatedRouter: ActivatedRoute){}

  ngOnInit(): void {
    this.activatedRouter.params.subscribe(params => {
      const id = params['id'];
      if(!id) return;

        this.httpCliente.get<Producto>('http://localhost:8080/productos/'+ id).subscribe(product =>{

          this.producForm.reset(product);
          this.isUpdate =true;
          this.product =product; 
        });
    });
  }
 onFileChange(event:Event){

  let target =event.target as HTMLInputElement; // este target es el input de tipo file donde se carga el archivo
  
  if( target.files === null || target.files.length == 0){
    return;// no se procesa ningún archivo
  }

  //guardar la img para enviarlo en el save()
  this.photoFile = target.files[0];

  /* previsualizar la img por pantalla*/
  let reader = new FileReader();
  reader.onload = event => this.photoPreview  = reader.result as string;
  reader.readAsDataURL(this.photoFile);
 }

  save () {

    let formData =new FormData();
    formData.append('id', this.producForm.get('id')?.value?.toString() ?? '0');
    formData.append('name', this.producForm.get('name')?.value ?? '');    
    formData.append('description', this.producForm.get('description')?.value ?? '');
    formData.append('price', this.producForm.get('price')?.value?.toString() ?? '');
    formData.append('photoUrl', this.producForm.get('photoUrl')?.value ?? ''); 
    formData.append('descuento', this.producForm.get('descuento')?.value ? 'true' : 'false');

    const category = this.producForm.get('category');
    if (category?.value) {
      formData.append('category.id', category.get('id')?.value?.toString() ?? '0');
      formData.append('category.name', category.get('name')?.value ?? '');
    }

    const store = this.producForm.get('store');
    if (store?.value) {
      formData.append('store.id', store.get('id')?.value?.toString() ?? '0');
      formData.append('store.name', store.get('name')?.value ?? '');
      formData.append('store.location', store.get('location')?.value ?? '');
    }
    
    if(this.photoFile){
      formData.append("photo", this.photoFile);
    }

    if (this.isUpdate) {
    const url = 'http://localhost:8080/productos/' + this.product?.id;
    this.httpCliente.put<Producto>(url,formData).subscribe(producBacken => {
    this.router.navigate(['/productos', producBacken, 'detail']);
    });
    
    } else {
    const url = 'http://localhost:8080/productos';
    this.httpCliente.post<Producto>(url,formData).subscribe(producBacken => {
    this.router.navigate(['/productos', producBacken.id, 'detail']);
    });
    }
  }
  compareObjects(o1: any, o2: any): boolean {
    // console.log("Comparando objetos: ", o1, o2);

    if(o1 && o2) {
      return o1.id === o2.id;
    }
    return o1 === o2;
  }
}

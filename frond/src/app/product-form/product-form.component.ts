import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Producto } from '../model/productos';
import { Category } from '../model/category';
import { Store } from '../model/store';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [ReactiveFormsModule, HttpClientModule, FormsModule],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css'
})
export class ProductFormComponent implements OnInit {

  producForm: FormGroup;
  product: Producto | undefined;
  isUpdate: boolean = false;
  photoFile: File | undefined;
  photoPreview: string | undefined;
  categoria: Category[] = [];
  store: Store[] = [];

  constructor(private httpCliente: HttpClient,
              private router: Router,
              private activatedRouter: ActivatedRoute,
              private fb: FormBuilder) {
    this.producForm = this.fb.group({
      id: [0],
      name: [''],
      description: [''],
      price: [0.0],
      photoUrl: [''],
      category: this.fb.group({
        id: [0],
        name: ['']
      }),
      store: this.fb.group({
        id: [0],
        name: [''],
        location: ['']
      })
    });
  }

  ngOnInit(): void {
    this.activatedRouter.params.subscribe(params => {
      const id = params['id'];
      if (!id) return;

      // Cargar las categorías y tiendas desde el backend
      this.httpCliente.get<Category[]>('http://localhost:8080/categorias').subscribe(categories => {
        this.categoria = categories;
      }, error => {
        console.error('Error al cargar categorías', error);
      });

      this.httpCliente.get<Store[]>('http://localhost:8080/tiendas').subscribe(stores => {
        this.store = stores;
      }, error => {
        console.error('Error al cargar tiendas', error);
      });

      this.httpCliente.get<Producto>('http://localhost:8080/productos/' + id).subscribe(product => {
        this.producForm.reset(product);
        this.isUpdate = true;
        this.product = product;
      }, error => {
        console.error('Error al cargar el producto', error);
      });
    });
  }

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length) {
      this.photoFile = input.files[0];
      const reader = new FileReader();
      reader.onload = () => this.photoPreview = reader.result as string;
      reader.readAsDataURL(this.photoFile);
    }
  }

  save() {
    const formData = new FormData();
    formData.append('id', this.producForm.get('id')?.value?.toString() ?? '0');
    formData.append('name', this.producForm.get('name')?.value ?? '');
    formData.append('description', this.producForm.get('description')?.value ?? '');
    formData.append('price', this.producForm.get('price')?.value?.toString() ?? '');
    formData.append('photo', this.photoFile ?? '');
    formData.append('category.id', this.producForm.get('category.id')?.value?.toString() ?? '0');
    formData.append('category.name', this.producForm.get('category.name')?.value ?? '');
    formData.append('store.id', this.producForm.get('store.id')?.value?.toString() ?? '0');
    formData.append('store.name', this.producForm.get('store.name')?.value ?? '');
    formData.append('store.location', this.producForm.get('store.location')?.value ?? '');

    if (this.photoFile) {
      formData.append("photo", this.photoFile);
    }

    if (this.isUpdate) {
      const url = 'http://localhost:8080/productos/' + this.product?.id;
      this.httpCliente.put<Producto>(url, formData).subscribe(producBacken => {
        this.router.navigate(['/productos', producBacken, 'detail']);
      });

    } else {
      const url = 'http://localhost:8080/productos';
      this.httpCliente.post<Producto>(url, formData).subscribe(producBacken => {
        this.router.navigate(['/productos', producBacken.id, 'detail']);
      });
    }
  }

  compareObjects(o1: any, o2: any): boolean {
    if (o1 && o2) {
      return o1.id === o2.id;
    }
    return o1 === o2;
  }
}
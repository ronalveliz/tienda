import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Producto } from '../interface/productos';
import { Category } from '../interface/category';
import { AuthenticationService } from '../authentication/authentication.service';



@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [ReactiveFormsModule, HttpClientModule],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css'
})
export class ProductFormComponent implements OnInit {

  producto: Producto | undefined;
  category: Category | undefined;

  isUpdate: boolean = false;
  photoFile: File | undefined;
  photoPreview: string | undefined;

  authService : AuthenticationService | undefined;
  userId: string | null = null;
  isTienda = false;
  isAdmin = false;
  isLoggedin = false;

  producForm = new FormGroup({
    id: new FormControl<number>(0),
    name: new FormControl<string>(''),
    description: new FormControl<string>(''),
    price: new FormControl<string>(''),
    photoUrl: new FormControl<string>(''),
    descuento: new FormControl<boolean>(false),
    category: new FormControl(),

  });

  constructor(
              private httpCliente: HttpClient,
              private router: Router,
              private activatedRouter: ActivatedRoute,
              private fb: FormBuilder,
              authService: AuthenticationService){
                this.authService = authService;
      if (this.authService) {
        this.authService.isLoggedin.subscribe(isLoggedin => this.isLoggedin = isLoggedin);
        this.authService.isAdmin.subscribe(isAdmin => this.isAdmin = isAdmin);
        this.authService.isTienda.subscribe(isTienda => this.isTienda = isTienda);
        this.authService.userId.subscribe(userId => this.userId = userId);
      }
    }



  ngOnInit(): void {

    // Cargar las categorías desde el backend

    this.activatedRouter.params.subscribe(params => {

      const id = params['id'];
      if (!id) return;

      const categoryUrl = 'http://localhost:8080/category/' + id;

      // Cargar las categorías  desde el backend
      this.httpCliente.get<Category>(categoryUrl)
      .subscribe(category => {
        this.category = category;
        this.producForm.patchValue({
          category: this.category
        });
      });


      this.httpCliente.get<Producto>('http://localhost:8080/productos/' + id)
      .subscribe(producto => {
        this.producForm.reset();
        this.producForm.get('category')?.setValue(producto.category);
        this.producto = producto;
        this.isUpdate = true;

      });

    });
  }

  onFileChange(event: Event) {
    let target = event.target as HTMLInputElement; // este target es el input de tipo file donde se carga el archivo

    if (target.files === null || target.files.length == 0) {
      return; // no se procesa ningún archivo
    }

    this.photoFile = target.files[0]; // guardar el archivo para enviarlo luego en el save()

    // OPCIONAL: PREVISUALIZAR LA IMAGEN POR PANTALLA
    let reader = new FileReader();
    reader.onload = event => this.photoPreview = reader.result as string;
    reader.readAsDataURL(this.photoFile);
  }

  // Crear FormData
  save() {
    if(!this.category) return;

    const formValue = this.producForm.value;
    const productos: Producto = {
      ...formValue,
      price: Number(formValue.price)
    } as Producto;
    productos.category = this.category;

    const formData = new FormData();
    formData.append('id', this.producForm.get('id')?.value?.toString() ?? '0');
    formData.append('name', this.producForm.get('name')?.value ?? '');
    formData.append('description', this.producForm.get('description')?.value ?? '');
    formData.append('price', this.producForm.get('price')?.value?.toString() ?? '');
    formData.append('photoUrl', this.producForm.get('photoUrl')?.value ?? '');
    formData.append('descuento', this.producForm.get('descuento')?.value?.toString() ?? 'false');
    formData.append('category', this.producForm.get('category')?.value?.toString() ?? '0');


    if (this.photoFile) {
      formData.append("photo", this.photoFile);
    }

    if (this.isUpdate) {
      const url = 'http://localhost:8080/productos/' + this.producto?.id;

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

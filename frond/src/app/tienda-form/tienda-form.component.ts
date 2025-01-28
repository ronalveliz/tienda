import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Store } from '../interface/store';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../authentication/authentication.service';

@Component({
  selector: 'app-tienda-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './tienda-form.component.html',
  styleUrl: './tienda-form.component.css',
})
export class TiendaFormComponent implements OnInit {
  tienda: Store | undefined;

  isUpdate: boolean = false;
  isAdmin = false;
  isLoggedin = false;

  photoFile: File | undefined;
  photoPreview: string | undefined;

  tiendaForm = new FormGroup({
    id: new FormControl(0),
    name: new FormControl(''),
  });

  constructor(
    private httpClient: HttpClient,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private authService: AuthenticationService
  ) {
    this.authService.isAdmin.subscribe((isAdmin) => (this.isAdmin = isAdmin));
    this.authService.isLoggedin.subscribe(
      (isLoggedin) => (this.isLoggedin = isLoggedin)
    );
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe((params) => {
      const id = params['id'];
      if (id) {
        this.isUpdate = true;
        this.loadTienda(id);
      } else {
        this.isUpdate = false;
      }
    });
  }

  loadTienda(id: string): void {
    this.httpClient
      .get<Store>(`http://localhost:8000/stores/${id}`)
      .subscribe((tienda) => {
        this.tiendaForm.patchValue(tienda);
      });
  }
  save() {
    const formData = new FormData();
    formData.append('name', this.tiendaForm.get('mane')?.value ?? '');
    const id = this.tiendaForm.get('id')?.value;
    if (id && this.isUpdate) {
      this.httpClient
        .put<Store>(`http://localhost:8000/stores/${id}`, formData)
        .subscribe(
          (response) => {
            this.router.navigate(['/tiendas', response.id, 'details']);
          },
          (error) => {
            console.error(error);
          }
        );
    } else {
      this.httpClient
        .post<Store>('http://localhost:8000/stores', formData)
        .subscribe(
          (response) => {
            this.router.navigate(['/tiendas', response.id, 'details']);
          },
          (error) => {
            console.error(error);
          }
        );
    }
  }
  compareObjects(o1: any, o2: any): boolean {
    if (o1 && o2) {
      return o1.id == o2.id;
    }

    return o1 == o2;
  }
}


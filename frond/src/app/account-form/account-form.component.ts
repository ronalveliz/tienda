import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { User } from '../interface/user';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { AuthenticationService } from '../authentication/authentication.service';

@Component({
  selector: 'app-account-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './account-form.component.html',
  styleUrl: './account-form.component.css'
})
export class AccountFormComponent implements OnInit {
  
  user: User | undefined;
  
  isAdmin = false;
  userForm = new FormGroup({
    firstName: new FormControl(),
    lastName: new FormControl(),
    email: new FormControl(),
    phone: new FormControl(),
    PhotoUrl: new FormControl()
  });
  photoFile: File | undefined;
  photoPreview: string | undefined;

  constructor(private httpClient: HttpClient,
                      private authService: AuthenticationService,
                      private modalService: NgbModal,
                      private router: Router) { }

  ngOnInit(): void {
    this.httpClient.get<User>('https://localhost:8080/account')
    .subscribe(user=> {
      this.user = user;
      this.userForm.reset(user);
      });
  }
  save(){
    if(!this.user){
      return;
    }
    this.user.firstName = this.userForm.get('firstName')?.value;
    this.user.lastName = this.userForm.get('lastName')?.value;
    this.user.email = this.userForm.get('email')?.value;
    this.user.phone = this.userForm.get('phone')?.value;
  
    if (this.photoFile) {
      this.updateProfile();
    } else {
      this.httpClient.put<User>('http://localhost:8080/users/account', this.user)
        .subscribe(updatedUser => {
          this.user = updatedUser;
          this.router.navigateByUrl('/home');
        });
    }
  }
  onFileChange(event: Event) {
  const target = event.target as HTMLInputElement; // este target es el input de tipo file donde se carga el archivo

  if (target.files === null || target.files.length === 0) {
    return; // no se procesa ningún archivo
  }

  this.photoFile = target.files[0]; // guardar el archivo para enviarlo luego en el save()

  // OPCIONAL: PREVISUALIZAR LA IMAGEN POR PANTALLA
  const reader = new FileReader();
  reader.onload = event => this.photoPreview = reader.result as string;
  reader.readAsDataURL(this.photoFile);
}
updateProfile() {
  if (!this.photoFile) {
    return;
  }

  const formData = new FormData();
  formData.append('photo', this.photoFile);

  const url = 'http://localhost:8080/users/account/avatar';
  this.httpClient.post<User>(url, formData)
    .subscribe(updatedUser => {
      this.user = updatedUser;
      this.httpClient.put<User>('http://localhost:8080/users/account', this.user)
        .subscribe(() => {
          this.router.navigateByUrl('/home');
          if(this.user?.imgUser){
            
          }
            //this.authService.avatarUrl.next(this.user?.imgUser);
          // TODO actualiar el avatrar en el authentication service 
          // authenticationService.updateAvatar(this.user?.avatar);
        });
    });
}
 
}

 


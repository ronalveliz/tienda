import { Component, OnInit } from '@angular/core';
import { User } from '../interface/user';
import { Role } from '../interface/role';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import { ActivatedRoute, Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-usuarios-form',
  standalone: true,
  imports: [ReactiveFormsModule,RouterLink],
  templateUrl: './usuarios-form.component.html',
  styleUrl: './usuarios-form.component.css'
})
export class UsuariosFormComponent implements OnInit {

  
  users: User[] = [];
  roles = Role; // Esto hará que los valores de la enum estén disponibles en el HTML
  showSpinner = true;

  registerUserForm = new FormGroup({
    id: new FormControl (0),
    firstName: new FormControl('',Validators.required),
    lastName: new FormControl('',Validators.required),
    email: new FormControl('',[Validators.required, Validators.email]),
    password: new FormControl('',[Validators.required, Validators.minLength(8),Validators.maxLength(30)]),
    passwordConfirm: new FormControl('',[Validators.required, Validators.minLength(8),Validators.maxLength(30)]),
    phone: new FormControl(0,[Validators.required, Validators.pattern('^[0-9]{9}$')]),
    role: new FormControl<Role>(Role.USER)
  },
  {validators: this.passwordConfirmValidator}
  );

  constructor(private httpClient : HttpClient,
              private router: Router,  // esto es para navegar
              private activatedRoute: ActivatedRoute,
              private fb: FormBuilder
  ){}
  ngOnInit(): void {
    window.scrollTo(0, 0); 
  }
 
  passwordConfirmValidator(control: AbstractControl){

    if(control.get('password')?.value === control.get('passwordConfirm')?.value){
      return null;

    }else{
        return{
          'confirmError': true
        }
    }
  } 

  
  save(){
    const user: User = this.registerUserForm.value as unknown as User;
    console.log(user)
      const url = 'http://localhost:8080/users/register';
       this.httpClient.post<User>(url,user).subscribe(backendUser =>{
        this.router.navigate(['/home']); 
      });    
  } 

}

import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  // esto permite subcribirse y estar informado de  cuando el valor cambie
  isloggedIn = new BehaviorSubject<boolean>(false);

  constructor() { }

  saveToken (token: string) {
    localStorage.setItem('token', token);
    this.isloggedIn.next(true);
  }
}

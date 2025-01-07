import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { BehaviorSubject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  isLoggedIn: BehaviorSubject<boolean>;
  userEmail: BehaviorSubject<string>;
  isAdmin: BehaviorSubject<boolean>;
  userId: BehaviorSubject<number>;

  constructor() {
    this.isLoggedIn = new BehaviorSubject<boolean>(this.existsToken());
    this.userEmail = new BehaviorSubject<string>(this.getUserEmail());
    this.isAdmin = new BehaviorSubject<boolean>(this.getIsAdmin());
    this.userId = new BehaviorSubject<number>(this.getUserId());
  }

  private isLocalStorageAvailable(): boolean {
    try {
      const test = 'test';
      localStorage.setItem(test, test);
      localStorage.removeItem(test);
      return true;
    } catch (e) {
      return false;
    }
  }

  existsToken(): boolean {
    if (this.isLocalStorageAvailable()) {
      return !!localStorage.getItem('jwt_token');
    }
    return false;
  }

  saveToken(token: string) {
    if (this.isLocalStorageAvailable()) {
      localStorage.setItem('jwt_token', token);
      this.isLoggedIn.next(true);
      this.userEmail.next(this.getUserEmail());
      this.isAdmin.next(this.getIsAdmin());
      this.userId.next(this.getUserId());
    }
  }

  removeToken() {
    if (this.isLocalStorageAvailable()) {
      localStorage.removeItem('jwt_token');
      this.isLoggedIn.next(false);
      this.userEmail.next('');
      this.isAdmin.next(false);
      this.userId.next(0);
    }
  }

  getUserEmail(): string {
    if (this.isLocalStorageAvailable()) {
      const token = localStorage.getItem('jwt_token');
      if (token) {
        const decoded: any = jwtDecode(token);
        return decoded.email || '';
      }
    }
    return '';
  }

  getIsAdmin(): boolean {
    if (this.isLocalStorageAvailable()) {
      const token = localStorage.getItem('jwt_token');
      if (token) {
        const decoded: any = jwtDecode(token);
        return decoded.role === 'admin';
      }
    }
    return false;
  }

  getUserId(): number {
    if (this.isLocalStorageAvailable()) {
      const token = localStorage.getItem('jwt_token');
      if (token) {
        const decoded: any = jwtDecode(token);
        return decoded.userId || 0;
      }
    }
    return 0;
  }
}
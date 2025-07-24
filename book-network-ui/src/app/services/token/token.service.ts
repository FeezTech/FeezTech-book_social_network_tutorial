import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private isBrowser(): boolean {
    return typeof window !== 'undefined' && !!window.localStorage;
  }

  set token(token: string) {
    if (this.isBrowser()) {
      localStorage.setItem('token', token);
    }
  }

  get token(): string {
    if (this.isBrowser()) {
      return localStorage.getItem('token') as string;
    }
    return '';
  }

  isTokenNotValid(): boolean {
    return !this.isTokenValid();
  }

  isTokenValid(): boolean {
    const token = this.token;
    if (!token) {
      return false;
    }
    const jwtHelper = new JwtHelperService();
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    if (isTokenExpired && this.isBrowser()) {
      localStorage.clear();
      return false;
    }
    return true;
  }
}

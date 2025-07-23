import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  
  set token (token: string) {
    localStorage.setItem('token', token);
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

    isTokenNotValid(): boolean {
        return !this.isTokenValid();
    }

    isTokenValid(): boolean {
        const token = this.token;
        if(!token) {
            return false;
        }
        //other wise we need to decode the token
        const jwtHelper = new JwtHelperService();
        // check the expiry date of the token
        const isTokenExpired = jwtHelper.isTokenExpired(token);
        if(isTokenExpired) {
            localStorage.clear();
            return false;
        }
        return true;
    }

}

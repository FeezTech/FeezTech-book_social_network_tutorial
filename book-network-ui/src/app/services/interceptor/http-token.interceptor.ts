import { HttpHeaders, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../token/token.service';


export const httpTokenInterceptor: HttpInterceptorFn = (request, next) => {
  const tokenService = inject(TokenService);
  const token = tokenService.token; // get the token value

  if (token) {
    const authRequest = request.clone({
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + token,
      })
    });
    return next(authRequest); // ✅ correct way to pass modified request
  }

  return next(request); // ✅ original request when no token
};

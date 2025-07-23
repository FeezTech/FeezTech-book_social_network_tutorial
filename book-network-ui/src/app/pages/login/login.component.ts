
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationRequest } from '../../services/models/authentication-request';
import { AuthenticationService } from '../../services/services/authentication.service';
import { TokenService } from '../../services/token/token.service';


@Component({
  standalone: true,
  selector: 'app-login',
  imports: [
    FormsModule
],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authenticationRequest: AuthenticationRequest = {
    email: '',
    password: ''
  }
  errorMessage: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ) {
  }


  login() {
    this.errorMessage = [];
    this.authService.authenticateResponse({
      body: this.authenticationRequest
    }).subscribe({
      next: (result) => {
        //save the token
        this.tokenService.token = result.token as string;
        this.router.navigate(['books']);

      },
      error: (err) => {
        console.log(err);
        if (err.error.validationErrors) {
          this.errorMessage = err.error.validationErrors;
        } else {
          this.errorMessage.push(err.error.errorMessage);
        }
      }
    });
  }

  register() {
    this.router.navigate(['register']);
  }
}

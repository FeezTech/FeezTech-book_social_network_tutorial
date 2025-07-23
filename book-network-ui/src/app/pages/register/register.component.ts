import { Component } from '@angular/core';
import {RegistrationRequest} from '../../services/models/registration-request';
import {FormsModule} from '@angular/forms';

import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';

@Component({
  selector: 'app-register',
  imports: [
    FormsModule
],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {
  }

  registerRequest: RegistrationRequest = {
    firstName: '',
    lastName: '',
    email: '',
    password: ''
  };

  errorMessage: Array<string> = [];

  register() {
    this.errorMessage = [];
    this.authService.register({
      body: this.registerRequest
    }).subscribe({
      next: () => {
        this.router.navigate(['activate-account']);
      },
      error: (err) => {
        this.errorMessage = err.error.validationErrors;
      }
    })
  }

  login() {
    this.router.navigate(['login']).then(r => console.log(r));
  }
}

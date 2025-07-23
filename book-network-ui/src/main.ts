import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import {routes} from './app/app.routes';
import { httpTokenInterceptor } from './app/services/interceptor/http-token.interceptor';

bootstrapApplication(AppComponent, {
  ...appConfig, // Spread existing appConfig
  providers: [
    provideHttpClient(withInterceptors([httpTokenInterceptor])),
    provideRouter(routes),// Add Router
    ...(appConfig.providers || []), // Include existing providers from appConfig


  ]
}).catch((err) => console.error(err));

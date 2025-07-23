import { provideRouter } from '@angular/router';
import { provideZoneChangeDetection } from '@angular/core';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { routes } from './app.routes';

import { provideApi } from './services/api.providers';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideClientHydration(withEventReplay()),
    ...provideApi({
      rootUrl: 'http://ec2-13-48-58-134.eu-north-1.compute.amazonaws.com:8080/api/v1'
    })
  ]
};

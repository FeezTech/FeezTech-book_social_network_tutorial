import { provideRouter } from '@angular/router';
import { provideZoneChangeDetection } from '@angular/core';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { routes } from './app.routes';
import { ApplicationConfig } from '@angular/core';

import { provideApi } from './services/api.providers';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideClientHydration(withEventReplay()),
    ...provideApi({
      rootUrl: 'http://13.48.58.134:8088/api/v1'
    })
  ]
};

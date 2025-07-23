import { Provider } from '@angular/core';
import { ApiConfiguration, ApiConfigurationParams } from './api-configuration';
import { FeedBackService } from './services/feed-back.service';
import { BookService } from './services/book.service';
import { AuthenticationService } from './services/authentication.service';

export function provideApi(config: ApiConfigurationParams): Provider[] {
  return [
    {
      provide: ApiConfiguration,
      useValue: {
        rootUrl: config.rootUrl ?? 'http://localhost:8088/api/v1'
      }
    },
    FeedBackService,
    BookService,
    AuthenticationService
  ];
}

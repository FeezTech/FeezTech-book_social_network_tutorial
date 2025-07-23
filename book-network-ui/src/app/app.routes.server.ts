import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
      path: 'books/manage/:bookId',
      renderMode: RenderMode.Server  // 🔴 Don't prerender this one
    },
  {
    path: '**',
    renderMode: RenderMode.Prerender
  }
];

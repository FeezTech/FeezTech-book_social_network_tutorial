import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {MenuComponent} from '../../components/menu/menu.component';
import {BookListComponent} from '../book-list/book-list.component';

@Component({
  selector: 'app-main',
  imports: [
    RouterOutlet,
    MenuComponent,
    //BookListComponent
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {

}

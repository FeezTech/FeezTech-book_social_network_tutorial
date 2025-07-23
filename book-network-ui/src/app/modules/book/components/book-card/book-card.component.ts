import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BookResponse} from '../../../../services/models/book-response';

import {RatingComponent} from '../rating/rating.component';

@Component({
  selector: 'app-book-card',
  imports: [
    RatingComponent
],
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {
  private _book: BookResponse = {};
  private _manage: boolean = false;
  private _bookCover: string | undefined;

  get book(): BookResponse {
    return this._book;
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }


  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }



  get bookCover(): string | undefined {
    if (this._book.cover) {
      return 'data:image/jpeg;base64, ' + this._book.cover;
    }
    return 'https://picsum.photos/seed/book1/1900/800';
  }

  set bookCover(value: string | undefined) {
    this._bookCover = value;
  }

  @Output()
  private share : EventEmitter<BookResponse> = new EventEmitter<BookResponse>() ;

  @Output()
  private archive : EventEmitter<BookResponse> = new EventEmitter<BookResponse>() ;

  @Output()
  private addToWaitingList : EventEmitter<BookResponse> = new EventEmitter<BookResponse>() ;

  @Output()
  private borrow : EventEmitter<BookResponse> = new EventEmitter<BookResponse>() ;

  @Output()
  private edit : EventEmitter<BookResponse> = new EventEmitter<BookResponse>() ;

  @Output()
  private details : EventEmitter<BookResponse> = new EventEmitter<BookResponse>() ;

  //in other, to communicate all these events below from a child to a parent, we need to expose them as outputs as done Above then we will just call them to emit them

  onShowDetails() {
    this.details.emit(this._book);
  }

  onBorrow() {
    this.borrow.emit(this._book);
  }

  onAddToWaitingList() {
    this.addToWaitingList.emit(this._book);
  }

  onEdit() {
    this.edit.emit(this._book);
  }

  onShare() {
    this.share.emit(this._book);
  }

  onArchive() {
    this.archive.emit(this._book);
  }
}

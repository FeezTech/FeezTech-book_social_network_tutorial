import { NgForOf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BookResponse, PageResponseBookResponse } from '../../../../services/models';
import { BookService } from '../../../../services/services';
import { BookCardComponent } from "../../components/book-card/book-card.component";

@Component({
  selector: 'app-my-books',
  imports: [
    BookCardComponent,
    NgForOf,
    RouterModule,
],
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent implements OnInit {


    editBook(book: BookResponse) {
        this.router.navigate(['books', 'manage', book.id]);
    }
    
    shareBook(book: BookResponse) {
        this.bookService.updateShareableStatus({
            'book-id': book.id as number
        }).subscribe ({
            next: () => {
                book.shareable = !book.shareable;
            }
        }); 
    }

    archiveBook(book: BookResponse) {
        this.bookService.updateArchivedStatus({
            'book-id': book.id as number
        }).subscribe ({
            next: () => {
                book.archived = !book.archived;
            }
        });
    }

    bookResponse: PageResponseBookResponse = {};

    page = 0;
    size = 4;


    constructor(
        private bookService: BookService,
        private router: Router,
        private activatedRoute: ActivatedRoute
        ) {
    }

    ngOnInit(): void {
        this.findAllBooks();
    }

    private findAllBooks() {
        this.bookService.findAllBooksByOwner({
        page: this.page,
        size: this.size
        }).subscribe({
        next: (books) => {
            this.bookResponse = books;
        }
        });
    }

    goToFirstPage() {
        this.page = 0;
        this.findAllBooks();

    }

    goToPreviousPage() {
        this.page --;
        this.findAllBooks();

    }

    goToPage(page: number) {
        this.page = page;
        this.findAllBooks();

    }

    goToNextPage() {
        this.page ++;
        this.findAllBooks();
    }

    goToLastPage() {
        this.page = this.bookResponse.totalPages as number - 1;
        this.findAllBooks();

    }

    get isLastPage() : boolean {
        return this.page == this.bookResponse.totalPages as number - 1;
    }

}

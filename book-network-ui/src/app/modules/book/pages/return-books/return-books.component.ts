import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BorrowedBookResponse, PageResponseBorrowedBookResponse } from '../../../../services/models';
import { BookService } from '../../../../services/services';

@Component({
  selector: 'app-return-books',
  imports: [
    NgFor,
    NgIf,
    FormsModule
  ],
  templateUrl: './return-books.component.html',
  styleUrl: './return-books.component.scss'
})
export class ReturnBooksComponent implements OnInit{

    page = 0;
    size = 5;
    returnedBooks: PageResponseBorrowedBookResponse = {};
    message: string = '';
    level: 'success' |'error' = 'success';

    constructor(
        private bookService: BookService
    ) { }

    ngOnInit(): void {
        this.findAllReturnedBooks();
    }

    private findAllReturnedBooks() {
        this.bookService.findAllReturnedBooks({
            page: this.page,
            size: this.size,
        }).subscribe ({
            next: (response) => {
                this.returnedBooks = response;
            }
        })
    }


    goToFirstPage() {
        this.page = 0;
        this.findAllReturnedBooks();

    }

    goToPreviousPage() {
        this.page --;
        this.findAllReturnedBooks();

    }

    goToPage(page: number) {
        this.page = page;
        this.findAllReturnedBooks();

    }

    goToNextPage() {
        this.page ++;
        this.findAllReturnedBooks();
    }

    goToLastPage() {
        this.page = this.returnedBooks.totalPages as number - 1;
        this.findAllReturnedBooks();

    }

    get isLastPage() : boolean {
        return this.page == this.returnedBooks.totalPages as number - 1;
    }

    approveBookReturn(book: BorrowedBookResponse) {
        if(!book.returned) {
            this.level = 'error',
            this.message = 'The Book is not yet returned'
            return;
        }
        this.bookService.approvereturnedBorrowBook ({
            'book-id': book.id as number
        }).subscribe ({
            next: () => {
                this.level = 'success',
                this.message = 'Book returned Approved',
                this.findAllReturnedBooks();
            }
        })
    
    }

}

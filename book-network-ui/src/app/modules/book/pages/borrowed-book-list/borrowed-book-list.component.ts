import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BorrowedBookResponse, FeedbackRequest, PageResponseBorrowedBookResponse } from '../../../../services/models';
import { BookService, FeedBackService } from '../../../../services/services';
import { RatingComponent } from "../../components/rating/rating.component";

@Component({
  selector: 'app-borrowed-book-list',
  imports: [
    NgFor,
    NgIf,
    FormsModule,
    RatingComponent,
    /* RouterModule */
],
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent implements OnInit{

    page = 0;
    size = 5;
    selectedBook: BorrowedBookResponse | undefined;
    borrowedBooks: PageResponseBorrowedBookResponse = {};
    feedbackRequest: FeedbackRequest = {
        bookId: 0,
        comment: '',
        note: 0
    };

    constructor(
        private bookService: BookService,
        private feedbackService: FeedBackService
    ) { }

    

    returnBorrowedBook(book: BorrowedBookResponse) {
        this.selectedBook = book;
        this.feedbackRequest.bookId = book.id as number
    }

    ngOnInit(): void {
        this.findAllBorrowedBooks();
    }

    findAllBorrowedBooks() {
        this.bookService.findAllBorrowedBooks({
            page: this.page,
            size: this.size,
        }).subscribe ({
            next: (response) => {
                this.borrowedBooks = response;
            }
        })
    }


    goToFirstPage() {
        this.page = 0;
        this.findAllBorrowedBooks();

    }

    goToPreviousPage() {
        this.page --;
        this.findAllBorrowedBooks();

    }

    goToPage(page: number) {
        this.page = page;
        this.findAllBorrowedBooks();

    }

    goToNextPage() {
        this.page ++;
        this.findAllBorrowedBooks();
    }

    goToLastPage() {
        this.page = this.borrowedBooks.totalPages as number - 1;
        this.findAllBorrowedBooks();

    }

    get isLastPage() : boolean {
        return this.page == this.borrowedBooks.totalPages as number - 1;
    }

    returnBook(withFeedback: boolean) {
        this.bookService.returnBorrowBook({
            'book-id': this.selectedBook?.id as number
        }).subscribe ({
            next: () => {
            if (withFeedback) {
                this.feedbackService.saveFeedback({
                'body': this.feedbackRequest
                }).subscribe({
                next: () => {
                    this.selectedBook = undefined;
                    this.findAllBorrowedBooks(); // only after feedback is saved
                }
                });
            } else {
                this.selectedBook = undefined;
                this.findAllBorrowedBooks(); // for just return without feedback
            }
            }
        })
    }
     giveFeedback() {
        this.feedbackService.saveFeedback({
            'body': this.feedbackRequest
        }).subscribe ({
            next: () => {
                
            }
        })
    }


}

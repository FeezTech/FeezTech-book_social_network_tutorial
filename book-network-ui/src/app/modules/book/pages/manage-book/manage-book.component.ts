
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BookRequest } from '../../../../services/models';
import { BookService } from '../../../../services/services';

@Component({
    standalone: true,
  selector: 'app-manage-book',
  imports: [
    FormsModule,
    RouterModule
],
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent implements OnInit{

    bookRequest: BookRequest = {
        authorName: '',
        isbn: '',
        synopsis: '',
        title: '',
    };

    constructor (
        private bookService: BookService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
    ) {

    }

  
    ngOnInit(): void {
        const bookId = this.activatedRoute.snapshot.params['bookId'];
        if (bookId) {
            this.bookService.findByBookId({
                'book-id': bookId
            }).subscribe({
                next: (book) => {
                    this.bookRequest = {
                        Id: book.id,
                        authorName: book.authorName as string,
                        isbn: book.isbn as string,
                        synopsis: book.synopsis as string,
                        title: book.title as string,
                        shareable:  book.shareable
                    }
                    if (book.cover) {
                        this.selectedPicture = 'data:image/jpeg;base64,' + book.cover;
                    }
                }
            });
        }
    }
    
    errorMessage: Array<string> = [];
    selectedPicture: string | undefined;
    selectedBookCover: any;

    onFileSelected(event: any) {
        this.selectedBookCover = event.target.files[0];
        console.log(this.selectedBookCover);
        if (this.selectedBookCover) {
            const reader = new FileReader();
            reader.onload = () => {
                this.selectedPicture = reader.result as string;
            };
            reader.readAsDataURL(this.selectedBookCover);
        }
    }

    saveBook() {
        this.bookService.saveBook({
            body: this.bookRequest
        }).subscribe ({
            next: (bookId) => {
                this.bookService.uploadBookCoverPicture({
                    'book-id':bookId,
                    body: {
                        file: this.selectedBookCover
                    }
                }).subscribe ({
                    next: () => {
                        this.router.navigate(['/books/my-books'])
                    }
                })
            },
            error: (error) => {
                this.errorMessage = error.error.validationErrors;
            }
        });
    }

}

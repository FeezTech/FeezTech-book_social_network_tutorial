package com.feeztech.book.book;

import com.feeztech.book.file.FileUtils;
import com.feeztech.book.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.Id())
                .title(request.title())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    /*this method will be used to transform the Book to BookResponse object*/
    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .archived(book.isArchived())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .shareable(book.isShareable())
                .owner(book.getOwner().fullName())
                .isbn(book.getIsbn())
                .cover(FileUtils.readFileFromLocation(book.getBookCover())) //implement this later
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory bookTransactionHistory) {

        return BorrowedBookResponse.builder()
                .id(bookTransactionHistory.getBook().getId())
                .title(bookTransactionHistory.getBook().getTitle())
                .authorName(bookTransactionHistory.getBook().getAuthorName())
                .rate(bookTransactionHistory.getBook().getRate())
                .returned(bookTransactionHistory.isReturned())
                .isbn(bookTransactionHistory.getBook().getIsbn())
                .returnApproved(bookTransactionHistory.isReturnApproved())
                .build();
    }
}

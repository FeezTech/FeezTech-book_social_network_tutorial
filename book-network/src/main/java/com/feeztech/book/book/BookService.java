package com.feeztech.book.book;

import com.feeztech.book.common.PageResponse;
import com.feeztech.book.exception.OperationNotPermittedException;
import com.feeztech.book.file.FileStorageService;
import com.feeztech.book.history.BookTransactionHistory;
import com.feeztech.book.history.BookTransactionHistoryRepository;
import com.feeztech.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.feeztech.book.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public Integer save(BookRequest request, Authentication connectedUser) {
        /*now we want to get the user from the Authentication object*/
        User user = (User) connectedUser.getPrincipal(); /*connectedUser is cast to the User Object*/
        /*now we need to transform BookRequest to object*/
        Book book = bookMapper.toBook(request);
        /*now we need to set the owner of the book*/
        book.setOwner(user);
        /*now we need to save the book*/
        return bookRepository.save(book).getId();

    }

    public BookResponse findById(Integer bookId) {

        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new RuntimeException("No Book found with id: " + bookId));

    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {

            User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List <BookResponse> bookResponse = books.stream()
                                            .map(bookMapper::toBookResponse)
                                            .toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable); /*since we don't have anything that supports specification in JpaRepository, we need to extend JpaSpecification in our Book Repository also*/

        List <BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        /*since we have a relationship between Book and BookTransactionHistory, then we need to use that*/

        Page <BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List <BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        /*since we have a relationship between Book and BookTransactionHistory, then we need to use that*/

        Page <BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List <BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID::  " + bookId));
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            /*one important thing is that; when you have exception you need to think of how to handle the exception*/
            throw new OperationNotPermittedException("You are not the owner of this book, so you cant update others books shareable status");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID::  " + bookId));
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            /*one important thing is that; when you have exception you need to think of how to handle the exception*/
            throw new OperationNotPermittedException("You are not the owner of this book, so you cant update others books archived status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        // STEPS TO FOLLOW ON HOW TO BORROW BOOK
        //1. we need to fetch the book, to make sure that the book appears in the database
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID::  " + bookId));

        //2. we need to check if the book is not archive or not shareable

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed because the book is archived or not shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        // 3. to check if the user is different from the owner

       if (Objects.equals(book.getOwner().getId(), user.getId())) {
            /*one important thing is that; when you have exception you need to think of how to handle the exception*/
            throw new OperationNotPermittedException("You cant borrow your own book");
        }
        //4. to check if the book is already borrowed or not, because we cant borrow a book that is already borrowed
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if(isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested book is already borrowed");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();


        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        // STEP TO FOLLOW TO KNOW THE BORROWED BOOK HAS BEEN RETURNED

        //1. we need to fetch the book, to make sure that the book appears in the database
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID::  " + bookId));

        //2. we need to check if the book is not archive or not shareable

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed because the book is archived or not shareable");
        }

        User user = (User) connectedUser.getPrincipal();
        // 3. to check if the user is different from the owner

        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            /*one important thing is that; when you have exception you need to think of how to handle the exception*/
            throw new OperationNotPermittedException("You cant borrow or return your own book");
        }
        //4. we need to make sure that the user has already borrowed this book, i.e., if not, we need to return OperationNotPermittedException Message. here we are finding it by bookId and UserId

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId()).orElseThrow(() -> new OperationNotPermittedException("You didnt borrow this book. i.e You cant return what you didnt borrow"));

        bookTransactionHistory.setReturned(true);

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
        // STEP TO FOLLOW TO APPROVE THE RETURNED BORROWED BOOK

        //1. we need to fetch the book, to make sure that the book appears in the database
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID::  " + bookId));

        //2. we need to check if the book is not archive or not shareable

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed because the book is archived or not shareable");
        }

        User user = (User) connectedUser.getPrincipal();
        // 3. to check if the user is different from the owner

        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            /*one important thing is that; when you have exception you need to think of how to handle the exception*/
            throw new OperationNotPermittedException("You cant return book that is not yours");
        }

        //4. we need to make sure that the user has already borrowed this book, i.e., if not, we need to return OperationNotPermittedException Message. but here we are finding it by bookId and ownerId

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId()).orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet, so you cannot approve its return"));

        bookTransactionHistory.setReturned(true);
        bookTransactionHistory.setReturnApproved(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {
        // STEP TO FOLLOW TO UPLOAD BOOK COVER PICTURE

        //1. we need to fetch the book, to make sure that the book appears in the database
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID::  " + bookId));

        //2. to check if the user is different from the owner
        User user = (User) connectedUser.getPrincipal();

        //3. creating this inorder to have access to the folder that will be created to store all the uploaded cover image
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);


    }
}

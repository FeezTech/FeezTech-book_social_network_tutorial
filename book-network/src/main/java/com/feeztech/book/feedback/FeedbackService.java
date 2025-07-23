package com.feeztech.book.feedback;

import com.feeztech.book.book.Book;
import com.feeztech.book.common.PageResponse;
import com.feeztech.book.exception.OperationNotPermittedException;
import com.feeztech.book.book.BookRepository;
import com.feeztech.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer saveFeedback(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        // STEPS IN IMPLEMENTING SAVE METHOD

        //1. we need to fetch the book, to make sure that the book appears in the database
        Book book = bookRepository.findById(feedbackRequest.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID::  " + feedbackRequest.bookId()));

        //2. we need to check if the book is not archive or not shareable

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You cannot give a feedback for an archived or non-shareable book");
        }

        User user = (User) connectedUser.getPrincipal();
        // 3. to check if the user is different from the owner

        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            /*one important thing is that; when you have exception you need to think of how to handle the exception*/
            throw new OperationNotPermittedException("You cannot give a feedback for your own book");
        }

        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size, Authentication connectedUser)
    {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List <FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId()))
                .toList();

        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}

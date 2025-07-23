package com.feeztech.book.feedback;

import com.feeztech.book.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> book(Book book);

    @Query("""
            SELECT Feedback
                FROM Feedback feedback
                    WHERE feedback.book.id = :bookId
                        
            """)
    Page<Feedback> findAllByBookId(Integer bookId, Pageable pageable);
}

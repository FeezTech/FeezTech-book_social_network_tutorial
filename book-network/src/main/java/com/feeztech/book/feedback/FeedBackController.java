package com.feeztech.book.feedback;

import com.feeztech.book.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "FeedBack")
public class FeedBackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback
            (
                    @Valid @RequestBody FeedbackRequest feedbackRequest,
                    Authentication connectedUser
            )
    {
        return ResponseEntity.ok(feedbackService.saveFeedback(feedbackRequest, connectedUser));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbackByBook
            (
                    @PathVariable("book-id") Integer bookId,
                    @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                    Authentication connectedUser
            )
    {
        return ResponseEntity.ok(feedbackService.findAllFeedbacksByBook(bookId, page, size, connectedUser));
    }

}

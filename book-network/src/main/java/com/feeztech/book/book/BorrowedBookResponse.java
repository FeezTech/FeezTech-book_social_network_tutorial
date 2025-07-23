package com.feeztech.book.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
/*NOTE: Builder doesn't work with required Args Constructor*/
public class BorrowedBookResponse {

    private Integer id;

    private String title;

    private String authorName;

    private String isbn;

    private double rate; /*this is the average of all the feedback that we were given for a specific book multipied by the number of feedback that we have, 5 feedbacks * 5 divided by 5 stars */

    private boolean returned;

    private boolean returnApproved;
}

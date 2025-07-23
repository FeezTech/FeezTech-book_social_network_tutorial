package com.feeztech.book.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private Integer id;

    private String title;

    private String authorName;

    private String isbn;

    private String synopsis;

    private String owner;

    private byte[] cover;

    private double rate; /*this is the average of all the feedback that we were given for a specific book multipied by the number of feedback that we have, 5 feedbacks * 5 divided by 5 stars */

    private boolean archived;

    private boolean shareable;

}

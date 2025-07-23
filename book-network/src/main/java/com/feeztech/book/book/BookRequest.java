package com.feeztech.book.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequest
        (
        Integer Id, /*if the id is null, it means we want to create a new book* and if it's not null, it means we want to update an existing book*/
                          @NotNull(message = "100") /*title should not be empty*/
                          @NotEmpty(message = "100") /*title should not be empty*/
                          String title,

                          @NotNull(message = "101") /*title should not be empty*/
                          @NotEmpty(message = "101")
                          String authorName,

                          @NotNull(message = "102") /*title should not be empty*/
                          @NotEmpty(message = "102")
                          String isbn,

                          @NotNull(message = "103") /*title should not be empty*/
                          @NotEmpty(message = "103")
                          String synopsis,


                          boolean shareable
        )
{

}

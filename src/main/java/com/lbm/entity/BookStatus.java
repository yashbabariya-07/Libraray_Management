package com.lbm.entity;

import com.lbm.entity.core.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Document(collection = "book_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookStatus extends BaseEntity {

    private boolean borrowed;

    @DocumentReference(collection = "users", lazy = true)
    private User borrowedBy;

    @DocumentReference(collection = "books", lazy = true)
    private Book book;

    private Date borrowedAt;
    private Date returnedAt;
    private Date returnDate;
}

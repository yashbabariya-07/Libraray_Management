package com.lbm.entity;

import com.lbm.entity.core.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalTime;

@Document(collection = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {

    private String name;
    private String author;
    private String publisher;
    private int totalAvailableQuantity;

    private String totalAvailableTime;

    @DocumentReference(collection = "library_list", lazy = true)
    private Library library;

}

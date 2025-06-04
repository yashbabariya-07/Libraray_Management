package com.lbm.repository;

import com.lbm.entity.BookStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BookStatusRepository extends MongoRepository<BookStatus, String> {
    List<BookStatus> findByBookId(String bookId);
    List<BookStatus> findByBorrowedByIdAndBorrowedIsTrue(String userId);
    List<BookStatus> findByBookIdAndBorrowedIsTrue(String bookId);
    Optional<BookStatus> findByBookIdAndBorrowedByIdAndBorrowedIsTrue(String bookId, String userId);

    default BookStatus findByStatusOrThrow(String bookId, String userId) {
        return findByBookIdAndBorrowedByIdAndBorrowedIsTrue(bookId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "You have not borrowed this book or it has already been returned."));
    }
}

package com.lbm.repository;

import com.lbm.entity.Book;
import com.lbm.exception.EntityNotFoundException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByNameIgnoreCase(String name);
    List<Book> findByLibrary_Id(String libraryId);

    default Book findBookByIdOrThrow(String bookId) {
        return findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book", bookId));
    }

    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'author': { $regex: ?0, $options: 'i' } }, " +
            "{ 'publisher': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Book> searchBooks(String text);
}

package com.lbm.validation;

import com.lbm.entity.Book;
import com.lbm.entity.BookStatus;
import com.lbm.entity.Library;
import com.lbm.entity.User;
import com.lbm.mapper.BookStatusMapper;
import com.lbm.repository.BookRepository;
import com.lbm.repository.BookStatusRepository;
import com.lbm.repository.LibraryRepository;
import com.lbm.utils.SecurityUtil;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BookValidation {

    private static  BookStatusRepository bookStatusRepository;
    private static  LibraryRepository libraryRepository;
    private static  BookRepository bookRepository;

    public BookValidation(BookStatusRepository bookStatusRepository,
                          LibraryRepository libraryRepository,
                          BookRepository bookRepository) {
        BookValidation.bookStatusRepository = bookStatusRepository;
        BookValidation.libraryRepository = libraryRepository;
        BookValidation.bookRepository = bookRepository;
    }

    public static void returnBorrowedBooksByUser(String userId) {
        bookStatusRepository.findByBorrowedByIdAndBorrowedIsTrue(userId)
                .forEach(status -> {
                    BookStatusMapper.INSTANCE.updateStatusForReturn(status, status);
                    bookStatusRepository.save(status);

                    Book book = bookRepository.findBookByIdOrThrow(status.getBook().getId());
                    book.setTotalAvailableQuantity(book.getTotalAvailableQuantity() + 1);
                    bookRepository.save(book);
                });
    }

    public static void deleteAllBooksInLibrary(Library library) {
        List<Book> books = bookRepository.findByLibrary_Id(library.getId());
        books.forEach(book -> {
            bookStatusRepository.findByBookId(book.getId())
                    .forEach(bookStatusRepository::delete);
            bookRepository.delete(book);
        });
    }

    public static void validateNoBorrowedBooksInLibrary(Library library, String message) {
        boolean hasBorrowedBooks = bookRepository.findByLibrary_Id(library.getId()).stream()
                .flatMap(book -> bookStatusRepository.findByBookIdAndBorrowedIsTrue(book.getId()).stream())
                .findAny().isPresent();

        if (hasBorrowedBooks) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void bookReturn(List<String> bookIds) {
        User currentUser = SecurityUtil.getAuthenticatedUser();

        bookIds.forEach(bookId -> {
            Book book = bookRepository.findBookByIdOrThrow(bookId);
            BookStatus status = bookStatusRepository.findByStatusOrThrow(bookId, currentUser.getId());
            BookStatusMapper.INSTANCE.updateStatusForReturn(status, status);
            bookStatusRepository.save(status);
            book.setTotalAvailableQuantity(book.getTotalAvailableQuantity() + 1);
            bookRepository.save(book);
        });
    }

    public static void validateReturnBook(User user, String userId) {
        if ("LIBRARIAN".equalsIgnoreCase(String.valueOf(user.getRole())) && !user.getId().equals(userId)) {
            throw new IllegalArgumentException("You cannot delete another librarian.");
        }

        returnBorrowedBooksByUser(userId);

        if ("LIBRARIAN".equalsIgnoreCase(String.valueOf(user.getRole()))) {
            List<Library> libraries = libraryRepository.findByUserId(userId);

            libraries.forEach(library -> validateNoBorrowedBooksInLibrary(
                    library,
                    "Can't delete librarian account. Books from one of the libraries are currently borrowed."));
            libraries.forEach(BookValidation::deleteAllBooksInLibrary);
            libraries.forEach(libraryRepository::delete);
        }
    }
}

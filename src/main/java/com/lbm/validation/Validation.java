package com.lbm.validation;

import com.lbm.entity.Book;
import com.lbm.entity.BookStatus;
import com.lbm.entity.Library;
import com.lbm.entity.User;
import com.lbm.exception.EntityNotFoundException;
import com.lbm.repository.BookRepository;
import com.lbm.repository.BookStatusRepository;
import com.lbm.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalTime;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
public class Validation {

    private final LibraryRepository libraryRepository;
    private final BookStatusRepository bookStatusRepository;

    public void validateUserEmail(User user) {
        if (user == null) {
            throw new BadCredentialsException("Incorrect email");
        }
    }

    public void validatePassword(User user, String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Incorrect password.");
        }
    }

    public Library checkLibraryUniqueId(String id){
        Library library = libraryRepository.findByUniqueId(id);
        if (library == null) {
            throw new EntityNotFoundException("Library", id);
        }
        return library;
    }

    public void validateOpeningAndClosingTimes(Library library) {
        LocalTime open = LocalTime.parse(library.getOpenTime());
        LocalTime close = LocalTime.parse(library.getCloseTime());

        if (close.isBefore(open)) {
            throw new IllegalArgumentException(
                    "Close time '" + close + "' cannot be before Open time '" + open + "'.");
        }
        if (open.equals(close)) {
            throw new IllegalArgumentException("Opening and closing times cannot be the same.");
        }
    }

    public void deleteLibrary(Library library) {
        BookValidation.validateNoBorrowedBooksInLibrary(
                library, "Can't delete library '" +
                        library.getName() + "'. Some books are currently borrowed.");
        BookValidation.deleteAllBooksInLibrary(library);
    }

    public void checkLibraryBook(Book book, String libraryId){
        if (!book.getLibrary().getId().equals(libraryId)) {
            throw new IllegalArgumentException(
                    "Book with ID '" + book.getId() + "' does not belong to library with id '" + libraryId + "'");
        }
    }

    public void checkActiveStatus(String bookId){
        List<BookStatus> activeStatuses = bookStatusRepository.findByBookIdAndBorrowedIsTrue(bookId);
        if (!activeStatuses.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot delete book with ID '" + bookId + "' because it is currently borrowed.");
        }
    }

}

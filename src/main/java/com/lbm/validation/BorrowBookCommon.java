package com.lbm.validation;

import com.lbm.entity.Book;
import com.lbm.entity.BookStatus;
import com.lbm.repository.BookRepository;
import com.lbm.repository.BookStatusRepository;
import lombok.RequiredArgsConstructor;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BorrowBookCommon {

    private final BookStatusRepository bookStatusRepository;
    private final BookRepository bookRepository;

    public void checkUserBorrowingStatus(String userId, Date currentTime, String bookId) {
        List<BookStatus> borrowedBooks = bookStatusRepository.findByBorrowedByIdAndBorrowedIsTrue(userId);

        for (BookStatus status : borrowedBooks) {
            if (status.getReturnedAt().before(currentTime)) {
                throw new IllegalArgumentException(
                        "You have overdue books. Please return them before borrowing new ones.");
            }
        }

        if (borrowedBooks.stream().anyMatch(status -> status.getBook().getId().equals(bookId))) {
            throw new IllegalArgumentException("You have already borrowed this book.");
        }

        if (borrowedBooks.size() >= 3) {
            throw new IllegalArgumentException(
                    "You have already borrowed 3 books. Return one before borrowing another.");
        }

    }

    public Book checkBookAvailability(String bookId) {
        Book book = bookRepository.findBookByIdOrThrow(bookId);
        if (book.getTotalAvailableQuantity() <= 0) {
            throw new IllegalArgumentException("No copies of this book are currently available for borrowing.");
        }
        return book;
    }

    public void checkBorrowDuration(Book book, LocalDateTime currentTime, LocalDateTime returnedAt) {
        Duration borrowDuration = calculateBorrowDuration(currentTime, returnedAt);

        LocalTime availableTime = LocalTime.parse(book.getTotalAvailableTime());
        Duration maxAllowedDuration = Duration.standardHours(availableTime.getHourOfDay())
                .plus(Duration.standardMinutes(availableTime.getMinuteOfHour()))
                .plus(Duration.standardSeconds(availableTime.getSecondOfMinute()));

        if (borrowDuration.compareTo(maxAllowedDuration) > 0) {
            throw new IllegalArgumentException("Return time exceeds maximum allowed borrowing time.");
        }
    }

    public void updateDetail(Book book) {
        book.setTotalAvailableQuantity(book.getTotalAvailableQuantity() - 1);
        bookRepository.save(book);
    }

    public static Duration calculateBorrowDuration(LocalDateTime currentTime, LocalDateTime returnTime) {
        if (returnTime.isBefore(currentTime)) {
            throw new IllegalArgumentException(
                    "Book return time '" + returnTime + "' cannot be before borrow time '" + currentTime + "'.");
        }
        return new Duration(currentTime.toDateTime(), returnTime.toDateTime());
    }

}

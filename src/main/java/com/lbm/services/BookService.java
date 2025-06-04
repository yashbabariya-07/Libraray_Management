package com.lbm.services;

import com.lbm.dto.BookDto;
import com.lbm.dto.LibraryDto;
import com.lbm.dto.response.ApiResponse;
import com.lbm.entity.Book;
import com.lbm.entity.BookStatus;
import com.lbm.entity.Library;
import com.lbm.entity.User;
import com.lbm.mapper.BookMapper;
import com.lbm.mapper.BookStatusMapper;
import com.lbm.mapper.LibraryMapper;
import com.lbm.repository.BookRepository;
import com.lbm.repository.BookStatusRepository;
import com.lbm.repository.LibraryRepository;
import com.lbm.repository.UserRepository;
import com.lbm.utils.SearchUtil;
import com.lbm.utils.SecurityUtil;
import com.lbm.validation.BookValidation;
import com.lbm.validation.BorrowBookCommon;
import com.lbm.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;
    private final BookStatusRepository bookStatusRepository;
    private final UserRepository userRepository;
    private final Validation validation;
    private final BorrowBookCommon borrowBookCommon;

    public LibraryDto addBookToLibrary(String libraryId, BookDto bookDto) {
        User currentUser = SecurityUtil.getAuthenticatedUser();
        Library library = libraryRepository.findByIdAndUserIdOrThrow(libraryId, currentUser.getId());
        Book book = BookMapper.INSTANCE.toEntity(bookDto, library);
        Book savedBook = bookRepository.save(book);
        library.getBooks().add(savedBook);
        return LibraryMapper.INSTANCE.toDto(libraryRepository.save(library));
    }

    public ApiResponse borrowBook(String bookId, LocalDateTime returnedAt) {

        User currentUser = SecurityUtil.getAuthenticatedUser();

        DateTimeZone userZone = DateTimeZone.forID(currentUser.getTimeZone());
        LocalDateTime currentTime = new LocalDateTime(userZone);

        borrowBookCommon.checkUserBorrowingStatus(currentUser.getId(), currentTime.toDate(), bookId);

        Book book = borrowBookCommon.checkBookAvailability(bookId);

        User user = userRepository.findUserById(currentUser.getId(), "User");

        borrowBookCommon.checkBorrowDuration(book, currentTime, returnedAt);

        BookStatus status = BookStatusMapper.INSTANCE.toBookStatus(user, book, currentTime, returnedAt);

        bookStatusRepository.save(status);
        borrowBookCommon.updateDetail(book);

        return ApiResponse.builder().status(true).message(
                "Book with ID '" + bookId + "' borrowed successfully.").build();

    }

    @Transactional
    public ApiResponse returnBook(List<String> bookIds) {
        BookValidation.bookReturn(bookIds);
        return ApiResponse.builder().status(true).message("Books returned successfully.").build();
    }


    public List<BookDto> getBookList() {
        return BookMapper.INSTANCE.toDtoList(bookRepository.findAll());
    }

    public List<BookDto> getBooksByLibraryId(String libraryId) {
        return BookMapper.INSTANCE.toDtoList(bookRepository.findByLibrary_Id(libraryId));
    }

    public List<BookDto> getBookByName(String name) {
        return BookMapper.INSTANCE.toDtoList(bookRepository.findByNameIgnoreCase(name));
    }

    public BookDto getBookById(String id) {
        return BookMapper.INSTANCE.toDto(bookRepository.findBookByIdOrThrow(id));
    }

    public ApiResponse deleteBookByIdAndLibrary(String libraryId, String bookId) {

        User currentUser = SecurityUtil.getAuthenticatedUser();

        Library library = libraryRepository.findByIdAndUserIdOrThrow(libraryId, currentUser.getId());

        Book book = bookRepository.findBookByIdOrThrow(bookId);
        validation.checkLibraryBook(book, libraryId);
        validation.checkActiveStatus(bookId);

        List<BookStatus> statuses = bookStatusRepository.findByBookId(bookId);
        statuses.forEach(bookStatusRepository::delete);

        library.getBooks().removeIf(b -> b.getId().equals(bookId));
        libraryRepository.save(library);

        bookRepository.delete(book);

        return ApiResponse.builder().status(true).message(
                "Book with ID '" + bookId + "' deleted successfully from library '" + libraryId + "'.").build();
    }

    public List<BookDto> searchBooks(String input) {
        String pattern = SearchUtil.searchData(input);
        return BookMapper.INSTANCE.toDtoList(bookRepository.searchBooks(pattern));
    }

    public BookDto updateBook(BookDto bookDto) {
        User currentUser = SecurityUtil.getAuthenticatedUser();
        Book book = bookRepository.findBookByIdOrThrow(bookDto.getId());
        libraryRepository.findByIdAndUserIdOrThrow(book.getLibrary().getId(), currentUser.getId());
        BookMapper.INSTANCE.updateBookFromDto(bookDto, book);

        return BookMapper.INSTANCE.toDto(bookRepository.save(book));
    }

}

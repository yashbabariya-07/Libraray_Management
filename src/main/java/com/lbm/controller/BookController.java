package com.lbm.controller;

import com.lbm.dto.BookDto;
import com.lbm.dto.BookStatusDto;
import com.lbm.dto.LibraryDto;
import com.lbm.dto.response.ApiResponse;
import com.lbm.services.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Token")
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/add/{libraryId}")
    public LibraryDto addBookToLibrary(
            @PathVariable String libraryId, @Valid @RequestBody BookDto bookDto) {
        return bookService.addBookToLibrary(libraryId, bookDto);
    }

    @GetMapping("/get")
    public List<BookDto> getAllBooks() {
         return bookService.getBookList();
    }

    @GetMapping("/library/{id}")
    public List<BookDto> getBooksByLibraryUniqueId(@PathVariable String id) {
         return bookService.getBooksByLibraryId(id);
    }

    @GetMapping("/name/{name}")
    public List<BookDto> getBookByName(@PathVariable String name) {
         return bookService.getBookByName(name);
    }

    @GetMapping("/id/{id}")
    public BookDto getBookById(@PathVariable String id) {
         return bookService.getBookById(id);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{libraryId}/{bookId}")
    public ApiResponse deleteBook(
            @PathVariable String libraryId,
            @PathVariable String bookId) {
        return  bookService.deleteBookByIdAndLibrary(libraryId, bookId);
    }

    @GetMapping("/search/{search}")
    public List<BookDto> searchBooks(@PathVariable String search) {
         return bookService.searchBooks(search);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/borrow/{bookId}")
    public ApiResponse borrowBook(@Valid @RequestBody BookStatusDto request, @PathVariable String bookId) {
        return bookService.borrowBook(bookId, request.getReturnedAt());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/return")
    public ApiResponse returnBook(
            @RequestBody List<String> bookIds) {
        return bookService.returnBook(bookIds);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PatchMapping("/update")
    public BookDto updateLibrary(@RequestBody BookDto updatedBook) {
        return bookService.updateBook(updatedBook);
    }
}

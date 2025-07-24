package com.lbm.controller;

import com.lbm.dto.response.ApiResponse;
import com.lbm.dto.LibraryDto;
import com.lbm.services.LibraryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/library")
@SecurityRequirement(name = "Bearer Token")
public class LibraryController {

    private final LibraryService libraryService;

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/add")
    public LibraryDto createLibrary(@Valid @RequestBody LibraryDto library) {
      return libraryService.addLibrary(library);
    }

    @GetMapping("/get")
    public List<LibraryDto> getAllLibraries() {
        return libraryService.getLibraryList();
    }

    @GetMapping("/id/{id}")
    public LibraryDto getLibraryById(@PathVariable String id) {
        return libraryService.getLibraryById(id);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PatchMapping("/update")
    public LibraryDto updateLibrary(@RequestBody LibraryDto updatedLibrary) {
        return libraryService.updateLibrary(updatedLibrary);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/unique-id/{id}")
    public LibraryDto getLibraryByUniqueId(@PathVariable String id) {
         return libraryService.getLibraryByUniqueId(id);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteLibrary(@PathVariable String id) {
        return libraryService.deleteLibrary(id);
    }
}

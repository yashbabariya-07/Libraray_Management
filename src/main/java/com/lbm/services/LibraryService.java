package com.lbm.services;

import com.lbm.dto.response.ApiResponse;
import com.lbm.dto.LibraryDto;
import com.lbm.entity.Library;
import com.lbm.entity.core.LocationData;
import com.lbm.entity.User;
import com.lbm.mapper.LibraryMapper;
import com.lbm.repository.LibraryRepository;
import com.lbm.utils.SecurityUtil;
import com.lbm.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final Validation validation;

    public LibraryDto addLibrary(LibraryDto libraryDto) {
        User currentUser = SecurityUtil.getAuthenticatedUser();
        Library library = LibraryMapper.INSTANCE.toEntity(libraryDto, currentUser);
        validation.validateOpeningAndClosingTimes(library);
        return LibraryMapper.INSTANCE.toDto(libraryRepository.save(library));
    }

    public List<LibraryDto> getLibraryList() {
        LocationData currentUser = SecurityUtil.getAuthenticatedUser().getLocation();
        return LibraryMapper.INSTANCE.toDtoList(
                libraryRepository.findLibraryByLocation(currentUser.getLongitude(), currentUser.getLatitude()));
    }

    public LibraryDto getLibraryById(String id) {
        User currentUser = SecurityUtil.getAuthenticatedUser();
        return LibraryMapper.INSTANCE.toDto(libraryRepository.findByIdAndUserIdOrThrow(id, currentUser.getId()));
    }

    public LibraryDto getLibraryByUniqueId(String uniqueId) {
        return LibraryMapper.INSTANCE.toDto(validation.checkLibraryUniqueId(uniqueId));
    }

    public LibraryDto updateLibrary(LibraryDto libraryDto) {
        User currentUser = SecurityUtil.getAuthenticatedUser();
        Library library = libraryRepository.findByIdAndUserIdOrThrow(libraryDto.getId(), currentUser.getId());
        LibraryMapper.INSTANCE.updateLibraryFromDto(libraryDto, library);
        validation.validateOpeningAndClosingTimes(library);
        return LibraryMapper.INSTANCE.toDto(libraryRepository.save(library));
    }

    public ApiResponse deleteLibrary(String libraryId) {
        User currentUser = SecurityUtil.getAuthenticatedUser();
        Library library = libraryRepository.findByIdAndUserIdOrThrow(libraryId, currentUser.getId());
        validation.deleteLibrary(library);
        libraryRepository.delete(library);
        return ApiResponse.builder().status(true).message(
                "Library with ID '" + libraryId + "' deleted successfully.").build();
    }

}

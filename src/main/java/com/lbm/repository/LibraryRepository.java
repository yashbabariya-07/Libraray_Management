package com.lbm.repository;

import com.lbm.entity.Library;
import com.lbm.exception.EntityNotFoundException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository extends MongoRepository<Library, String> {
    Library findByUniqueId(String uniqueId);
    Optional<Library> findByIdAndUserId(String libraryId, String userId);
    List<Library> findByUserId(String userId);

    default Library findByIdAndUserIdOrThrow(String libraryId, String userId) {
        return findByIdAndUserId(libraryId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Library", libraryId));
    }

    @Query("{'location':{$near:{$geometry:{type:'Point',coordinates:[?1, ?0]}}}}")
    List<Library> findLibraryByLocation(double longitude, double latitude);

}

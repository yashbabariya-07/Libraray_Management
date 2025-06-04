package com.lbm.repository;

import com.lbm.entity.User;
import com.lbm.exception.EntityNotFoundException;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    Optional<User> findByName(String name);

    default User findUserById(String userId, String roleName) {
        return findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(roleName, userId));
    }

}

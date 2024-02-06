package com.chatop.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chatop.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    User findByEmail(String email);
}

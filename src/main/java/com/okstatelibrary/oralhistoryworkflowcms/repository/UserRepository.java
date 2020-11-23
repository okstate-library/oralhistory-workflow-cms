package com.okstatelibrary.oralhistoryworkflowcms.repository;

import org.springframework.data.repository.CrudRepository;

import com.okstatelibrary.oralhistoryworkflowcms.entity.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();
}
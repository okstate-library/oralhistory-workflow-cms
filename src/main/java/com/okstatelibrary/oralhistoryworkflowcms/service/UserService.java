package com.okstatelibrary.oralhistoryworkflowcms.service;

import org.springframework.stereotype.Service;

import com.okstatelibrary.oralhistoryworkflowcms.entity.User;

import java.util.List;

@Service("userDetailsService")
public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);

    void save(User user);

    User createUser(User user);

    User saveUser(User user);

    List<User> findUserList();

    void updatePassword(User user);

}
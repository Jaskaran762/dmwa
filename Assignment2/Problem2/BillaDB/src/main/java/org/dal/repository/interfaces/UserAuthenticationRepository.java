package org.dal.repository.interfaces;

import org.dal.model.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface UserAuthenticationRepository {

    boolean saveUser(User user) throws Exception;
    Optional<User> findUserByUsername(String username);
    boolean deleteUser(User user);
    List<User> getAllUsers() throws Exception;
}

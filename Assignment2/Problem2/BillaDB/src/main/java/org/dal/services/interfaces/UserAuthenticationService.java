package org.dal.services.interfaces;

import org.dal.model.User;

import java.util.List;

public interface UserAuthenticationService {

    Boolean login(User user);
    Boolean register(User user) throws Exception;

    List<User> listAllUsers() throws Exception;
}

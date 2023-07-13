package org.dal.views.interfaces;

public interface UserAuthenticationView {
    boolean login();

    boolean register() throws Exception;

    boolean listAllUsers() throws Exception;
}

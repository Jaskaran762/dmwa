package org.dal.services;

import org.dal.model.User;
import org.dal.repository.UserAuthenticationRepositoryImpl;
import org.dal.repository.interfaces.UserAuthenticationRepository;
import org.dal.services.interfaces.UserAuthenticationService;
import org.dal.util.PasswordEncoder;

import java.util.List;
import java.util.Optional;

public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserAuthenticationRepository userAuthenticationRepository;

    public UserAuthenticationServiceImpl() {
        userAuthenticationRepository = new UserAuthenticationRepositoryImpl();
    }

    @Override
    public Boolean login(User user) {

        Optional<User> extractedUser = userAuthenticationRepository.findUserByUsername(user.getUserName());
        if (extractedUser.isEmpty())
            throw new RuntimeException();
        else {
            return PasswordEncoder.comparePasswords(user.getPassword(),
                    extractedUser.get().getPassword());
        }
    }

    @Override
    public Boolean register(User user) throws Exception {

        try {
            if (userAuthenticationRepository.findUserByUsername(user.getUserName()).isPresent());
        }catch (Exception e){
            System.out.println("ok");
        }

        String password = user.getPassword();
        String encryptedPassword = PasswordEncoder.encryptPassword(password);
        user.setPassword(encryptedPassword);

        return userAuthenticationRepository.saveUser(user);
    }

    @Override
    public List<User> listAllUsers() throws Exception {
        return userAuthenticationRepository.getAllUsers();
    }

    public void resetPassword(User user) throws Exception {
        Optional<User> extractedUser = userAuthenticationRepository.findUserByUsername(user.getUserName());
        if (extractedUser.isEmpty())
            throw new RuntimeException();

        if (user.getQuestion().equals(extractedUser.get().getQuestion())
                && user.getPassword().equals(extractedUser.get().getPassword())) {

            String password = user.getPassword();
            String encryptedPassword = PasswordEncoder.encryptPassword(password);
            user.setPassword(encryptedPassword);

            user.setRole(extractedUser.get().getRole());
            userAuthenticationRepository.deleteUser(extractedUser.get());
            userAuthenticationRepository.saveUser(user);
        }
    }
}

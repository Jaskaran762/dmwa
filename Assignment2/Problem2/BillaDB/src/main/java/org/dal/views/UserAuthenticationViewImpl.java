package org.dal.views;

import org.dal.model.User;
import org.dal.services.interfaces.UserAuthenticationService;
import org.dal.views.interfaces.UserAuthenticationView;

import java.util.List;
import java.util.Scanner;

public class UserAuthenticationViewImpl implements UserAuthenticationView {

    private static final Scanner sc;
    private User user;
    private UserAuthenticationService userService;

    static {
        sc = new Scanner(System.in);
    }

    public UserAuthenticationViewImpl(User user, UserAuthenticationService userService){
        this.user = user;
        this.userService = userService;
    }

    @Override
    public boolean login(){

        System.out.println("Enter username");
        String userName = sc.nextLine();
        System.out.println("Enter password");
        String password = sc.nextLine();
        user.setUserName(userName);
        user.setPassword(password);

        return userService.login(user);
    }

    @Override
    public boolean register() throws Exception {

        System.out.println("Enter username");
        String userName = sc.nextLine();
        System.out.println("Enter password");
        String password = sc.nextLine();
        System.out.println("Enter security question");
        String question = sc.nextLine();
        System.out.println("Enter answer");
        String answer = sc.nextLine();

        user.setPassword(password);
        user.setUserName(userName);
        user.setQuestion(question);
        user.setAnswer(answer);

        return userService.register(user);
    }

    @Override
    public boolean listAllUsers() throws Exception {
        List<User> users = userService.listAllUsers();
        System.out.println("Users present are:");
        for (User user : users){
            System.out.println(user.getUserName());
        }
        return true;
    }
}

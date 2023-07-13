package org.dal;

import org.dal.model.User;
import org.dal.services.QueryHandlerService;
import org.dal.services.UserAuthenticationServiceImpl;
import org.dal.views.UserAuthenticationViewImpl;

import java.util.Scanner;

public class MainApp {


    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to the Billa DB!");

        UserAuthenticationViewImpl view = new UserAuthenticationViewImpl(new User(), new UserAuthenticationServiceImpl());
        QueryHandlerService service = new QueryHandlerService();
        System.out.println("If you are a new user, press 1 or press any other key to login");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        boolean authenticated = false;
        switch(input){
            case "1":
                view.register();
                break;
            default:
                authenticated = view.login();
                break;
        }
        if (authenticated){
            System.out.println("Login successful");
            System.out.println();
            boolean status = true;
            System.out.println("Enter queries");
            System.out.println("Press 0 to exit");
            System.out.println("Enter erd after selecting a database to print cardinality");
            while (status){
                String sql = sc.nextLine();
                switch (sql){
                    case "0":
                        status = false;
                        break;
                    default:
                        service.queryHandler(sql);
                }
            }
        }
        else{
            System.out.println("Wrong credentials");
        }
        //view.register();
        //service.queryHandler("CREATE Database Mall");
        //service.queryHandler("CREATE TABLE Consumer (id INT, name VARCHAR, phone VARCHAR);");
        //service.queryHandler("INSERT INTO Consumer (id, name, phone) VALUES (15,\"rohit\", '9955');");
        //service.queryHandler("SELECT * FROM CONSUMER WHERE id = 12;");
        //service.queryHandler("UPDATE CONSUMER SET name = roh WHERE id = 15;");
        //service.queryHandler("DELETE FROM CONSUMER WHERE id = 12;");
    }
}
package org.dal.repository.rowmapper;

import org.dal.model.Role;
import org.dal.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRowMapper {

    public static List<User> toUsersList(String usersString){
        String[] usersArray = usersString.split("\\|\\|");
        List<User> users = new ArrayList<>();
        User user = new User();
        for (String userFromString : usersArray){
            String[] userProperties = userFromString.split("~");
            if (!userProperties[0].contains("null")) {
                //user.setUserId(Integer.valueOf(userProperties[0]));
            }
            user.setUserName(userProperties[1]);
            user.setPassword(userProperties[2]);
            user.setQuestion(userProperties[3]);
            user.setAnswer(userProperties[4]);

            String roleString = userProperties[5];
            if (!roleString.equals("null")){
                String[] roleArray = roleString.split(", ");

                Role role = new Role();
                role.setRoleName(roleArray[0]);
                List<String> authorities = new ArrayList<>();
                for (int i = 1; i < roleArray.length; i++) {
                    if (i == 1)
                        authorities.add(roleArray[i].substring(1));
                    else if (i == roleArray.length - 1) {
                        authorities.add(roleArray[i].substring(0, roleArray[i].length() - 2));
                    } else {
                        authorities.add(roleArray[i]);
                    }
                }
                role.setAuthorities(authorities);
                user.setRole(role);
            }
            users.add(user);
            user = new User();
        }
        return users;
    }
}

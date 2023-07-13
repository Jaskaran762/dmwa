package org.dal.repository;

import org.dal.model.User;
import org.dal.repository.interfaces.UserAuthenticationRepository;
import org.dal.repository.rowmapper.UserRowMapper;
import org.dal.util.CustomFileFormatHandler;
import org.dal.util.CustomFileFormatImpl;
import org.dal.util.interfaces.CustomFileFormat;

import java.util.List;
import java.util.Optional;

public class UserAuthenticationRepositoryImpl implements UserAuthenticationRepository {

    private final String fileName = "loginFile";
    private final String filePath = "User";
    private CustomFileFormat customFileFormat;

    public UserAuthenticationRepositoryImpl() {
        this.customFileFormat = new CustomFileFormatImpl();
    }

    @Override
    public boolean saveUser(User user) throws Exception {
        byte[] encryptedContent = CustomFileFormatHandler.encryptData(user.toString().concat("||").getBytes());
        return this.customFileFormat.writeToFile(encryptedContent, filePath, fileName);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {

        try {
            List<User> users = getAllUsers();
            return users.stream().filter(user -> user.getUserName().equals(username)).findFirst();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean deleteUser(User user) {

        try {
            List<User> users = getAllUsers();
            this.customFileFormat.deleteFile(filePath, fileName);
            users.remove(findUserByUsername(user.getUserName()).get());

            for (User us: users){
                saveUser(us);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        byte[] encryptedBytes = this.customFileFormat.readFile(filePath,fileName);
        byte[] decryptedBytes = CustomFileFormatHandler.decryptFile(encryptedBytes);

        String usersString = new String(decryptedBytes);

        List<User> users = UserRowMapper.toUsersList(usersString);
        return users;
    }
}

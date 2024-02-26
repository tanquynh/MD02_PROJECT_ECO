package ra.service;

import ra.config.InputMethods;
import ra.constant.Constant;
import ra.model.User;
import ra.repository.FileRepo;
import ra.view.loginOrRegister.LoginAndRegister;
import ra.view.user.UserView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static ra.constant.Constant.UserStatus.OFFLINE;


public class UserService implements IShop<User> {
    FileRepo<User, Integer> userRepo;

    public UserService() {
        this.userRepo = new FileRepo<>(Constant.FilePath.USER_FILE);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public User findById(int id) {
        return userRepo.findById(id);
    }

    @Override
    public int autoId() {
        return userRepo.autoId();
    }

    public User login(String userName, String password) {
        User user = getUserByUsername(userName);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;

    }

    public User getUserByUsername(String userName) {
        List<User> users = findAll();
        for (User user : users) {
            if (user.getUserName() != null && user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public void setStatusLogin(String username, boolean newStatus) {
        List<User> users = findAll();
        boolean foundUser = false;

        for (User user : users) {
            if (user.getUserName().equals(username)) {
                user.setStatus(newStatus);
                save(user);
                foundUser = true;
            } else {
                user.setStatus(OFFLINE);
                save(user);
            }
        }

        if (!foundUser) {
            System.err.println("Không tìm thấy người dùng có tên đăng nhập: " + username);
        }
    }

    public List<User> getSortUsersList() {
        List<User> sortsUsers = findAll();
        Collections.sort(sortsUsers, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getUserName().compareTo(o2.getUserName());
            }
        });
        return sortsUsers;
    }

    public List<User> getFitterUsers(String username) {
        List<User> users = findAll();
        List<User> fitterUser = new ArrayList<>();
        for (User user : users) {
            if (user.getUserName().toLowerCase().contains(username.toLowerCase())) {
                fitterUser.add(user);
            }
        }
        return fitterUser;
    }

    public User getUserByUsename(String userName) {
        List<User> users = findAll();
        for (User user : users) {
            if (user.getUserName() != null && user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public void updateImportance(boolean status, String username) {
        List<User> users = findAll();
        for (User user : users) {
            if (user.getUserName().equals(username)) {
                user.setImportance(status);
                save(user);
                break;
            }
        }
    }

    public User userActive() {
        List<User> users = findAll();
        for (User user : users) {
            if (user != null && user.isStatus()) {
                return user;
            }
        }
        return null;
    }

    public void logout() {
        System.err.println("Bạn chắc chắn muốn thoát chứ ??");
        System.err.println("1. Có                2.Không");
        int choice = InputMethods.getInteger();
        if (choice == 1) {
            new LoginAndRegister().loginOrRegister();
        }
    }
}

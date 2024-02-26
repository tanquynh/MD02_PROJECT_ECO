package ra.service;

import ra.constant.Constant;
import ra.model.Cart;
import ra.model.Message;
import ra.model.User;
import ra.repository.FileRepo;

import java.util.List;

public class MessageService implements IShop<Message> {
    private UserService userService;

    public MessageService() {
        this.userService = new UserService();
    }

    public User userLogin() {
        User userLogin;
        userLogin = userService.userActive();
        return userLogin;
    }

    @Override
    public List<Message> findAll() {
        return userLogin().getMessages();
    }

    @Override
    public void save(Message message) {
        User user = userLogin();
        List<Message> messages = user.getMessages();
        messages.add(message);
        userService.save(user);
    }

    @Override
    public Message findById(int id) {
        return null;
    }

    @Override
    public int autoId() {
        int max = 0;
        for (Message message : userLogin().getMessages()) {
            if (message.getId() > max) {
                max = message.getId();
            }
        }
        return max + 1;
    }
}

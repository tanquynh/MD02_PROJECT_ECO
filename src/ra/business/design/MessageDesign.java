package ra.business.design;

import ra.business.model.Message;
import ra.business.model.User;

import java.util.List;

public class MessageDesign implements IShop<Message> {
    private UserDesign userService;

    public MessageDesign() {
        this.userService = new UserDesign();
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

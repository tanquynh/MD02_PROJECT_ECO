package ra.view.user;

import ra.config.InputMethods;
import ra.model.Message;

import ra.service.MessageService;
import ra.service.UserService;

import java.util.List;
import java.util.Objects;


public class MessageView {
    private MessageService messageService;
    private UserService userService;

    public MessageView() {
        this.userService = new UserService();
        this.messageService = new MessageService();
    }

    public void menuMessage() {
        List<Message> messageList = messageService.findAll();
        Message message = new Message();
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃      Message       ┃    Thoát( 0 )     ┃");
        System.out.println("┣━━━━━━━━━━━━━━━━━━━━┛━━━━━━━━━━━━━━━━━━━┫");
        System.out.println("┃  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓  ┃");
        for (Message message1 : messageList) {
            if (!Objects.equals(message1.getUserId(), userService.userActive().getId())) {
                System.out.println("┃  ┃      " + message1.getMessage() + "                           ┃  ┃");
            } else {
                System.out.println("┃  ┃      " + message1.getMessage() + "                           ┃  ┃");
            }
        }

        System.out.println("┃  ┃                                  ┃  ┃");
        System.out.println("┃  ┃                                  ┃  ┃");
        System.out.println("┃  ┃                                  ┃  ┃");
        System.out.println("┃  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛  ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

        do {
            String input = InputMethods.getString();
            message.setMessage(input);
            message.setId(messageService.autoId());
            message.setUserId((Integer) userService.userActive().getId());
            messageService.save(message);
            menuMessage();
        } while (InputMethods.getInteger() != 0);
    }
}

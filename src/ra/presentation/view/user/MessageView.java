package ra.presentation.view.user;

import ra.business.config.InputMethods;
import ra.business.model.Message;

import ra.business.design.MessageDesign;
import ra.business.design.UserDesign;

import java.util.List;
import java.util.Objects;

import static ra.business.config.ConsoleColor.*;


public class MessageView {
    private MessageDesign mesageDesign;
    private UserDesign userDesign;

    public MessageView() {
        this.userDesign = new UserDesign();
        this.mesageDesign = new MessageDesign();
    }

    public void menuMessage() {
        List<Message> messageList = mesageDesign.findAll();
        Message message = new Message();
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃      Message       ┃    Thoát( 0 )     ┃");
        System.out.println("┣━━━━━━━━━━━━━━━━━━━━┛━━━━━━━━━━━━━━━━━━━┫");
        System.out.println("┃  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓  ┃");
        for (Message message1 : messageList) {
            if (!Objects.equals(message1.getUserId(), userDesign.userActive().getId())) {
                print(YELLOW);
                System.out.println("┃  ┃      " + message1.getMessage() + "                           ┃  ┃");
                printFinish();
            } else {
                print(BLUE);
                System.out.println("┃  ┃      " + message1.getMessage() + "                           ┃  ┃");
                printFinish();
            }
        }

        System.out.println("┃  ┃                                  ┃  ┃");
        System.out.println("┃  ┃                                  ┃  ┃");
        System.out.println("┃  ┃                                  ┃  ┃");
        System.out.println("┃  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛  ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

        do {
            String input = InputMethods.getInput();
            message.setId(mesageDesign.autoId());
            message.setMessage(input);
            message.setUserId((Integer) userDesign.userActive().getId());
            mesageDesign.save(message);
            menuMessage();

        } while (InputMethods.getInteger() != 0);
    }
}

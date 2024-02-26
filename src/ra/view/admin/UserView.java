package ra.view.admin;

import ra.model.User;
import ra.service.UserService;

import java.util.List;

import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.getInteger;
import static ra.config.InputMethods.getString;
import static ra.constant.Constant.Importance.BLOCK;
import static ra.constant.Constant.Importance.OPEN;
import static ra.constant.Constant.Role.ADMIN;
import static ra.constant.Until.formattedPhoneNumber;

public class UserView {
    private  UserService userService;

    public UserView() {
        this.userService = new UserService();
    }

    public void userManagement() {
        int choice;

        do {
            print(CYAN);

            System.out.println(".--------------------------------------------------------.");
            System.out.println("|           🧡🧡        QUẢN LÝ USER        🧡🧡        |");
            System.out.println("|--------------------------------------------------------|");
            System.out.println("|                     1. DANH SÁCH USER                  |");
            System.out.println("|                     2. TÌM KIẾM USER THEO TÊN          |");
            System.out.println("|                     3. KHÓA / MỞ TÀI KHOẢN USER        |");
            System.out.println("|                     4. QUAY LẠI MENU TRƯỚC             |");
            System.out.println("|                     0. ĐĂNG XUẤT                       |");
            System.out.println("'--------------------------------------------------------'\n");
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡 : ");
            choice = getInteger();
            printFinish();

            switch (choice) {
                case 1:
                    displayUserList();
                    break;
                case 2:
                    displayUserByUserName();
                    break;
                case 3:
                    changeUserImportance();
                    break;
                case 4:
                    return;
                case 0:
                    userService.logout();
                    break;
                default:
                    break;
            }
        } while (true);
    }
    private void changeUserImportance() {
        System.out.println("Hãy nhập username bạn muốn thay đổi trạng thái:");
        String username = getString();
        User user = userService.getUserByUsename(username);
        if (user == null) {
            System.err.println("Không tìm thấy username bạn muốn đổi trạng thái !!\"");

        } else {
            if (user.getRole() == ADMIN) {
                printlnError("Không thể khóa user ADMIN !!");
            } else {
                userService.updateImportance((user.isImportance() == OPEN ? BLOCK : OPEN), username);
                printlnSuccess("Thay đổi trạng thái thành công!");
            }
        }
    }

    private void displayUserByUserName() {
        System.out.println("Nhập vào tên User cần tìm kiếm");
        String username = getString();
        List<User> fitterUsers = userService.getFitterUsers(username);
        if (fitterUsers.size() != 0) {
            print(GREEN);
            System.out.println("\n                                                  DANH SÁCH USER THEO NAME                  ");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |   NAME  |       EMAIL      |     PHONE    |    ĐỊA CHỈ   |   STATUS  |  ROLE | IMPORTANCE |  CREATE AT  |    UPDATE AT  " + "|");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");

            for (User user : fitterUsers) {
                System.out.printf("|%-5d | %-7s | %-16s | %-12s | %-12s | %-9s | %-5s | %-10s | %-11s | %-13s |%n",
                        user.getId(), user.getUserName(), user.getEmail(), formattedPhoneNumber(user.getPhone()), user.getAddress(), (user.isStatus() ? "ONLINE" : "OFFLINE"), (user.getRole() == 1 ? "ADMIN" : "USER"), user.isImportance() ? "OPEN" : "BLOCK", user.getCreateAt(), (user.getUpdateAt()) == null ? "Chưa cập nhật" : user.getUpdateAt());
            }
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");
            printFinish();
        } else {
            printlnError("Không có user phù hợp!!");

        }
    }

    private void displayUserList() {
        List<User> sortUsers = userService.getSortUsersList();
        if (sortUsers.size() != 0) {

            print(GREEN);
            System.out.println("\n                                                   DANH SÁCH USER SẮP XẾP THEO NAME                  ");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |   NAME  |       EMAIL      |     PHONE    |    ĐỊA CHỈ   |   STATUS  |  ROLE | IMPORTANCE |  CREATE AT  |    UPDATE AT  " + "|");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");

            for (User user : sortUsers) {
                System.out.printf("|%-5d | %-7s | %-16s | %-12s | %-12s | %-9s | %-5s | %-10s | %-11s | %-13s |%n",
                        user.getId(), user.getUserName(), user.getEmail(), formattedPhoneNumber(user.getPhone()), user.getAddress(), (user.isStatus() ? "ONLINE" : "OFFLINE"), (user.getRole() == 1 ? "ADMIN" : "USER"), user.isImportance() ? "OPEN" : "BLOCK", user.getCreateAt(), (user.getUpdateAt()) == null ? "Chưa cập nhật" : user.getUpdateAt());
            }
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");
            printFinish();
        } else {
            printlnError("Không có user !!");

        }

    }
}

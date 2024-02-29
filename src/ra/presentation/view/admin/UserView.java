package ra.presentation.view.admin;

import ra.business.model.User;
import ra.business.design.UserDesign;

import java.util.List;

import static ra.business.config.ConsoleColor.*;
import static ra.business.config.InputMethods.getInteger;
import static ra.business.config.InputMethods.getString;
import static ra.business.constant.Constant.Importance.BLOCK;
import static ra.business.constant.Constant.Importance.OPEN;
import static ra.business.constant.Constant.Role.ADMIN;
import static ra.business.constant.Until.formattedPhoneNumber;

public class UserView {
    private UserDesign userDesign;

    public UserView() {
        this.userDesign = new UserDesign();
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
                    userDesign.logout();
                    break;
                default:
                    break;
            }
        } while (true);
    }
    private void changeUserImportance() {
        System.out.println("Hãy nhập username bạn muốn thay đổi trạng thái:");
        String username = getString();
        User user = userDesign.getUserByUsename(username);
        if (user == null) {
            System.err.println("Không tìm thấy username bạn muốn đổi trạng thái !!\"");

        } else {
            if (user.getRole() == ADMIN) {
                printlnError("Không thể khóa user ADMIN !!");
            } else {
                userDesign.updateImportance((user.isImportance() == OPEN ? BLOCK : OPEN), username);
                printlnSuccess("Thay đổi trạng thái thành công!");
            }
        }
    }

    private void displayUserByUserName() {
        System.out.println("Nhập vào tên User cần tìm kiếm");
        String username = getString();
        List<User> fitterUsers = userDesign.getFitterUsers(username);
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
        List<User> sortUsers = userDesign.getSortUsersList();
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

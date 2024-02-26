package ra.view.admin;

import ra.config.InputMethods;
import ra.config.Validate;
import ra.model.User;
import ra.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.getInteger;
import static ra.config.InputMethods.scanner;
import static ra.constant.Until.formattedPhoneNumber;

public class MyAccount {
    private UserService userService ;

    public MyAccount() {
        this.userService = new UserService();
    }

    public void MyAccount() {
        int choice;
        do {

            print(BLUE);

            System.out.println(".--------------------------------------------------------.");
            System.out.println("|                     MENU MY PROFILE                    |");
            System.out.println("|--------------------------------------------------------|");
            System.out.println("|                     1. ĐỔI MẬT KHẨU                    |");
            System.out.println("|                     2. HIỂN THỊ THÔNG TIN              |");
            System.out.println("|                     3. CHỈNH SỬA THÔNG TIN            |");
            System.out.println("|                     4. QUAY LẠI MENU TRƯỚC             |");
            System.out.println("|                     0. ĐĂNG XUẤT                       |");
            System.out.println("'--------------------------------------------------------'\n");

            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡: ");
            printFinish();

            choice = getInteger();
            switch (choice) {
                case 1:
                    changePassword();
                    break;
                case 2:
                    showInforUser();
                    break;
                case 3:
                    changeInforUser();
                    break;
                case 4:
                    return;
                case 0:
                    userService.logout();
                    break;
                default:
                    break;
            }

        } while (choice != 5);
    }
    private void changePassword() {
        System.out.println("Mời bạn nhập mật khẩu cũ:");
        String oldPassword = scanner().nextLine();

        if (userService.userActive().getPassword().equals(oldPassword)) {
            boolean newPasswordValid = false;

            while (!newPasswordValid) {
                System.out.println("Hãy nhập vào mật khẩu mới:");
                String newPassword = InputMethods.scanner().nextLine();

                if (Validate.isValidPassWord(newPassword)) {
                    int userId = (int) userService.userActive().getId();
                    User user = userService.findById(userId);
                    user.setPassword(newPassword);
                    userService.save(user);
                    newPasswordValid = true;
                    System.out.println("Đổi mật khẩu thành công!");
                } else {
                    System.err.println("Mật khẩu mới không hợp lệ. Hãy thử lại.");
                }
            }
        } else {
            System.err.println("Mật khẩu cũ không chính xác. Thay đổi mật khẩu thất bại.");
        }
    }
    private void changeInforUser() {
        System.out.println("Thay đổi thông tin User");
        int userId = (int) userService.userActive().getId();
        List<User> users = userService.findAll();
        User user = userService.findById(userId);
        while (true) {
            System.out.println("Hãy nhâp vào họ và tên đầy đủ :(Enter để bỏ qua) ");
            String fullName = InputMethods.scanner().nextLine();
            if (fullName.isEmpty()) {
                break;
            } else if (Validate.isValidFullName(fullName)) {
                user.setFullName(fullName);
                break;
            }
        }

        while (true) {
            System.out.println("Hãy nhập tên đăng nhập mới: (Enter để bỏ qua)");
            String username = scanner().nextLine();
            if (username.isEmpty()) {
                break;
            } else if (Validate.isValidFullName(username)) {
                boolean isUsernameAvailable = true;
                if (users != null) {
                    for (User existingUser : users) {
                        if (existingUser.getUserName().trim().equals(username)) {
                            printlnError("Tên đăng nhập đã được sử dụng, mời nhập tên đăng nhập mới.");
                            isUsernameAvailable = false;
                            break;
                        }
                    }
                } else {
                    isUsernameAvailable = false;
                }

                if (isUsernameAvailable) {
                    user.setUserName(username);
                    break; // Kết thúc vòng lặp khi tên đăng nhập hợp lệ và không trùng lặp
                }
            }
        }

        while (true) {
            System.out.println("Hãy nhập vào email mới:(Enter để bỏ qua) ");
            String email = scanner().nextLine();
            if (email.isEmpty()) {
                break;
            } else if (Validate.isValidEmail(email)) {
                boolean isEmailAvailable = true;

                if (users != null) {
                    for (User existingUser : users) {
                        if (existingUser.getEmail().trim().equals(email)) {
                            printlnError("Email đã được sử dụng, mời nhập email mới.");
                            isEmailAvailable = false;
                            break;
                        }
                    }
                } else {
                    isEmailAvailable = false;
                }

                if (isEmailAvailable) {
                    user.setEmail(email);
                    break; // Kết thúc vòng lặp khi email hợp lệ và không trùng lặp
                }
            }
        }

        while (true) {
            System.out.println("Hãy nhập vào số điện thoại: (Enter để bỏ qua) ");
            String phone = scanner().nextLine();
            if (phone.isEmpty()) {
                break;
            } else if (Validate.isValidPhone(phone)) {
                boolean isPhoneAvailable = true;
                if (users != null) {
                    for (User existingUser : userService.findAll()) {
                        if (existingUser.getPhone().trim().equals(phone)) {
                            printlnError("Số điện thoại đã được sử dụng, mời nhập số điện thoại mới.");
                            isPhoneAvailable = false;
                            break;
                        }
                    }
                } else {
                    isPhoneAvailable = false;
                }

                if (isPhoneAvailable) {
                    user.setPhone(phone);
                    break; // Kết thúc vòng lặp khi số điện thoại hợp lệ và không trùng lặp
                }
            }
        }

        // Nhập địa chỉ
        while (true) {
            System.out.println("Hãy nhập vào địa chỉ: (Enter để bỏ qua) ");
            String address = scanner().nextLine();
            if (address.isEmpty()) {
                break;
            } else if (Validate.isValidAddress(address)) {
                user.setAddress(address);
                break;
            }

        }
        user.setUpdateAt(LocalDate.now());
        userService.save(user);
        System.out.println("Thay đổi thông tin thành công!");

    }

    private void showInforUser() {
        int userId = (int) userService.userActive().getId();
        User user = userService.findById(userId);
        print(GREEN);
        System.out.println("\n                                                   THÔNG TIN USER                  ");
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------|");
        System.out.println("|" + "  ID  |   NAME  |       EMAIL      |     PHONE    |    ĐỊA CHỈ   |   STATUS  |  ROLE | IMPORTANCE |  CREATE AT  |   UPDATE AT " + "|");
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------|");

        System.out.printf("|%-5d | %-7s | %-16s | %-12s | %-12s | %-9s | %-5s | %-10s | %-11s | %-11s |%n",
                user.getId(), user.getUserName(), user.getEmail(), formattedPhoneNumber(user.getPhone()), user.getAddress(), (user.isStatus() ? "ONLINE" : "OFFLINE"), (user.getRole() == 1 ? "ADMIN" : "USER"), user.isImportance() ? "OPEN" : "BLOCK", user.getCreateAt(), (user.getUpdateAt()) == null ? "Chưa cập nhật" : user.getUpdateAt());

        System.out.println("|------------------------------------------------------------------------------------------------------------------------------|");
        printFinish();

    }
}

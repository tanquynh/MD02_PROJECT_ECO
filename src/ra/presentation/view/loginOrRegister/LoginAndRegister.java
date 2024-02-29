package ra.presentation.view.loginOrRegister;

import org.mindrot.jbcrypt.BCrypt;
import ra.business.config.InputMethods;
import ra.business.config.Validate;
import ra.business.model.User;

import ra.business.design.UserDesign;
import ra.presentation.view.admin.AdminView;
import ra.presentation.view.user.UserView;

import java.time.LocalDate;
import java.util.List;

import static ra.business.config.ConsoleColor.*;
import static ra.business.config.ConsoleColor.printlnError;
import static ra.business.config.InputMethods.*;
import static ra.business.config.Validate.isValidFullName;
import static ra.business.constant.Constant.ADMIN_CODE;
import static ra.business.constant.Constant.Role.ADMIN;
import static ra.business.constant.Constant.UserStatus.ONLINE;

public class LoginAndRegister {
    private UserDesign userDesign;


    public LoginAndRegister() {

        this.userDesign = new UserDesign();
    }

    public void loginOrRegister() {
        print(BLUE);


        System.out.println("                                         .-------------------------------------------------------------------------------.");
        System.out.println("                                         |        ღ ღ (¯`◕‿◕´¯) ღ ღ       CỬA HÀNG KARA       ღ ღ (¯`◕‿◕´¯) ღ ღ         |");
        System.out.println("                                         |-------------------------------------------------------------------------------|");
        System.out.println("                                         |                                                                               |");
        System.out.println("                                         |                                 1. ĐĂNG NHẬP                                  |");
        System.out.println("                                         |                                 2. ĐĂNG KÝ                                    |");
        System.out.println("                                         |                                 0. THOÁT                                      |");
        System.out.println("                                         |                                                                               |");
        System.out.println("                                         '-------------------------------------------------------------------------------'\n");


        System.out.print("Nhập vào lựa chọn của bạn 🧡🧡 :   ");
        int choice = getInteger();
        switch (choice) {
            case 1:
                User user = login();
                break;
            case 2:
                User user1 = registerUser();
                userDesign.save(user1);
                printlnSuccess("Đăng ký thành công !");
                loginOrRegister();
                break;
            case 0:
                break;
        }
    }


    private User login() {
        System.out.println(BLUE + " ღ ღ (¯`◕‿◕´¯) ღ ღ-------- ĐĂNG NHẬP--------  ღ ღ (¯`◕‿◕´¯) ღ ღ");
        String pass;
        String userName;
        printlnMess("Thực hiện đăng nhập 🧡😍:");
        while (true) {
            System.out.print("UserName: ");
            userName = getString();
            if (isValidFullName(userName)) {
                break;
            }
        }
        while (true) {
            System.out.print("Password: ");
            pass = scanner().nextLine();
            if (Validate.isValidPassWord(pass)) {
                break;
            }
        }
        User user;
        user = userDesign.login(userName, pass);

        if (user != null) {
            if (user.isImportance()) {
                userDesign.setStatusLogin(userName, ONLINE);
                if (user.getRole() == ADMIN) {
                    new AdminView().displayAdminMenu();

                } else {
                    new UserView().displayUserMenu();
                }

            } else {
                printlnError("Tài khoản của bạn đã bị khóa😂😂 !!");
                loginOrRegister();
            }


        } else {
            printlnError("Đăng nhập thấy bại,Mật khẩu hoặc UserName ko trùng hợp!!! ");
            loginOrRegister();

        }
        return user;
    }

    private User registerUser() {
        System.out.println(BLUE + " ღ ღ (¯`◕‿◕´¯) ღ ღ-------- ĐĂNG KÝ--------  ღ ღ (¯`◕‿◕´¯) ღ ღ");
        List<User> users = userDesign.findAll();
        User user = new User();
        user.setId(userDesign.autoId());
        printlnMess("Vui lòng đăng ký tài khoản !!");
        // Chọn role của người dùng
        System.out.println("Hãy chọn role của bạn: ");
        System.out.println("1: ADMIN");
        System.out.println("2: USER");
        int role = getInteger();
        if (role == ADMIN) {
            // Nếu là ADMIN, yêu cầu nhập mã xác nhận ADMIN
            printlnMess("Nhập vào mã xác nhận ADMIN: ");
            String adminCode = getString();

            if (!adminCode.equals(ADMIN_CODE)) {
                printlnError("Mã xác thực không đúng, vui lòng nhập lại.");
                return registerUser(); // Gọi lại phương thức để người dùng nhập lại
            }
        }

        user.setRole(role);

        // Nhập họ và tên đầy đủ
        while (true) {
            System.out.println("Hãy nhâp vào họ và tên đầy đủ: ");
            String fullName = InputMethods.scanner().nextLine();

            if (Validate.isValidFullName(fullName)) {
                user.setFullName(fullName);
                break;
            }
        }

        // Nhập tên đăng nhập
        while (true) {
            System.out.println("Hãy nhập tên đăng nhập: ");
            String username = InputMethods.scanner().nextLine();

            if (Validate.isValidFullName(username)) {
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

        // Nhập mật khẩu
        while (true) {
            System.out.println("Hãy nhập vào mật khẩu: ");
            String password = InputMethods.scanner().nextLine();

            if (Validate.isValidPassWord(password)) {
                user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(5))); // mã hóa pass
//                user.setPassword(password);
                break;
            }
        }

        // Nhập email
        while (true) {
            System.out.println("Hãy nhập vào email đăng ký: ");
            String email = InputMethods.scanner().nextLine();

            if (Validate.isValidEmail(email)) {
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

        // Nhập số điện thoại
        while (true) {
            System.out.println("Hãy nhập vào số điện thoại: ");
            String phone = InputMethods.scanner().nextLine();

            if (Validate.isValidPhone(phone)) {
                boolean isPhoneAvailable = true;
                if (users != null) {
                    for (User existingUser : userDesign.findAll()) {
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
            System.out.println("Hãy nhập vào địa chỉ: ");
            String address = InputMethods.scanner().nextLine();

            if (Validate.isValidAddress(address)) {
                user.setAddress(address);
                break;
            }
        }
        user.setCreateAt(LocalDate.now());

        // Đăng ký hoàn thành, trả về đối tượng User đã tạo
        return user;
        
        
//        
//        List<User> users = userDesign.findAll();
//        User user = new User();
//        user.setId(userDesign.autoId());
//        printlnMess("Vui lòng đăng ký tài khoản !!");
//        // Chọn role của người dùng
//        user.setRole(USER);
//
//        // Nhập họ và tên đầy đủ
//        while (true) {
//            System.out.println("Hãy nhâp vào họ và tên đầy đủ: ");
//            String fullName = InputMethods.scanner().nextLine();
//
//            if (Validate.isValidFullName(fullName)) {
//                user.setFullName(fullName);
//                break;
//            }
//        }
//
//        // Nhập tên đăng nhập
//        while (true) {
//            System.out.println("Hãy nhập tên đăng nhập: ");
//            String username = InputMethods.scanner().nextLine();
//
//            if (Validate.isValidFullName(username)) {
//                boolean isUsernameAvailable = true;
//
//                if (users != null) {
//                    for (User existingUser : users) {
//                        if (existingUser.getUserName().trim().equals(username)) {
//                            printlnError("Tên đăng nhập đã được sử dụng, mời nhập tên đăng nhập mới.");
//                            isUsernameAvailable = false;
//                            break;
//                        }
//                    }
//                } else {
//                    isUsernameAvailable = false;
//                }
//
//                if (isUsernameAvailable) {
//                    user.setUserName(username);
//                    break; // Kết thúc vòng lặp khi tên đăng nhập hợp lệ và không trùng lặp
//                }
//            }
//        }
//
//        // Nhập mật khẩu
//        while (true) {
//            System.out.println("Hãy nhập vào mật khẩu: ");
//            String password = InputMethods.scanner().nextLine();
//
//            if (Validate.isValidPassWord(password)) {
//                user.setPassword(password);
//                break;
//            }
//        }
//
//        // Nhập email
//        while (true) {
//            System.out.println("Hãy nhập vào email đăng ký: ");
//            String email = InputMethods.scanner().nextLine();
//
//            if (Validate.isValidEmail(email)) {
//                boolean isEmailAvailable = true;
//
//                if (users != null) {
//                    for (User existingUser : users) {
//                        if (existingUser.getEmail().trim().equals(email)) {
//                            printlnError("Email đã được sử dụng, mời nhập email mới.");
//                            isEmailAvailable = false;
//                            break;
//                        }
//                    }
//                } else {
//                    isEmailAvailable = false;
//                }
//
//                if (isEmailAvailable) {
//                    user.setEmail(email);
//                    break; // Kết thúc vòng lặp khi email hợp lệ và không trùng lặp
//                }
//            }
//        }
//
//        // Nhập số điện thoại
//        while (true) {
//            System.out.println("Hãy nhập vào số điện thoại: ");
//            String phone = InputMethods.scanner().nextLine();
//
//            if (Validate.isValidPhone(phone)) {
//                boolean isPhoneAvailable = true;
//                if (users != null) {
//                    for (User existingUser : userDesign.findAll()) {
//                        if (existingUser.getPhone().trim().equals(phone)) {
//                            printlnError("Số điện thoại đã được sử dụng, mời nhập số điện thoại mới.");
//                            isPhoneAvailable = false;
//                            break;
//                        }
//                    }
//                } else {
//                    isPhoneAvailable = false;
//                }
//
//                if (isPhoneAvailable) {
//                    user.setPhone(phone);
//                    break; // Kết thúc vòng lặp khi số điện thoại hợp lệ và không trùng lặp
//                }
//            }
//        }
//
//        // Nhập địa chỉ
//        while (true) {
//            System.out.println("Hãy nhập vào địa chỉ: ");
//            String address = InputMethods.scanner().nextLine();
//
//            if (Validate.isValidAddress(address)) {
//                user.setAddress(address);
//                break;
//            }
//        }
//        user.setCreateAt(LocalDate.now());
//
//        // Đăng ký hoàn thành, trả về đối tượng User đã tạo
//        return user;
    }
}

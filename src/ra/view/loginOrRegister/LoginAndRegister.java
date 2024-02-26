package ra.view.loginOrRegister;

import ra.config.InputMethods;
import ra.config.Validate;
import ra.model.User;
import ra.service.CartService;
import ra.service.CategoryService;
import ra.service.ProductService;
import ra.service.UserService;
import ra.view.admin.AdminView;
import ra.view.user.UserView;

import java.time.LocalDate;
import java.util.List;

import static ra.config.ConsoleColor.*;
import static ra.config.ConsoleColor.printlnError;
import static ra.config.InputMethods.getString;
import static ra.config.InputMethods.scanner;
import static ra.config.Validate.isValidFullName;
import static ra.constant.Constant.Role.ADMIN;
import static ra.constant.Constant.Role.USER;
import static ra.constant.Constant.UserStatus.ONLINE;

public class LoginAndRegister {
    private UserService userService;
    private ProductService productService;
    private CategoryService categoryService;
    private CartService cartService;

    public LoginAndRegister() {
        this.cartService = new CartService();
        this.categoryService = new CategoryService();
        this.productService = new ProductService();
        this.userService = new UserService();
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
        int choice = InputMethods.getInteger();
        switch (choice) {
            case 1:
                User user = login();
                break;
            case 2:
                User user1 = registerUser();
                userService.save(user1);
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
        user = userService.login(userName, pass);

        if (user != null) {
            if (user.isImportance()) {
                userService.setStatusLogin(userName, ONLINE);
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
        List<User> users = userService.findAll();
        User user = new User();
        user.setId(userService.autoId());
        printlnMess("Vui lòng đăng ký tài khoản !!");
        // Chọn role của người dùng
        user.setRole(USER);

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
                user.setPassword(password);
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
    }
}

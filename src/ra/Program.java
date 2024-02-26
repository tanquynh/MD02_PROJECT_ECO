package ra;

import ra.view.loginOrRegister.LoginAndRegister;
import ra.view.user.UserView;

public class Program {
    public static void main(String[] args) {
        LoginAndRegister loginAndRegister = new LoginAndRegister();
        loginAndRegister.loginOrRegister();

//        AdminView adminView = new AdminView();
//        adminView.displayAdminMenu();
//        String password = "password123";
//        String encodedPassword = PasswordEncoder.encode(password);
//        System.out.println("Mật khẩu đã mã hóa: " + encodedPassword);
    }
}
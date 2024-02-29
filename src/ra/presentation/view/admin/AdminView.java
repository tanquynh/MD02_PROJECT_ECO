package ra.presentation.view.admin;

import ra.business.design.UserDesign;

import static ra.business.config.ConsoleColor.*;
import static ra.business.config.InputMethods.getInteger;

public class AdminView {
    private UserDesign userDesign;

    public AdminView() {
        this.userDesign = new UserDesign();
    }
    public void displayAdminMenu() {
        int choice;
        do {
            print(PURPLE);
            System.out.println(".--------------------------------------------------------.");
            System.out.println("| WELCOME ADMIN : "+ userDesign.userActive().getUserName()+"                                  |");
            System.out.println("|--------------------------------------------------------|");
            System.out.println("|                     1. QUẢN LÝ NGƯỜI DÙNG              |");
            System.out.println("|                     2. QUẢN LÝ DANH MỤC                |");
            System.out.println("|                     3. QUẢN LÝ SẢN PHẨM                |");
            System.out.println("|                     4. QUẢN LÝ ĐƠN HÀNG                |");
            System.out.println("|                     0. ĐĂNG XUẤT                       |");
            System.out.println("'--------------------------------------------------------'\n");
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡 :   ");
            printFinish();
            choice = getInteger();
            switch (choice) {
                case 1:
                    new UserView().userManagement();
                    break;

                case 2:
                    new CategoryView().displayAdminCategory();
                    break;
                case 3:
                    new ProductView().displayMenuAdminMenuProduct();
                    break;
                case 4:
                    new OrderView().menuAdminOrder();
                    break;
                case 0:
                    userDesign.logout();
                    break;
                default:
                    break;
            }
        } while (choice != 7);
    }




}

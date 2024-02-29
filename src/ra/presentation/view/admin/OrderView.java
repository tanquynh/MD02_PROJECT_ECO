package ra.presentation.view.admin;

import ra.business.design.UserDesign;
import ra.business.model.Cart;
import ra.business.model.Order;
import ra.business.model.Product;
import ra.business.model.User;
import ra.business.design.OrderDesign;
import ra.business.design.ProductDesign;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ra.business.config.ConsoleColor.*;
import static ra.business.config.InputMethods.getInteger;
import static ra.business.constant.OrderStatus.*;
import static ra.business.constant.Until.formatCurrency;
import static ra.business.constant.Until.formattedPhoneNumber;

public class OrderView {
    private UserDesign userDesign;
    private OrderDesign oderDesign;
    private ProductDesign productDesign;

    public OrderView() {
        this.userDesign = new UserDesign();
        this.oderDesign = new OrderDesign();
        this.productDesign = new ProductDesign();
    }

    public void menuAdminOrder() {

        while (true) {

            print(BLUE);
            System.out.println(".---------------------------------------------------------------------.");
            System.out.println("|                      ADMIN-QUẢN LÝ ĐƠN HÀNG                         |");
            System.out.println("|---------------------------------------------------------------------|");
            System.out.println("|                     1. HIỂN THỊ TẤT CẢ ĐƠN HÀNG                     |");
            System.out.println("|                     2. TÌM KIẾM ĐƠN HÀNG THEO USER                  |");
            System.out.println("|                     3. DANH SÁCH ĐƠN HÀNG ĐANG CHỜ XÁC NHẬN         |");
            System.out.println("|                     4. DANH SÁCH ĐƠN HÀNG ĐÃ XÁC NHẬN               |");
            System.out.println("|                     5. DANH SÁCH ĐƠN HÀNG ĐÃ BỊ HỦY                 |");
            System.out.println("|                     6. DANH SÁCH ĐƠN HÀNG ĐANG GIAO                 |");
            System.out.println("|                     7. DANH SÁCH ĐƠN HÀNG ĐÃ GIAO                   |");
            System.out.println("|                     8. HIỂN THỊ CHI TIẾT ĐƠN HÀNG                   |");
            System.out.println("|                     9. QUAY LẠI MENU TRƯỚC                          |");
            System.out.println("|                     0. ĐĂNG XUẤT                                    |");
            System.out.println("'---------------------------------------------------------------------'");
            System.out.println("Nhập lựa chọn của bạn :");
            printFinish();

            int choice = getInteger();
            switch (choice) {
                case 1:
                    showAllOrder();
                    break;
                case 2:
                    sortOrderByUserName();
                    break;
                case 3:
                    orderConfirmation();
                    break;
                case 4:
                    orderStatus(ACCEPT);

                    break;
                case 5:
                    orderStatus(CANCEL);
                    break;
                case 6:
                    orderStatus(DELIVERY);
                    break;
                case 7:
                    orderStatus(SUCCESS);
                    break;
                case 8:
                    orderDetailAdmin();
                    break;
                case 9:
                    return;
                case 0:
                    userDesign.logout();
                    break;
                default:
                    printlnError("--->> Lua chon khong phu hop. Vui long chon lai ❤ ");
            }

        }
    }

    private void sortOrderByUserName() {
        List<User> sortUsers = userDesign.findAll();

        if (sortUsers.size() != 0) {

            print(GREEN);
            System.out.println("\n                                                   DANH SÁCH USER                   ");
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
            System.err.println("Chưa có User");
        }

        System.out.println("Mời ban nhập ID của User");
        int id = getInteger();
        boolean flag = true;
        while (true) {
            for (User user : sortUsers
            ) {
                if ((int) user.getId() == id) {
                    flag = flag;
                }
            }
            if (flag) {
                printlnError("ID user không tồn tại, mời nhập lại");
            } else {
                break;
            }
        }
        List<Order> orders = new ArrayList<>();
        for (Order order : oderDesign.findAll()) {
            if (Objects.equals(order.getIdUser(), userDesign.findById(id))) {
                orders.add(order);
            }
        }
        if (orders.isEmpty()) {
            printlnError("KHÔNG CÓ LỊCH SỬ ĐẶT HÀNG");
        } else {
            print(GREEN);
            printlnMess("________________________ LỊCH SỬ ĐẶT HÀNG________________________");
            System.out.println("|-------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |    NAME   |      PHONE    |    ĐỊA CHỈ   |     STATUS      " + "|");
            System.out.println("|-------------------------------------------------------------------|");
            for (Order order : orders) {
                System.out.printf("|%-5d | %-9s |  %-12s | %-12s | %-15s |%n",
                        order.getId(), order.getReceiver(), formattedPhoneNumber(order.getNumberPhone()), order.getAddress(), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ HOÀN THÀNH"))));

            }
            System.out.println("|-------------------------------------------------------------------|");
            printFinish();
        }
    }

    private void orderDetailAdmin() {
        System.out.print("Nhập ID đơn hàng: ");
        int orderId = getInteger();
        Order order = oderDesign.findById(orderId);
        if (order != null) {
            System.out.println("______________________ CHI TIẾT ĐƠN HÀNG ______________________");
            System.out.println("Người nhận       : " + order.getReceiver());
            System.out.println("Số điện thoại    : " + formattedPhoneNumber(order.getNumberPhone()));
            System.out.println("Địa chỉ          : " + order.getAddress());
            System.out.println("Thời gian        : " + order.getBuyDate());

            print(GREEN);
            System.out.println("______________________ THÔNG TIN ĐƠN HÀNG _______________________");
            System.out.println("|-------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |        PRODUCT    |    QUANTITY  |       PRIME    |      TOTAL    |     STATUS    " + " |");
            System.out.println("|-------------------------------------------------------------------------------------------|");

            for (Cart ca : order.getOrderDetail()) {
                System.out.printf("|%-5d | %-17s | %-12d | %-15s|%-15s| %-15s | %n",
                        ca.getCartId(), ca.getProduct().getProductName(), ca.getQuantity(), formatCurrency(ca.getProduct().getPrice()), formatCurrency(ca.getQuantity() * ca.getProduct().getPrice()), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ GIAO"))));
            }
            System.out.println("|--------------------------------------------------------------------------------------------|");
            printFinish();
        } else {
            System.err.println("Không tìm thấy đơn hàng.");
        }

    }

    private void orderStatus(byte orderStatus) {
        List<Order> oderList = oderDesign.findAll();
        List<Order> orders = new ArrayList<>();
        for (Order order : oderList) {
            if (order.getStatus() == orderStatus) {
                orders.add(order);
            }
        }
        if (orders.isEmpty()) {
            printlnError("Danh sách đơn hàng đã xác nhận rỗng");
        } else {
            print(GREEN);
            System.out.println("\n                                                 ĐƠN HÀNG ");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |   USERNAME   |   RECEIVER    |      TOTAL      |     PHONE    |    ĐỊA CHỈ   |    STATUS       " + "|");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");

            for (Order order : orders
            ) {
                System.out.printf("|%-5d | %-12s |  %-12s | %-15s | %-12s | %-12s | %-15s |%n",
                        order.getId(), userDesign.findById(order.getIdUser()).getUserName(), order.getReceiver(), formatCurrency(order.getTotal()), formattedPhoneNumber(order.getNumberPhone()), order.getAddress(), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ GIAO"))));
            }
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            printFinish();
        }


    }


    private void orderConfirmation() {
        List<Order> oderList = oderDesign.findAll();
        List<Order> orders = new ArrayList<>();
        for (Order order : oderList) {
            if (order.getStatus() == WAITING) {
                orders.add(order);
            }
        }
        if (orders.isEmpty()) {
            System.err.println("Đơn hàng chờ xác nhận rỗng");
        } else {
            print(GREEN);
            System.out.println("\n                                                 ĐƠN HÀNG                ");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |   USERNAME   |   RECEIVER    |      TOTAL      |     PHONE    |    ĐỊA CHỈ   |    STATUS       " + "|");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");

            for (Order order : orders
            ) {
                System.out.printf("|%-5d | %-12s |  %-12s | %-15s | %-12s | %-12s | %-15s |%n",
                        order.getId(), userDesign.findById(order.getIdUser()).getUserName(), order.getReceiver(), formatCurrency(order.getTotal()), formattedPhoneNumber(order.getNumberPhone()), order.getAddress(), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ GIAO"))));
            }
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            printFinish();

            System.out.println("Nhập vào id order cần xác nhận: ");
            int id = getInteger();
            Order order = oderDesign.findById(id);

            if (order != null) {
                if (order.getStatus() == WAITING) {
                    System.out.println("Xác nhận:     1.Chấp nhận      . 2.Hủy :");
                    int x = getInteger();

                    order.setStatus((byte) x);
//
                    if ((byte) x == 2) {
                        Order order1 = oderDesign.findById(id);
                        for (Cart ca : order1.getOrderDetail()) {
                            Product product = productDesign.findById((int) ca.getProduct().getId());
                            product.setStock(product.getStock() + ca.getQuantity());
                            productDesign.save(product);
                        }
                    }
//

                    // Đặt thời gian xác nhận đơn hàng
                    order.setConfirmTime(LocalDateTime.now());
                    oderDesign.save(order);
                    printlnMess("Xác thực đơn hàng thành công");

                } else if (order.getStatus() == CANCEL) {
                    printlnError("Đơn hàng đã bị hủy không thể xác thực !!.");
                } else {
                    printlnError("Đơn hàng đã được xác nhận, không thể thay đổi trạng thái.");
                }
            } else {
                printlnError("Không tìm thấy đơn hàng.");
            }
        }
    }


    private void showAllOrder() {
        List<Order> orders = oderDesign.findAll();
        if (!orders.isEmpty()) {
            print(GREEN);
            System.out.println("\n                                                ĐƠN HÀNG                ");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |   USERNAME   |   RECEIVER    |      TOTAL      |     PHONE    |    ĐỊA CHỈ   |    STATUS       " + "|");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            for (Order order : oderDesign.findAll()) {
                // Kiểm tra và chuyển trạng thái đơn hàng
                if (order.getConfirmTime() != null) {
                    if (ChronoUnit.MINUTES.between(order.getConfirmTime(), LocalDateTime.now()) >= 5) {
                        order.setStatus((byte) 3);
                        oderDesign.save(order);
                    }
                }

                if (order.getConfirmTime() != null) {
                    if (ChronoUnit.MINUTES.between(order.getConfirmTime(), LocalDateTime.now()) >= 10) {

                        order.setStatus((byte) 4);
                        oderDesign.save(order);
                    }
                }
                System.out.printf("|%-5d | %-12s |  %-12s | %-15s | %-12s | %-12s | %-15s |%n",
                        order.getId(), userDesign.findById(order.getIdUser()).getUserName(), order.getReceiver(), formatCurrency(order.getTotal()), formattedPhoneNumber(order.getNumberPhone()), order.getAddress(), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ GIAO"))));

            }
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            printFinish();

        } else {
            System.err.println("Đơn hàng rỗng");
        }
    }
}

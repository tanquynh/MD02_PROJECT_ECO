package ra.presentation.view.user;

import ra.business.config.Validate;
import ra.business.model.Cart;
import ra.business.model.Order;
import ra.business.model.Product;
import ra.business.model.User;
import ra.business.design.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ra.business.config.ConsoleColor.*;
import static ra.business.config.InputMethods.*;
import static ra.business.constant.Until.formatCurrency;


public class CartView {


    private UserDesign userDesign;
    private CartDesign cartDesign;
    private ProductDesign productDesign;
    private OrderDesign orderDesign;


    public CartView() {
        this.userDesign = new UserDesign();
        this.cartDesign = new CartDesign();
        this.productDesign = new ProductDesign();
        this.orderDesign = new OrderDesign();

    }


    public void displayMenuCart() {
        int selectCart;
        while (true) {


            print(YELLOW);
            System.out.println(".-------------------------------------------------------------.");
            System.out.println("|                            MENU-GIỎ HÀNG                    |");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|                     1. DANH SÁCH SẢN PHẨM TRONG GIỎ HÀNG    |");
            System.out.println("|                     2. CHỈNH SỬA SỐ LƯỢNG SẢN PHẨM          |");
            System.out.println("|                     3. XÓA 1 SẢN PHẨM TRONG GIỎ HÀNG        |");
            System.out.println("|                     4. XÓA TOÀN BỘ SẢN PHẨM TRONG GIỎ HÀNG  |");
            System.out.println("|                     5. THANH TOÁN                           |");
            System.out.println("|                     6. LỊCH SỬ MUA HÀNG                     |");
            System.out.println("|                     7. QUAY LẠI MENU TRƯỚC                  |");
            System.out.println("|                     0. ĐĂNG XUẤT                            |");
            System.out.println("'-------------------------------------------------------------'");
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡 : ");
            printFinish();
            System.out.println("Nhap lua chon cua ban : ");
            selectCart = getInteger();
            switch (selectCart) {
                case 1:

                    displayAllCart();
                    break;
                case 2:
                    updateQuantity();
                    break;
                case 3:
                    deleteProductInCart();
                    break;
                case 4:
                    deleteAllProductInCart();
                    break;
                case 5:
                    endPay();
                    break;
                case 6:
                    new OrderView().OrderMenuHistory();
                    break;
                case 7:
                    return;
                case 0:
                    userDesign.logout();
                    break;
                default:
                    System.err.println("--->> Lua chon khong phu hop. Vui long chon lai ❤ ");
                    break;
            }
        }
    }

    private void displayAllCart() {
        List<Cart> carts = cartDesign.userLogin().getCart();

        if (carts.isEmpty()) {
            printlnError("Giỏ hàng rỗng !!.");
            return;
        }
        print(GREEN);
        System.out.println("                             DANH SÁCH GIỎ HÀNG        ");
        System.out.println("|--------------------------------------------------------------------------|");
        System.out.println("|" + "  ID  |        PRODUCT    |    QUANTITY  |       PRIME    |     TOTAL    " + " |");
        System.out.println("|--------------------------------------------------------------------------|");

        for (Cart ca : carts) {
            System.out.printf("|%-5d | %-17s | %-12s | %-15s|%-15s| %n",
                    ca.getCartId(), ca.getProduct().getProductName(), ca.getQuantity(), formatCurrency(ca.getProduct().getPrice()), formatCurrency(ca.getQuantity() * ca.getProduct().getPrice()));
        }
        System.out.println("|--------------------------------------------------------------------------|");
        printFinish();
    }


    private void updateQuantity() {
        System.out.println("Nhập vào ID: ");
        int idCart = getInteger();
        User user = cartDesign.userLogin();
        List<Cart> carts = user.getCart();
        Cart cart = cartDesign.findById(idCart);
        if (cart == null) {
            printlnError("Sản phẩm không tồn tại trong giỏ hàng!!");
            return;
        }

        System.out.println("Nhập vào số lượng muốn cập nhật mới: ");
        int updateQuantity = getInteger();
        if (updateQuantity <= 0) {
            printlnError("Số lượng cập nhật phải lớn hơn 0");
        } else if (updateQuantity > cart.getProduct().getStock()) {
            printlnError("Số lượng sản phẩm vượt quá tồn kho.");
        } else {
            cart.setQuantity(updateQuantity);
            int index = cartDesign.findIndex1(idCart);
            carts.set(index, cart);
            printlnSuccess("Cập nhật số lượng thành công 🎈🎈.");
            userDesign.save(user);
//            cartDesign.save(cart);
        }
    }

    private void deleteProductInCart() {
        System.out.println("Nhập vào ID");
        int idCart = getInteger();
        int index = cartDesign.findIndex(idCart);

        if (index == -1) {
            System.err.println("Sản phẩm không tồn tại trong giỏ hàng.");
            return;
        }
        cartDesign.deleteCart(index);
        System.out.println("Sản phẩm đã được xóa");
    }

    private void deleteAllProductInCart() {
        cartDesign.deleteAll();
        System.out.println("Đã xóa toàn bộ sản phẩm ");
    }

    private void endPay() {

        User user = cartDesign.userLogin();
        if (user.getCart().isEmpty()) {
            printlnMess("Chưa có đơn hàng cần thanh toán !!.");
            return;
        }
        Order newOrder = new Order();
        newOrder.setId(orderDesign.autoId());

        // Cập nhật tổng tiền
        double total = 0;
        for (Cart ca : user.getCart()) {
            total += ca.getProduct().getPrice() * ca.getQuantity();
        }
        newOrder.setTotal(total);
        newOrder.setIdUser((int) userDesign.userActive().getId());
        System.out.println("Nhập tên người nhận hàng: ");
        newOrder.setReceiver(getString());
        System.out.println("Nhập số điện thoại người nhận: ");
        while (true) {
            String phone = scanner().nextLine();
            if (Validate.isValidPhone(phone)) {
                newOrder.setNumberPhone(phone);
                break;
            }
            ;
        }

        System.out.println("Nhập vào địa chỉ người nhận:");
        newOrder.setAddress(getString());
        newOrder.setBuyDate(LocalDate.now());
//        newOrder.setStatus(WAITING);
        // Tiến hành trừ đi số lượng trong kho hàng
        boolean flag = true;
        for (Cart ca : user.getCart()) {
            Product pt = productDesign.findById((int) ca.getProduct().getId());
            if (pt.getStock() - ca.getQuantity() >= 0) {
                flag = false;
                pt.setStock(pt.getStock() - ca.getQuantity());
                productDesign.updateQuantity(pt);
            }

        }
        if (flag) {
            printlnError(
                    "Đớn hàng không được tạo thành công, do số lượng đặt hàng lớn hơn trong kho"
            );
        }

        printlnSuccess("Đặt hàng thành công🎈🎈.Vui lòng chờ xác nhận !!.");
        orderDesign.save(newOrder);
        Order order = new Order();
        userDesign.save(user);
        order.setOrderDetail(user.getCart());
        user.setCart(new ArrayList<>());
        userDesign.save(user);
    }


}

package ra.presentation.view.user;

import ra.business.config.InputMethods;
import ra.business.model.*;
import ra.business.design.ProductDesign;
import ra.business.design.UserDesign;
import ra.business.design.WishlistDesign;

import java.util.ArrayList;
import java.util.List;

import static ra.business.config.ConsoleColor.*;
import static ra.business.config.InputMethods.*;
import static ra.business.constant.Until.formatCurrency;

public class WishlistView {
    private UserDesign userDesign;
    private WishlistDesign wishlistDesign;
    private ProductDesign productDesign;

    public WishlistView() {
        this.wishlistDesign = new WishlistDesign();
        this.productDesign = new ProductDesign();
        this.userDesign = new UserDesign();
    }

    public void displayMenuWishlist() {
        int selectCart;
        while (true) {


            print(YELLOW);
            System.out.println(".-------------------------------------------------------------.");
            System.out.println("|                            MENU-WISHLIST                    |");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|                     1. DANH SÁCH SẢN PHẨM                   |");
            System.out.println("|                     2. QUAY LẠI MENU TRƯỚC                  |");
            System.out.println("|                     0. ĐĂNG XUẤT                            |");
            System.out.println("'-------------------------------------------------------------'");
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡 : ");
            printFinish();
            System.out.println("Nhap lua chon cua ban : ");
            selectCart = getInteger();
            switch (selectCart) {
                case 1:
                    List<WishList> wishLists = wishlistDesign.findAll();
                    displayAllWishlist(wishLists);
                    break;
                case 2:
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

    private void displayAllWishlist(List<WishList> wishLists) {
        if (wishLists.isEmpty()) {
            System.err.println("Danh sách sản phẩm trống!!!");
        } else {
            print(GREEN);
            System.out.println("                                        DANH SÁCH PRODUCT            ");
            System.out.println("|-----------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STOCK  |      PRICE    |   CATEGORY  |  STATUS " + " |");
            System.out.println("|-----------------------------------------------------------------------------------------------------|");

            for (WishList wishList : wishLists) {
                System.out.printf("|%-5d | %-17s | %-20s | %-8s| %-14s| %-12s| %-9s |%n",
                        wishList.getId(), wishList.getProduct().getProductName(), wishList.getProduct().getProductDes(), wishList.getProduct().getStock(), formatCurrency(wishList.getProduct().getPrice()), wishList.getProduct().getCategory().getCategoryName(), wishList.getProduct().isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG");
            }
            System.out.println("|-----------------------------------------------------------------------------------------------------|");
            printFinish();
            System.out.println("+---------------------------------------------------------------------------------+");
            System.out.println("|" + "   1. Add To Cart           |   2. Delete          |   3. Delete  All        |   0. QUAY LẠI              " + "|");
            System.out.println("+---------------------------------------------------------------------------------+");

            System.out.println("Nhập lựa chọn: ");
            switch (getInteger()) {
                case 1:
                    List<Product> products = new ArrayList<>();
                    for (WishList wishList : wishLists) {
                        products.add(wishList.getProduct());
                    }
                    new UserView().addToCart(products);
                    break;
                case 2:
                    System.out.println("Nhập id sản phẩm muốn xóa khỏi danh sách Wishlist");
                    int idDelete = InputMethods.getInteger();
                    Product product = productDesign.findById(idDelete);
                    if (product != null) {
                        wishLists.remove(idDelete);
                        for (WishList wishList : wishLists) {
                            wishlistDesign.save(wishList);
                        }
                        ;
                    } else {
                        System.err.println("ID nhập vào không đúng");
                    }
                    break;
                case 3:
                    userDesign.userActive().setWishList(new ArrayList<>());
                    userDesign.save(userDesign.userActive());
                    break;
                case 0:
                    return;
                default:
                    System.out.println(RED + "Không có chức năng phù hợp, vui lòng chọn lại!!!" + RESET);
                    break;
            }
        }
    }
}

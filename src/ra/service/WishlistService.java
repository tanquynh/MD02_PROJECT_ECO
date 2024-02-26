package ra.service;

import ra.model.Cart;
import ra.model.User;
import ra.model.WishList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import static ra.config.ConsoleColor.printlnError;

public class WishlistService implements IShop<WishList>{
    private UserService userService;
    public WishlistService() {
        this.userService = new UserService();
    }
    public User userLogin() {
        User userLogin;
        userLogin = userService.userActive();
        return userLogin;
    }
    @Override
    public List<WishList> findAll() {
        return userLogin().getWishList();
    }
       @Override
    public void save(WishList wishlist) {
        User user = userLogin();
        WishList existingWishList = findByProduct(wishlist);
        if (existingWishList != null) {
            // Nếu sản phẩm đã tồn tại trong mục yêu thic
            System.out.println("Bạn đã thêm vào muc yêu thích");

        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới2
            save(wishlist);
        }

        userService.save(user);

    }
    public WishList findByProduct(WishList wishList) {
        for (WishList ci : userLogin().getWishList()) {
            if (ci.getProduct().equals(wishList.getProduct())){
                return ci;
            }
        }
        return null;
    }

    @Override
    public WishList findById(int id) {
        for (WishList ci : userLogin().getWishList()) {
            if (ci != null && ci.getWishListId() == (id)) {
                return ci;
            }
        }
        return null;
    }

    @Override
    public int autoId() {
        int max = 0;
        for (WishList wishList : userLogin().getWishList()) {
            if (wishList.getWishListId() > max) {
                max = wishList.getId();
            }
        }
        return max + 1;
    }
}

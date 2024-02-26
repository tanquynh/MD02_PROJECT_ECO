package ra.model;

import java.io.Serializable;

public class WishList extends Entity<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int wishListId;
    private int userId;
    private Product product;

    public WishList() {
    }


    public WishList(int wishListId, int userId, Product product) {
        this.wishListId = wishListId;
        this.userId = userId;
        this.product = product;
    }

    public int getWishListId() {
        return wishListId;
    }

    public void setWishListId(int wishListId) {
        this.wishListId = wishListId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

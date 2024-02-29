package ra.business.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Product extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String productName;
    private String sku;
    private double price;
    private int stock;
    private String productDes;
    private Category category;
    boolean productStatus = true;
    private LocalDate createAt;
    private LocalDate updateAt;
    private  Integer countLikes=0;
    private boolean like;

    public Product() {
    }

    public Product(String productName, String sku, double price, int stock, String productDes, Category category, boolean productStatus, LocalDate createAt, LocalDate updateAt, Integer countLikes, boolean like) {
        this.productName = productName;
        this.sku = sku;
        this.price = price;
        this.stock = stock;
        this.productDes = productDes;
        this.category = category;
        this.productStatus = productStatus;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.countLikes = countLikes;
        this.like = like;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getProductDes() {
        return productDes;
    }

    public void setProductDes(String productDes) {
        this.productDes = productDes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isProductStatus() {
        return productStatus;
    }

    public void setProductStatus(boolean productStatus) {
        this.productStatus = productStatus;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public void setQuantity(int stock) {
        this.stock = stock;
    }

    public Integer getCountLikes() {
        return countLikes;
    }

    public void setCountLikes(Integer countLikes) {
        this.countLikes = countLikes;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}

package ra.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private boolean status = true;
    private boolean importance = true;
    private LocalDate createAt;
    private LocalDate updateAt;
    private String address;
    private int role;
    private List<Cart> cart = new ArrayList<>();
    private List<WishList> wishList = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public User() {
    }

    public User(String userName, String fullName, String password, String email, String phone, boolean status, boolean importance, LocalDate createAt, LocalDate updateAt, String address, int role, List<Cart> cart, List<WishList> wishList, List<Message> messages) {
        this.userName = userName;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.importance = importance;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.address = address;
        this.role = role;
        this.cart = cart;
        this.wishList = wishList;
        this.messages = messages;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isImportance() {
        return importance;
    }

    public void setImportance(boolean importance) {
        this.importance = importance;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    public List<WishList> getWishList() {
        return wishList;
    }

    public void setWishList(List<WishList> wishList) {
        this.wishList = wishList;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}

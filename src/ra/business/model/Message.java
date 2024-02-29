package ra.business.model;

import java.io.Serializable;

public class Message extends Entity<Integer> implements Serializable {
    private int userId;
    private String message;
    private int adminId = 0;

    public Message() {
    }

    public Message(int userId, String message, int adminId) {
        this.userId = userId;
        this.message = message;
        this.adminId = adminId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}

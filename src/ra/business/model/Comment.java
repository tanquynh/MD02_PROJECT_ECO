package ra.business.model;

import java.io.Serializable;

public class Comment extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer userId;
    private Integer productId;
    private String comment;

    public Comment() {
    }

    public Comment(Integer userId, Integer productId, String comment) {
        this.userId = userId;
        this.productId = productId;
        this.comment = comment;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

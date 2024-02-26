package ra.model;

import java.io.Serializable;

public class Like extends Entity<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer userId;
    private Integer productId;
    private boolean likeStatus;

    public Like() {
    }

    public Like(Integer userId, Integer productId, boolean likeStatus) {
        this.userId = userId;
        this.productId = productId;
        this.likeStatus = likeStatus;
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

    public boolean isLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }
}

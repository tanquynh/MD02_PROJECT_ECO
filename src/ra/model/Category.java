package ra.model;

import java.io.Serializable;

public class Category extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String categoryName;
    private String categoryDes;
    private boolean categoryStatus = true;

    public Category() {

    }

    public Category(String categoryName, String categoryDes, boolean categoryStatus) {
        this.categoryName = categoryName;
        this.categoryDes = categoryDes;
        this.categoryStatus = categoryStatus;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDes() {
        return categoryDes;
    }

    public void setCategoryDes(String categoryDes) {
        this.categoryDes = categoryDes;
    }

    public boolean isCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(boolean categoryStatus) {
        this.categoryStatus = categoryStatus;
    }


}

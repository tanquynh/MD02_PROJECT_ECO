package ra.presentation.view.user;

import ra.business.model.Like;
import ra.business.model.Product;
import ra.business.design.LikeDesign;
import ra.business.design.ProductDesign;
import ra.business.design.UserDesign;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LikeView {
    private UserDesign userDesign;
    private LikeDesign likeDesign;

    public LikeView() {
        this.likeDesign = new LikeDesign();
        this.userDesign = new UserDesign();
    }

    public void handleLikeProduct(Product product) {
        List<Like> productLikeList = getProductLikeList(product);
        Like like = findLikeById(productLikeList);
        if (productLikeList.isEmpty() || like == null) {
            Like newLike = new Like();
            newLike.setId(likeDesign.autoId());
            newLike.setProductId((Integer) product.getId());
            newLike.setUserId((Integer) userDesign.userActive().getId());
            newLike.setLikeStatus(true);
            likeDesign.save(newLike);
            System.out.println("Bạn đã like sản phẩm!");
            return;
        }
        like.setLikeStatus(!like.isLikeStatus());
        if (like.isLikeStatus()) {
            System.out.println("Bạn đã like sản phẩm!");
        } else {
            System.out.println("Bạn đã unlike sản phẩm!");
        }
        likeDesign.save(like);
    }

    public List<Like> getProductLikeList(Product product) {
        List<Like> productLikeList;
        if (likeDesign.findAll().isEmpty() || likeDesign.findAll() == null) {
            productLikeList = new ArrayList<>();
        } else {
            productLikeList = likeDesign.findAll().stream().filter(l -> l.getProductId().equals(product.getId())).filter(Like::isLikeStatus).collect(Collectors.toList());
        }
        return productLikeList;
    }

    public Like findLikeById(List<Like> productLikeList) {
        for (Like like : productLikeList) {
            if (like.getUserId().equals(userDesign.userActive().getId()) && like.isLikeStatus()) {
                return like;
            }
        }
        return null;
    }

    public int countLikeProduct(Product product) {
        List<Like> productLikeList = getProductLikeList(product);

        if (likeDesign.findAll().isEmpty()) {
            System.out.println("Sản phẩm " + product.getProductName() + " chưa có likes nào!");
            System.out.println("Bạn chưa like sản phẩm!");
            return 0;
        }

        System.out.println("Sản phẩm " + product.getProductName() + " có " + productLikeList.size() + " likes ");

        if (findLikeById(productLikeList) != null) {
            System.out.println("Bạn đã like sản phẩm!");
            return productLikeList.size();
        } else {
            System.out.println("Bạn chưa like sản phẩm!");
            return 0;
        }
    }
}

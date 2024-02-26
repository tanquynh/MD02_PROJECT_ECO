package ra.view.user;

import ra.config.InputMethods;
import ra.model.Like;
import ra.model.Product;
import ra.service.LikeService;
import ra.service.ProductService;
import ra.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LikeView {
    private UserService userService;
    private LikeService likeService;
    private ProductService productService;

    public LikeView() {
        this.likeService = new LikeService();
        this.productService = new ProductService();
        this.userService = new UserService();
    }

    public void handleLikeProduct(Product product) {
        List<Like> productLikeList = getProductLikeList(product);
        Like like = findLikeById(productLikeList);
        if (productLikeList.isEmpty() || like == null) {
            Like newLike = new Like();
            newLike.setId(likeService.autoId());
            newLike.setProductId((Integer) product.getId());
            newLike.setUserId((Integer) userService.userActive().getId());
            newLike.setLikeStatus(true);
            likeService.save(newLike);
            System.out.println("Bạn đã like sản phẩm!");
            return;
        }
        like.setLikeStatus(!like.isLikeStatus());
        if (like.isLikeStatus()) {
            System.out.println("Bạn đã like sản phẩm!");
        } else {
            System.out.println("Bạn đã unlike sản phẩm!");
        }
        likeService.save(like);
    }

    public List<Like> getProductLikeList(Product product) {
        List<Like> productLikeList;
        if (likeService.findAll().isEmpty() || likeService.findAll() == null) {
            productLikeList = new ArrayList<>();
        } else {
            productLikeList = likeService.findAll().stream().filter(l -> l.getProductId().equals(product.getId())).filter(Like::isLikeStatus).collect(Collectors.toList());
        }
        return productLikeList;
    }

    public Like findLikeById(List<Like> productLikeList) {
        for (Like like : productLikeList) {
            if (like.getUserId().equals(userService.userActive().getId()) && like.isLikeStatus()) {
                return like;
            }
        }
        return null;
    }

    public int countLikeProduct(Product product) {
        List<Like> productLikeList = getProductLikeList(product);

        if (likeService.findAll().isEmpty()) {
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

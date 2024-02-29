package ra.presentation.view.user;

import ra.business.config.InputMethods;
import ra.business.model.Comment;
import ra.business.model.Product;
import ra.business.design.CommentDesign;
import ra.business.design.UserDesign;

import java.util.List;
import java.util.stream.Collectors;

public class CommentView {
    private CommentDesign commentDesign;
    private UserDesign userDesign;

    public CommentView() {
        this.commentDesign = new CommentDesign();
        this.userDesign = new UserDesign();
    }

    public int showComment(Product product) {
        List<Comment> productCommentList = commentDesign.findAll().stream().filter(c -> c.getProductId().equals(product.getId())).collect(Collectors.toList());
        if (productCommentList.isEmpty()) {
            System.out.println("Sản phẩm " + product.getProductName() + " chưa có bình luận nào!");
            return 0;
        }
        System.out.println("Sản phẩm " + product.getProductName() + "  có " + productCommentList.size() + " bình luận!");

        for (Comment comment : productCommentList) {
            if (product.getId().equals(comment.getProductId())) {

                System.out.println((userDesign.findById(comment.getUserId()).getFullName()) + " đã bình luận: " + comment.getComment());
            }
        };
        return productCommentList.size();
    }

    public void addNewComment(Product product) {
        System.out.println("Thêm bình luận cho sản phẩm: ");
        Comment newComment = new Comment();
        newComment.setId(commentDesign.autoId());
        newComment.setUserId((Integer) userDesign.userActive().getId());
        newComment.setProductId((Integer) product.getId());
        newComment.setComment(InputMethods.getString());

        commentDesign.save(newComment);
        System.out.println("Bạn đã bình luận cho sản phẩm!");
    }
}

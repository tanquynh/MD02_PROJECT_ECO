package ra.view.user;

import ra.config.InputMethods;
import ra.model.Comment;
import ra.model.Product;
import ra.service.CommentService;
import ra.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

public class CommentView {
    private CommentService commentService;
    private UserService userService;

    public CommentView() {
        this.commentService = new CommentService();
        this.userService = new UserService();
    }

    public int showComment(Product product) {
        List<Comment> productCommentList = commentService.findAll().stream().filter(c -> c.getProductId().equals(product.getId())).collect(Collectors.toList());
        if (productCommentList.isEmpty()) {
            System.out.println("Sản phẩm " + product.getProductName() + " chưa có bình luận nào!");
            return 0;
        }
        System.out.println("Sản phẩm " + product.getProductName() + "  có " + productCommentList.size() + " bình luận!");

        for (Comment comment : productCommentList) {
            if (product.getId().equals(comment.getProductId())) {

                System.out.println((userService.findById(comment.getUserId()).getFullName()) + " đã bình luận: " + comment.getComment());
            }
        };
        return productCommentList.size();
    }

    public void addNewComment(Product product) {
        System.out.println("Thêm bình luận cho sản phẩm: ");
        Comment newComment = new Comment();
        newComment.setId(commentService.autoId());
        newComment.setUserId((Integer) userService.userActive().getId());
        newComment.setProductId((Integer) product.getId());
        newComment.setComment(InputMethods.getString());

        commentService.save(newComment);
        System.out.println("Bạn đã bình luận cho sản phẩm!");
    }
}

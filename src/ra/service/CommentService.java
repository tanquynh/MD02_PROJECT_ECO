package ra.service;

import ra.constant.Constant;
import ra.model.Comment;
import ra.model.Product;
import ra.repository.FileRepo;

import java.util.List;

public class CommentService implements IShop<Comment> {
    FileRepo<Comment, Integer> commentRepo;

    public CommentService() {
        this.commentRepo = new FileRepo<>(Constant.FilePath.COMMENT_FILE);
    }


    @Override
    public List<Comment> findAll() {
        return commentRepo.findAll();
    }

    @Override
    public void save(Comment comment) {
        commentRepo.save(comment);
    }

    @Override
    public Comment findById(int id) {
        return commentRepo.findById(id);
    }

    @Override
    public int autoId() {
        return commentRepo.autoId();
    }
}

package ra.business.design;

import ra.business.constant.Constant;
import ra.business.model.Comment;
import ra.business.repository.FileRepo;

import java.util.List;

public class CommentDesign implements IShop<Comment> {
    FileRepo<Comment, Integer> commentRepo;

    public CommentDesign() {
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

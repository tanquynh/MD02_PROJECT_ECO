package ra.business.design;

import ra.business.constant.Constant;
import ra.business.model.Like;
import ra.business.repository.FileRepo;

import java.util.List;

public class LikeDesign implements IShop<Like> {
    FileRepo<Like, Integer> likeRepo;

    public LikeDesign() {
        this.likeRepo = new FileRepo<>(Constant.FilePath.LIKE_FILE);
    }
    @Override
    public List<Like> findAll() {
        return likeRepo.findAll();
    }

    @Override
    public void save(Like like) {
        likeRepo.save(like);
    }

    @Override
    public Like findById(int id) {
        return likeRepo.findById(id);
    }

    @Override
    public int autoId() {
        return likeRepo.autoId();
    }
}

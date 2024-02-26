package ra.service;

import ra.constant.Constant;
import ra.model.Category;
import ra.model.Like;
import ra.repository.FileRepo;

import java.util.List;

public class LikeService implements IShop<Like> {
    FileRepo<Like, Integer> likeRepo;

    public LikeService() {
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

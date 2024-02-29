package ra.business.design;

import ra.business.constant.Constant;
import ra.business.model.Category;
import ra.business.repository.FileRepo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDesign implements IShop<Category> {
    FileRepo<Category, Integer> categoryRepo;

    public CategoryDesign() {
        this.categoryRepo = new FileRepo<>(Constant.FilePath.CATEGORY_FILE);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    @Override
    public void save(Category category) {
        categoryRepo.save(category);
    }

    @Override
    public Category findById(int id) {
        return categoryRepo.findById(id);
    }

    @Override
    public int autoId() {
        return categoryRepo.autoId();
    }

    public List<Category> sortByName() {
//        List<Category> categoryList = findAll();
//        categoryList.sort((c1, c2) -> c1.getCategoryName().compareTo(c2.getCategoryName()));
//        return  categoryList;
        return findAll().stream().sorted(Comparator.comparing(Category::getCategoryName)).collect(Collectors.toList());

    }
}

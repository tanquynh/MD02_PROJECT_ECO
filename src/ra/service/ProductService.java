package ra.service;

import ra.constant.Constant;
import ra.model.Product;
import ra.repository.FileRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import static ra.config.InputMethods.scanner;

public class ProductService implements IShop<Product> {
    FileRepo<Product, Integer> productRepo;

    public ProductService() {
        this.productRepo = new FileRepo<>(Constant.FilePath.PRODUCT_FILE);
    }

    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Override
    public void save(Product product) {
        productRepo.save(product);
    }

    @Override
    public Product findById(int id) {
        return productRepo.findById(id);
    }

    @Override
    public int autoId() {
        return productRepo.autoId();
    }

    public List<Product> getSearchProduct() {
        List<Product> products = findAll();
        List<Product> findProduct = new ArrayList<>();
        System.out.println("Mời nhập tên sản phẩm cần tìm kiếm!!");
        String searchName = scanner().nextLine();
        if (products.isEmpty()) {
            return new ArrayList<>();
        } else {
            for (Product product : products) {
                if (product.getProductName().toLowerCase().trim().contains(searchName.trim().toLowerCase())) {
                    findProduct.add(product);
                }
            }
            return findProduct;
        }
    }

    public void updateQuantity(Product product) {
        List<Product> allMenu = findAll();
        // Tìm sản phẩm trong danh sách
        for (Product existingProduct : allMenu) {
            if (Objects.equals(existingProduct.getId(), product.getId())) {
                // Cập nhật số lượng
                existingProduct.setStock(product.getStock());
                save(product);
                break;
            }
        }

    }


    public List<Product> getSortPriceProducts() {
        List<Product> sortProducts = findAll();
        sortProducts.sort((o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice()));
        return sortProducts;

    }
    public List<Product> getSortNameProducts() {
        List<Product> sortProducts = findAll();
        sortProducts.sort((o1,o2)->o1.getProductName().compareTo(o2.getProductName()));
        return sortProducts;

    }
}

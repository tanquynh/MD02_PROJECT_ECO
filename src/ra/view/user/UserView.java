package ra.view.user;

import ra.config.InputMethods;
import ra.model.*;
import ra.service.*;
import ra.view.admin.MyAccount;

import java.util.*;
import java.util.stream.Collectors;

import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.getInteger;
import static ra.constant.Constant.ProductStatus.HIDE;
import static ra.constant.Constant.Status.INACTIVE;
import static ra.constant.Until.formatCurrency;


public class UserView {
    private UserService userService;
    private ProductService productService;
    private CategoryService categoryService;
    private CartService cartService;
    private OrderService orderService;
    private WishlistService wishlistService;

    public UserView() {
        this.cartService = new CartService();
        this.categoryService = new CategoryService();
        this.productService = new ProductService();
        this.userService = new UserService();
        this.orderService = new OrderService();
        this.wishlistService = new WishlistService();
    }

    public void displayUserMenu() {

        int choice;

        do {
            print(BLUE);

            System.out.println(".-------------------------------------------------------------.");
//            System.out.println("| WELCOME USER : " + userService.userActive().getUserName() + "                                        |");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|                 1. TÌM KIẾM SẢN PHẨM THEO TÊN               |");
            System.out.println("|                 2. HIỂN THỊ SẢN PHẨM THEO DANH MỤC          |");
            System.out.println("|                 3. DANH SÁCH SẢN PHẨM                       |");
            System.out.println("|                 4. HIỂN THỊ SẢN PHẨM NỔI BẬT                |");
            System.out.println("|                 5. SẮP XẾP SẢN PHẨM THEO GIÁ                |");
            System.out.println("|                 6. THÊM VÀO GIỎ HÀNG                        |");
            System.out.println("|                 7. THÊM VÀO DANH SÁCH YÊU THÍCH             |");
            System.out.println("|                 8. GIỎ HÀNG                                 |");
            System.out.println("|                 9. MY PROFILE                               |");
            System.out.println("|                 10. NHẮN TIN                                |");
            System.out.println("|                 0. ĐĂNG XUẤT                                |");
            System.out.println("'-------------------------------------------------------------'\n");
            printFinish();
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡: ");
            choice = getInteger();

            switch (choice) {
                case 1:
                    searchProduct();
                    break;
                case 2:
                    showProductByCategory();
                    break;
                case 3:
                    List<Product> productList = productService.findAll();
                    displayProductList(productList);
                    break;
                case 4:
                    showOutstandingProducts();
                    break;
                case 5:
                    sortProduct();
                    break;
                case 6:
                    List<Product> products = productService.findAll();
                    addToCart(products);
                    break;
                case 7:
                    addToWishList();
                    break;
                case 8:
                    new CartView().displayMenuCart();
                    break;
                case 9:
                    new MyAccount().MyAccount();
                    break;
                case 10:
                    new MessageView().menuMessage();
                    break;
                case 0:
                    userService.logout();
                default:
                    break;
            }
        } while (choice != 5);
    }

    private void showOutstandingProducts() {
        System.out.println("Lựa chọn hiên thị sản phẩm theo giá: ");
        System.out.println("+---------------------------------------------------------------------------------+");
//        System.out.println("|" + "   1. MỚI NHẤT          |    2. MUA NHIỀU        |    3. YÊU THÍCH        |    0. QUAY LẠI         " + "|");
        System.out.println("|" + "   1. MỚI NHẤT          |    2. MUA NHIỀU        |                 |    0. QUAY LẠI         " + "|");
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println("Nhập lựa chọn: ");
        switch (InputMethods.getInteger()) {
            case 1:
                List<Product> latestProducts = productService.findAll()
                        .stream()
                        .filter(product -> product.getCreateAt() != null)
                        .sorted(Comparator.comparing(Product::getCreateAt).reversed())
                        .limit(10)
                        .collect(Collectors.toList());
                displayProductList(latestProducts);
                break;
            case 2:


                // Lấy danh sách 10 sản phẩm có số lượng mua nhiều nhất từ danh sách đơn hàng
                List<Order> orderList = orderService.findAll();

                // Tạo một Map để lưu trữ thông tin về số lượng mua của từng sản phẩm
                Map<Product, Integer> productQuantityMap = new HashMap<>();

                // Lặp qua danh sách đơn hàng để tính toán số lượng mua của từng sản phẩm
                for (Order order : orderList) {
                    for (Cart cart : userService.findById(order.getIdUser()).getCart()) {
                        Product product = cart.getProduct();
                        productQuantityMap.put(product, productQuantityMap.getOrDefault(product, 0) + cart.getQuantity());
                    }
                }

                // Sắp xếp sản phẩm theo số lượng mua giảm dần và giới hạn số lượng sản phẩm là 10
                List<Product> products = productQuantityMap.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(10)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                // Hiển thị danh sách sản phẩm
                displayProductList(products);

                break;
            case 3:

                break;
            case 0:
                return;
            default:
                System.out.println(RED + "Không có chức năng phù hợp, vui lòng chọn lại!!!" + RESET);
                break;
        }
    }


    public List<Product> showProductByCategory() {
        List<Product> products = productService.findAll();
        List<Category> categories = categoryService.findAll();
        List<Product> findProducts = new ArrayList<>();

        if (categories.isEmpty()) {
            System.err.println("Danh sách Category rỗng");

        } else {
            System.out.print("\033[0;32m"); // Đặt màu chữ thành xanh
            System.out.println("\n                       DANH SÁCH CATEGORY               ");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STATUS " + " |");
            System.out.println("|-------------------------------------------------------------|");

            for (Category catalog : categories) {
                if (catalog.isCategoryStatus() != HIDE) {
                    System.out.printf("|%-5d | %-17s | %-20s | %-9s |%n",
                            catalog.getId(), catalog.getCategoryName(), catalog.getCategoryDes(), (catalog.isCategoryStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
                }
            }

            System.out.println("|-------------------------------------------------------------|");
            System.out.print("\033[0m"); // Đặt lại màu chữ thành mặc định
            printFinish();
        }
        boolean categoryFound = false;
        int searchId = -1;

        while (!categoryFound) {
            System.out.println("Mời bạn nhập id category:");
            searchId = getInteger();

            for (Category category : categories) {
                if (category.getId().equals(searchId)) {
                    categoryFound = true;
                    break;
                }
            }

            if (!categoryFound) {
                System.err.println("ID không hợp lệ, mời nhập lại.");
            }
        }
        for (Product product : products) {
            if (product.getCategory().getId().equals(searchId)) {
                if (product.isProductStatus() != HIDE) {
                    findProducts.add(product);
                }

            }
        }

        if (findProducts.isEmpty()) {
            System.err.println("Không tìm thấy sản phẩm trong danh mục này.");
        } else {
            displayProductList(findProducts);
        }

        return findProducts;
    }

    public void addToCart(List<Product> products) {
        if (products.isEmpty()) {
            printlnError("Chưa có sản phẩm");

        }
        // Hiển thị danh sách sản phẩm

        print(GREEN);
        System.out.println("\n                                           DANH SÁCH PRODUCT            ");
        System.out.println("|----------------------------------------------------------------------------------------|");
        System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |     PRICE   |  CATEGORY  |   STATUS " + " |");
        System.out.println("|----------------------------------------------------------------------------------------|");
        for (Product product : products) {
            if (product.getCategory().isCategoryStatus() != INACTIVE && product.isProductStatus() != HIDE) {
                System.out.printf("|%-5d | %-17s | %-20s | %-10s| %-11s|%-11s|%n",
                        product.getId(), product.getProductName(), product.getProductDes(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));

            }
        }
        System.out.println("|----------------------------------------------------------------------------------------|");
        printFinish();


        System.out.println("Nhập vào ID sản phẩm để thêm vào giỏ hàng");
        int productId;
        while (true) {
            productId = getInteger();
            Product product = productService.findById(productId);
            if (product == null || product.isProductStatus() == HIDE || product.getCategory().isCategoryStatus() == INACTIVE) {
                System.err.println("Không tìm thấy sản phẩm hoặc với ID " + productId);
            } else {
                // Tìm thấy sản phẩm và sản phẩm không bị ẩn, thoát khỏi vòng lặp
                break;
            }
        }


        // Tạo đối tượng Cart để lưu thông tin sản phẩm
        Cart cart = new Cart();
        cart.setProduct(productService.findById(productId));
        cart.setCartId(cartService.autoId());

        while (true) {
            System.out.println("Nhập vào số lượng muốn thêm vào giỏ hàng: ");
            int count = getInteger();
            if (count <= 0) {
                printlnError("Nhập số lượng sản phẩm lớn hơn 0");
            } else if (count > productService.findById(productId).getStock()) {
                printlnError("Số lượng này lớn hơn hàng chúng tôi có sẵn. Vui lòng giảm số lượng xuống.");
            } else {
                cart.setQuantity(count);
                break;
            }
        }

        // Lưu đối tượng Cart vào giỏ hàng
        cartService.save(cart);
        printlnSuccess("Thêm vào giỏ hàng thành công🎈🎈!!");
        displayUserMenu();

    }


    private void searchProduct() {

        List<Product> products = productService.getSearchProduct();
        displayProductList(products);

    }

    private void displayProductList(List<Product> productList) {
        if (productList.size() == 0) {
            System.out.println("Danh sách sản phẩm trống!!!");
        } else

            print(GREEN);
        System.out.println("\n                                        DANH SÁCH PRODUCT            ");
        System.out.println("|----------------------------------------------------------------------------------------|");
        System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |     PRICE   |  CATEGORY  |   STATUS " + " |");
        System.out.println("|----------------------------------------------------------------------------------------|");

        for (Product product : productList) {
            if (product.isProductStatus() != HIDE && product.getCategory().isCategoryStatus() != INACTIVE) {
                System.out.printf("|%-5d | %-17s | %-20s | %-10s| %-11s|%-11s|%n",
                        product.getId(), product.getProductName(), product.getProductDes(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
            }
        }
        System.out.println("|----------------------------------------------------------------------------------------|");

        productDetail();
    }

    private void productDetail() {
        System.out.println("Lựa chọn cách sắp xếp sản phẩm theo giá: ");
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println("|" + "   1. CHI TIẾT SẢN PHẨM         |    0. QUAY LẠI      " + "|");
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println("Nhập lựa chọn: ");
        switch (InputMethods.getInteger()) {
            case 1:
                likeCommentWishlistAndBuy();
                break;
            case 0:
                return;
            default:
                System.out.println(RED + "Không có chức năng phù hợp, vui lòng chọn lại!!!" + RESET);
                break;
        }
    }

    private void sortProduct() {
        System.out.println("Lựa chọn cách sắp xếp sản phẩm theo giá: ");
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println("|" + "   1. GIÁ TĂNG DẦN          |    2. GIÁ GIẢM DẦN        |    0. QUAY LẠI         " + "|");
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println("Nhập lựa chọn: ");
        switch (InputMethods.getInteger()) {
            case 1:
                List<Product> productList = productService.findAll().stream().sorted((p1, p2) -> (int) -(p1.getPrice() - p2.getPrice())).collect(Collectors.toList());
                displayProductList(productList);
                break;
            case 2:
                List<Product> products = productService.findAll().stream().sorted((p1, p2) -> (int) (p1.getPrice() - p2.getPrice())).collect(Collectors.toList());
                displayProductList(products);
                break;
            case 0:
                return;
            default:
                System.out.println(RED + "Không có chức năng phù hợp, vui lòng chọn lại!!!" + RESET);
                break;
        }

    }

    private void likeCommentWishlistAndBuy() {
        System.out.println("Nhập id sản phẩm cần xem chi tiết");
        int idProductDetail = InputMethods.getInteger();
        Product product = productService.findById(idProductDetail);
        if (product == null) {
            return;
        }
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        displayProductList(productList);
        print(GREEN);
        System.out.println("| " + 1 + " AddToCart    |    " + 2 + " Wishlist    |     " + 3 + "  Like  {" + (new LikeView().countLikeProduct(product)) + "} |   4  Comment    |   5  Show Comment {" + (new CommentView().showComment(product)) + "}  |     6.  Quay Lại   |");
        System.out.println("|----------------------------------------------------------------------------------------|");
        printFinish();
        System.out.println("Nhập lựa chọn: ");
        switch (InputMethods.getInteger()) {
            case 1:
                List<Product> products = productService.findAll();
                addToCart(products);
                break;
            case 2:
                addToWishList();
                break;
            case 3:
                new LikeView().handleLikeProduct(product);
                break;
            case 4:
                new CommentView().addNewComment(product);
                break;
            case 6:
                new CommentView().showComment(product);
                return;
            default:
                System.out.println(RED + "Không có chức năng phù hợp, vui lòng chọn lại!!!" + RESET);
                break;
        }
    }

    public void addToWishList() {
        List<Product> products = productService.findAll();
        if (products.isEmpty()) {
            printlnError("Chưa có sản phẩm");

        }
        // Hiển thị danh sách sản phẩm

        print(GREEN);
        System.out.println("\n                                           DANH SÁCH PRODUCT            ");
        System.out.println("|----------------------------------------------------------------------------------------|");
        System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |     PRICE   |  CATEGORY  |   STATUS " + " |");
        System.out.println("|----------------------------------------------------------------------------------------|");
        for (Product product : products) {
            if (product.getCategory().isCategoryStatus() != INACTIVE && product.isProductStatus() != HIDE) {
                System.out.printf("|%-5d | %-17s | %-20s | %-10s| %-11s|%-11s|%n",
                        product.getId(), product.getProductName(), product.getProductDes(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));

            }
        }
        System.out.println("|----------------------------------------------------------------------------------------|");
        printFinish();


        System.out.println("Nhập vào ID sản phẩm để thêm vào danh sách yêu thích");
        int productId;
        while (true) {
            productId = getInteger();
            Product product = productService.findById(productId);
            if (product == null || product.isProductStatus() == HIDE || product.getCategory().isCategoryStatus() == INACTIVE) {
                System.err.println("Không tìm thấy sản phẩm hoặc với ID " + productId);
            } else {
                // Tìm thấy sản phẩm và sản phẩm không bị ẩn, thoát khỏi vòng lặp
                break;
            }
        }


        // Tạo đối tượng Cart để lưu thông tin sản phẩm
        WishList wishList = new WishList();
        wishList.setProduct(productService.findById(productId));
        wishList.setWishListId(wishlistService.autoId());
        // Lưu đối tượng Cart vào giỏ hàng
        wishlistService.save(wishList);
        printlnSuccess("Thêm vào giỏ hàng thành công🎈🎈!!");
        displayUserMenu();

    }
}

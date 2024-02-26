package ra.view.admin;

import ra.config.InputMethods;
import ra.model.Cart;
import ra.model.Category;
import ra.model.Product;
import ra.model.User;
import ra.service.CategoryService;
import ra.service.ProductService;
import ra.service.UserService;

import java.util.List;
import java.util.stream.Collectors;


import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.*;
import static ra.constant.Constant.ProductStatus.HIDE;
import static ra.constant.Constant.ProductStatus.UNHIDE;
import static ra.constant.Until.formatCurrency;

public class ProductView {
    private UserService userService;
    private CategoryService categoryService;
    private ProductService productService;


    public ProductView() {
        this.userService = new UserService();
        this.categoryService = new CategoryService();
        this.productService = new ProductService();
    }

    public void displayMenuAdminMenuProduct() {
        int choice;
        do {

            print(BLUE);

            System.out.println(".--------------------------------------------------------.");
            System.out.println("| WELCOME ADMIN :                                          ");
            System.out.println("|--------------------------------------------------------|");
            System.out.println("|                     1. THÊM MỚI SẢN PHẨM               |");
            System.out.println("|                     2. HIỂN THỊ DANH SÁCH SẢN PHẨM     |");
            System.out.println("|                     3. CHỈNH SỬA THÔNG TIN SẢN PHẨM    |");
            System.out.println("|                     4. ẨN / HIỆN SẢN PHẨM              |");
            System.out.println("|                     5. ẨN / HIỆN NHIỀU SẢN PHẨM        |");
            System.out.println("|                     6. TÌM KIẾM SẢN PHẨM               |");
            System.out.println("|                     7. SẮP XẾP SẢN PHẨM THEO GIÁ       |");
            System.out.println("|                     8. SẮP XẾP SẢN PHẨM THEO TÊN       |");
            System.out.println("|                     9. LỌC SẢN PHẨM THEO TRANG THÁI    |");
            System.out.println("|                     10. QUAY LẠI MENNU TRƯỚC           |");
            System.out.println("|                     0. ĐĂNG XUẤT                       |");
            System.out.println("'--------------------------------------------------------'\n");

            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡: ");
            printFinish();

            choice = getInteger();
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    List<Product> productList = productService.findAll();
                    displayProducts(productList);
                    break;
                case 3:
                    editProduct();
                    break;
                case 4:
                    hideProduct();
                    break;
                case 5:
                    hideAllProduct();
                    break;
                case 6:
                    searchProductByName();
                    break;
                case 7:
                    sortByPrice();
                    break;
                case 8:
                    sortByName();
                    break;
                case 9:
                    fitterByStatus();
                    break;
                case 10:
                    return;
                case 0:
                    break;
                default:
                    break;
            }

        } while (choice != 10);
    }


    private void addProduct() {
        System.out.println("Nhập số sản phẩm cần thêm mới:");
        int numberOfProducts = getInteger();

        if (numberOfProducts <= 0) {
            System.err.println("Số sản phẩm phải lớn hơn 0");
            return; // Thoát ngay khi số lượng không hợp lệ
        }

        for (int i = 0; i < numberOfProducts; i++) {
            List<Product> products = productService.findAll();
            System.out.println("Sản phẩm thứ " + (i + 1));
            Product product = new Product();

            // Nhập tên sản phẩm và kiểm tra xem tên đã tồn tại chưa
            while (true) {
                System.out.println("Nhập tên sản phẩm:");
                String productName = getString();
                boolean isNameExists = false;

                for (Product pro : products) {
                    if (pro.getProductName().equalsIgnoreCase(productName)) {
                        isNameExists = true;
                        System.err.println("Tên sản phẩm đã tồn tại, mời nhập tên mới.");
                        break;
                    }
                }

                if (!isNameExists) {
                    product.setProductName(productName);
                    break; // Kết thúc vòng lặp khi tên hợp lệ và không trùng lặp
                }
            }

            // Nhập giá sản phẩm
            System.out.println("Nhập giá sản phẩm:");
            double price = InputMethods.getDouble();
            product.setPrice(price);

            System.out.println("Nhập mô tả sản phẩm:");
            String productDes = getString();
            product.setProductDes(productDes);

            // Nhập số lượng
            System.out.println("Nhập số lượng:");
            int quantity = getInteger();
            product.setQuantity(quantity);

            // Hiển thị danh sách danh mục
            List<Category> categories = categoryService.findAll();
            if (categories.isEmpty()) {
                printlnError("Danh sách danh mục rỗng. Vui lòng thêm danh mục trước!!");
                return; // Thoát nếu không có danh mục
            }

            print(GREEN);
            System.out.println("\n                    DANH SÁCH CATEGORY                 ");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STATUS " + " |");
            System.out.println("|-------------------------------------------------------------|");

            for (Category category : categories) {
                System.out.printf("|%-5d | %-17s | %-20s | %-9s |%n",
                        category.getId(), category.getCategoryName(), category.getCategoryDes(), (category.isCategoryStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
            }
            System.out.println("|-------------------------------------------------------------|");
            printFinish();
//            System.out.println("Nhập id catagory :");

            while (true) {
                System.out.println("Nhập id danh mục sản phẩm:");
                int categoryId = getInteger();
                Category selectedCategory = null;

                // Tìm danh mục được chọn bởi người dùng
                for (Category category : categories) {
                    if (category.getId().equals(categoryId)) {
                        selectedCategory = category;
                        break;
                    }
                }

                if (selectedCategory != null) {
                    product.setCategory(selectedCategory);
                    product.setProductStatus(UNHIDE);
                    product.setId(productService.autoId());
                    productService.save(product);
                    System.out.println("Tạo sản phẩm thành công");
                    break; // Kết thúc vòng lặp sau khi sản phẩm đã được tạo
                } else {
                    System.out.println("Id danh mục không tồn tại, mời nhập lại");
                }
            }
        }
    }

    private void displayProducts(List<Product> productList) {
        if (productList.isEmpty()) {
            System.err.println("Danh sách sản phẩm trống!!!");
        } else {
            print(GREEN);
            System.out.println("                                        DANH SÁCH PRODUCT            ");
            System.out.println("|-----------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STOCK  |      PRICE    |   CATEGORY  |  STATUS " + " |");
            System.out.println("|-----------------------------------------------------------------------------------------------------|");

            for (Product product : productList) {
                System.out.printf("|%-5d | %-17s | %-20s | %-8s| %-14s| %-12s| %-9s |%n",
                        product.getId(), product.getProductName(), product.getProductDes(), product.getStock(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
            }
            System.out.println("|-----------------------------------------------------------------------------------------------------|");
            printFinish();
        }
    }

    private void editProduct() {
        System.out.println("Nhập ID sản phẩm cần sửa: ");
        int id = getInteger();
        List<Product> products = productService.findAll();
        int index = -1; // Khởi tạo index bằng -1 để xác định xem sản phẩm có tồn tại hay không

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                index = i;
                break; // Thoát vòng lặp khi tìm thấy sản phẩm với ID tương ứng
            }
        }

        if (index != -1) {
            Product productToEdit = products.get(index);
            boolean isExit = false;

            while (true) {
                System.out.println("Nhập tên sản phẩm mới (Enter để bỏ qua):");
                String productName = scanner().nextLine();
                if (!productName.trim().isEmpty()) {
                    boolean isNameExists = false;

                    for (Product pro : products) {
                        if (!pro.getId().equals(id) && pro.getProductName().equalsIgnoreCase(productName)) {
                            isNameExists = true;
                            System.err.println("Tên sản phẩm đã tồn tại, mời nhập tên mới.");
                            break;
                        }
                    }

                    if (!isNameExists) {
                        productToEdit.setProductName(productName);
                        break; // Kết thúc vòng lặp khi tên hợp lệ và không trùng lặp
                    }
                } else {
                    break;
                }
            }

            // Nhập giá sản phẩm
            System.out.println("Nhập giá sản phẩm (Enter để bỏ qua):");
            while (true) {
                String priceInput = scanner().nextLine();
                if (priceInput.trim().isEmpty()) {
                    break;
                }
                try {
                    double price = Double.parseDouble(priceInput);
                    if (price >= 0) {
                        productToEdit.setPrice(price);
                        break;
                    } else {
                        System.err.println("Giá sản phẩm phải lớn hơn hoặc bằng 0.");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi: Giá sản phẩm không hợp lệ.");
                }
            }

            System.out.println("Nhập mô tả sản phẩm (Enter để bỏ qua):");
            while (true) {
                String productDes = scanner().nextLine();
                if (productDes.isEmpty()) {
                    break;
                } else {
                    productToEdit.setProductDes(productDes);
                    break;
                }
            }


            // Nhập số lượng
            System.out.println("Nhập số lượng (Enter để bỏ qua):");
            while (true) {
                String stockInput = scanner().nextLine();
                if (stockInput.trim().isEmpty()) {
                    break;
                }
                try {
                    int stock = Integer.parseInt(stockInput);
                    if (stock >= 0) {
                        productToEdit.setStock(stock);
                        break;
                    } else {
                        System.err.println("Số lượng sản phẩm phải lớn hơn hoặc bằng 0.");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi: Số lượng sản phẩm không hợp lệ.");
                }
            }

            System.out.println("Danh sách danh mục:");
            List<Category> categories = categoryService.findAll();

            print(GREEN);
            System.out.println("\n                    DANH SÁCH CATEGORY                 ");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STATUS " + " |");
            System.out.println("|-------------------------------------------------------------|");

            for (Category category : categories) {
                System.out.printf("|%-5d | %-17s | %-20s | %-9s |%n",
                        category.getId(), category.getCategoryName(), category.getCategoryDes(), (category.isCategoryStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
            }
            System.out.println("|-------------------------------------------------------------|");
            printFinish();


            System.out.println("Nhập ID danh mục mới (Enter để bỏ qua):");
            while (!isExit) {
                String st = scanner().nextLine();
                if (st.isEmpty()) {
                    break; // Người dùng bỏ qua việc nhập danh mục mới
                } else if (st.matches("\\d+")) { // Kiểm tra xem chuỗi chỉ chứa chữ số
                    int newCategoryId = Integer.parseInt(st);
                    Category newCategory = categoryService.findById(newCategoryId);
                    if (newCategory != null) {


                        productToEdit.setCategory(newCategory);
                        isExit = true; // Thoát khỏi vòng lặp sau khi nhập thành công ID danh mục
                    } else {
                        System.err.println("Danh mục không tồn tại. Mời nhập lại.");
                    }
                } else {
                    System.err.println("Hãy nhập một số nguyên hợp lệ.");
                }
            }

            productService.save(productToEdit); // Cập nhật thông tin sản phẩm
            System.out.println("Sửa sản phẩm thành công");


            List<User> users = userService.findAll();
            for (User user : users
            ) {
                for (Cart cart : user.getCart()
                ) {
                    if ((int) cart.getProduct().getId() == id) {
                        cart.setProduct(productToEdit);
                    }
                }
            }
            for (User user : users
            ) {
                userService.save(user);
            }

        } else {
            System.err.println("Không tìm thấy sản phẩm cần sửa !!!");
        }
    }

    private void hideProduct() {
        List<User> users = userService.findAll();
        boolean isChange = false;

        System.out.println("Hãy nhập id sản phẩm bạn muốn thay đổi trạng thái:");
        int idProduct = getInteger();
        Product product = productService.findById(idProduct);
        if (product == null) {
            printlnError("Không tìm thấy sản phẩm bạn muốn đổi trạng thái !!");
        } else {
            for (User user : users
            ) {
                for (Cart cart : user.getCart()) {
                    if (cart.getProduct().getId().equals(idProduct)) {
                        isChange = true;
                    }
                }
            }
            if (isChange) {
                printlnError("Sản phẩm có trong giỏ hàng, nên không thể ẩn sản phẩm");

            } else {
                product.setProductStatus(product.isProductStatus() == HIDE ? UNHIDE : HIDE);
                productService.save(product);
                printlnSuccess("Thay đổi trạng thái thành công!");
            }

        }
    }

    private void hideAllProduct() {
        List<User> users = userService.findAll();
        System.out.println("Nhập danh sách mã sản phẩm cần ẩn/hiện (cách nhau bằng dấu phẩy):");
        String inputIds = scanner().nextLine();
        // Tách danh sách mã sản phẩm thành mảng các ID
        String[] idStrings = inputIds.split(",");
        boolean anyChanges = false;

        for (String idString : idStrings) {
            try {
                int idProduct = Integer.parseInt(idString);
                Product product = productService.findById(idProduct);

                if (product == null) {
                    System.err.println("ID " + idProduct + " không tồn tại.");
                } else {
                    boolean isChange = false;
                    for (User user : users) {
                        for (Cart cart : user.getCart()) {
                            if (cart.getProduct().getId().equals(idProduct)) {
                                isChange = true;
                                break;  // Thoát khỏi vòng lặp khi sản phẩm được tìm thấy trong giỏ hàng
                            }
                        }
                        if (isChange) {
//                            System.err.println("ID sản phẩm: " + idProduct + " có trong giỏ hàng của người dùng " + user.getUsername() + ", nên không thể ẩn sản phẩm");
                            break;  // Thoát khỏi vòng lặp người dùng khi sản phẩm được tìm thấy trong giỏ hàng
                        }
                    }
                    if (!isChange) {
                        product.setProductStatus(product.isProductStatus() == HIDE ? UNHIDE : HIDE);
                        productService.save(product);
                        anyChanges = true;
                        System.out.println("ID sản phẩm: " + idProduct + " Thay đổi trạng thái thành công!");
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Lỗi: " + idString + " không phải là một số nguyên hợp lệ.");
            }
        }

//        if (anyChanges) {
//            System.out.println("Các thay đổi trạng thái sản phẩm đã được áp dụng!");
//        }
    }

    private void searchProductByName() {
        List<Product> products = productService.getSearchProduct();
        if (products.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống!!");

        } else {
            print(GREEN);
            System.out.println("                                        DANH SÁCH PRODUCT            ");
            System.out.println("|-----------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STOCK  |      PRICE    |   CATEGORY  |   STATUS " + " |");
            System.out.println("|-----------------------------------------------------------------------------------------------------|");

            for (Product product : products) {
                System.out.printf("|%-5d | %-17s | %-20s | %-8s| %-14s| %-12s| %-9s |%n",
                        product.getId(), product.getProductName(), product.getProductDes(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
            }
            System.out.println("|-----------------------------------------------------------------------------------------------------|");
            printFinish();
        }
    }
    private void sortByPrice() {
        System.out.println("Lựa chọn cách sắp xếp sản phẩm theo giá: ");
        System.out.println(  "+---------------------------------------------------------------------------------+"  );
        System.out.println("|"  + "   1. GIÁ TĂNG DẦN          |    2. GIÁ GIẢM DẦN        |    0. QUAY LẠI         " + "|");
        System.out.println(  "+---------------------------------------------------------------------------------+"  );
        System.out.println("Nhập lựa chọn: ");
        switch (InputMethods.getInteger()) {
            case 1:
                List<Product> productList = productService.findAll().stream().sorted((p1, p2) -> (int) -(p1.getPrice() - p2.getPrice())).collect(Collectors.toList());
                displayProducts(productList);
                break;
            case 2:
                List<Product> products = productService.findAll().stream().sorted((p1, p2) -> (int) (p1.getPrice() - p2.getPrice())).collect(Collectors.toList());
                displayProducts(products);
                break;
            case 0:
                return;
            default:
                System.out.println(RED + "Không có chức năng phù hợp, vui lòng chọn lại!!!" + RESET);
                break;
        }
    }
    private void sortByName() {
        List<Product> productList = productService.getSortNameProducts();
        displayProducts(productList);

    }
    private void fitterByStatus() {
        System.out.println("Lựa chọn trạng thái sản phẩm muốn lọc: ");
        System.out.println( "+---------------------------------------------------------------------------------+" );
        System.out.println("|" +   "   1. CÒN HÀNG             |   2. HẾT HÀNG          |   0. QUAY LẠI              "  + "|");
        System.out.println( "+---------------------------------------------------------------------------------+" );

        System.out.println("Nhập lựa chọn: ");
        switch (InputMethods.getInteger()) {
            case 1:
                List<Product> trueProduct = productService.findAll().stream().filter(Product::isProductStatus).collect(Collectors.toList());
                displayProducts(trueProduct);
                break;
            case 2:
                List<Product> falseProduct = productService.findAll().stream().filter(product -> !product.isProductStatus()).collect(Collectors.toList());
                displayProducts(falseProduct);
                break;
            case 0:
                return;
            default:
                System.out.println(RED + "Không có chức năng phù hợp, vui lòng chọn lại!!!" + RESET);
                break;
        }
    }
}

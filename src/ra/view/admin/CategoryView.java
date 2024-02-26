package ra.view.admin;

import ra.config.InputMethods;
import ra.model.Cart;
import ra.model.Category;
import ra.model.User;
import ra.service.CategoryService;
import ra.service.ProductService;
import ra.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.scanner;
import static ra.constant.Constant.CategoryStatus.ACTIVE;
import static ra.constant.Constant.Status.INACTIVE;

public class CategoryView {
    private CategoryService categoryService;
    private UserService userService;
    private ProductService productService;


    public CategoryView() {

        this.userService = new UserService();
        this.categoryService = new CategoryService();
        this.productService = new ProductService();
    }


    public void displayAdminCategory() {
        int choice;
        do {
            print(YELLOW);

            System.out.println(".-----------------------------------------------------------.");
            System.out.println("|                            ADMIN-CATEGORY                 |");
            System.out.println("|-----------------------------------------------------------|");
            System.out.println("|                     1. THÊM MỚI DANH MỤC                  |");
            System.out.println("|                     2. DANH SÁCH DANH MỤC                 |");
            System.out.println("|                     3. TÌM KIẾM DANH MỤC THEO TÊN         |");
            System.out.println("|                     4. CHỈNH SỬA THÔNG TIN DANH MUC       |");
            System.out.println("|                     5. ẨN / HIỆN DANH MỤC                 |");
            System.out.println("|                     6. ẨN / HIỆN NHIỀU DANH MỤC           |");
            System.out.println("|                     7. SẮP XẾP DANH MỤC THEO TÊN          |");
            System.out.println("|                     8. QUAY LẠI MENU TRƯỚC                |");
            System.out.println("|                     0. ĐĂNG XUẤT                          |");
            System.out.println("'-----------------------------------------------------------'");
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡 : ");
            printFinish();
            choice = InputMethods.getInteger();
            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    displayAllCategory();
                    break;
                case 3:
                    searchCategoryByName();
                    break;
                case 4:
                    editCategory();
                    break;
                case 5:
                    hideCategory();
                    break;
                case 6:
                    hideAllCategory();
                    break;
                case 7:
                    sortByName();
                    break;
                case 8:
                    return;
                case 0:
                    new UserService().logout();
                    break;
                default:
                    break;

            }
        } while (true);
    }

    private void addCategory() {

        System.out.println("Nhập số lượng danh mục cần thêm mới");
        int numberOfCategories = InputMethods.getInteger();
        if (numberOfCategories < 0) {
            printlnError("Số lượng danh mục thêm mới phải lớn hơn 0) ");
            return;
        }
        for (int i = 0; i < numberOfCategories; i++) {

            System.out.println("Danh muc thứ " + (i + 1));
            Category category = new Category();
            // Nhập tên danh mục kiểm tra tên danh mục đã tồn tại chưa

            while (true) {
                List<Category> categories = categoryService.findAll();
                System.out.println("Nhập tên danh mục");
                String categoryName = InputMethods.getString();
                if (categories.isEmpty()) {
                    category.setCategoryName(categoryName);
                    break;
                }
                boolean isNameExists = false;
//                for (Category ca : categories) {
//                    if (ca.getCategoryName().equalsIgnoreCase(categoryName)) {
//                        isNameExists = true;
//                        printlnError("Tên danh mục đã tồn tại, mời nhập lại");
//                        break;
//
//                    }
//                }
//                if (!isNameExists) {
//                    category.setCategoryName(categoryName);
//                    break;
//                }
                Optional<Category> ca = categories.stream()
                        .filter(t -> t.getCategoryName().equalsIgnoreCase(categoryName))
                        .findFirst();
                if (ca.isPresent()) {
                    printlnError("Tên danh mục đã tồn tại, mời nhập lại");
                } else {
                    category.setCategoryName(categoryName);
                    break;
                }

            }
            System.out.println("Nhập mô tả danh mục");
            String categoryDes = InputMethods.getString();
            category.setCategoryDes(categoryDes);
            category.setId(categoryService.autoId());
            categoryService.save(category);
        }
    }


    private void displayAllCategory() {
        List<Category> categories = categoryService.findAll();
        if (categories.isEmpty()) {
            System.err.println("Danh sách Category rỗng");
        } else {
            print(BLUE);
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
        }
    }

    private void searchCategoryByName() {
        List<Category> categoryList = categoryService.findAll();
//        List<Category> categories = new ArrayList<>();
        System.out.println("Nhập tên danh mục cần tìm kiếm");
        String searchName = InputMethods.getString();
//        for (Category ca : categoryList) {
//            if (ca.getCategoryName().toLowerCase().contains(searchName.toLowerCase())) {
//                categories.add(ca);
//            }
//        }
        // Dùng stream
        List<Category> categories = categoryList.stream()
                .filter(t -> t.getCategoryName().toLowerCase().contains(searchName.toLowerCase()))
                .collect(Collectors.toList());
        //----------------
        if (categories.isEmpty()) {
            printlnError("Không tìm thấy danh mục phù hợp");
        } else {
            print(BLUE);
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
        }
    }

    private void editCategory() {
        List<Category> categoryList = categoryService.findAll();
        displayAllCategory();
        if (categoryList.isEmpty()) {
            return;
        }
        System.out.println("Nhập ID của danh mục cần chỉnh sửa:");
        int choice = InputMethods.getInteger();
        Category categoryToEdit = categoryService.findById(choice);
        if (categoryToEdit == null) {
            printlnError("ID không hợp lệ");
        } else {
            System.out.println("Nhập tên danh mục mới (Nhấn Enter để bỏ qua):");
            String newNameCate = InputMethods.getInput();
            boolean canExit = true;
            if (!newNameCate.trim().isEmpty()) {
                boolean isDuplicate;
                do {
                    isDuplicate = false;
                    for (Category ca : categoryList) {
                        if (ca.getCategoryName().equalsIgnoreCase(newNameCate)) {
                            printlnError("Tên danh mục đã tồn tại. Vui lòng nhập tên khác:");
                            newNameCate = InputMethods.getInput();
                            isDuplicate = true;
                            break;
                        }
                    }
                } while (isDuplicate);
                categoryToEdit.setCategoryName(newNameCate);
            }
            System.out.println("Nhập mô tả danh mục mới (Nhấn Enter để bỏ qua):");
            String newDes = InputMethods.getInput();
            if (!newDes.trim().isEmpty()) {
                categoryToEdit.setCategoryDes(newDes);
            }
            categoryService.save(categoryToEdit);
        }
    }

    private void hideCategory() {
        List<User> users = userService.findAll();
        boolean isChange = false;
        displayAllCategory();
        System.out.println("Hãy nhập id Category bạn muốn thay đổi trạng thái:");
        int id = InputMethods.getInteger();
        Category category = categoryService.findById(id);
        if(category == null) {
            printlnError("Không tìm thấy category bạn muốn đổi trạng thái !!");
        } else {
            for (User user : users
            ) {
                for (Cart cart : user.getCart()) {
                    if (cart.getProduct().getCategory().getId().equals(id)) {
                        isChange = true;
                    }
                }
            }
            if (isChange) {
                printlnError("Sản phẩm có trong giỏ hàng, nên không thể ẩn Category");
            } else {
                category.setCategoryStatus((category.isCategoryStatus() == ACTIVE ? INACTIVE: ACTIVE));
               printlnSuccess("Thay đổi trạng thái thành công!");
            }
            categoryService.save(category);
        }
    }
    private void hideAllCategory() {
        List<Category> categories = categoryService.findAll();
        List<User> users = userService.findAll();
        System.out.println("Nhập danh sách mã danh mục cần ẩn/hiện (cách nhau bằng dấu phẩy):");
        String inputIds = scanner().nextLine();

        // Tách danh sách mã danh mục thành mảng các ID
        String[] idStrings = inputIds.split(",");
        boolean anyChanges = false;

        for (String idString : idStrings) {
            try {
                int idCategory = Integer.parseInt(idString);
                Category category = categoryService.findById(idCategory);

                if (category == null) {
                    System.err.println("ID " + idCategory + " không tồn tại.");

                } else {
                    boolean isChange = false;
                    for (User user : users) {
                        for (Cart cart : user.getCart()) {
                            if (cart.getProduct().getCategory().getId().equals(idCategory)) {
                                isChange = true;
                                break;  // Thoát khỏi vòng lặp khi sản phẩm được tìm thấy trong giỏ hàng
                            }
                        }
                        if (isChange) {
                            System.err.println("ID sản phẩm: " + idCategory + " có trong giỏ hàng của người dùng " + user.getUserName() + ", nên không thể ẩn sản phẩm");
                            break;  // Thoát khỏi vòng lặp người dùng khi sản phẩm được tìm thấy trong giỏ hàng
                        }
                    }
                    if (!isChange) {
                        category.setCategoryStatus((category.isCategoryStatus() == ACTIVE ? INACTIVE: ACTIVE));
                        categoryService.save(category);
                        anyChanges = true;
                        System.out.println("ID danh mục: " + idCategory + " Thay đổi trạng thái thành công!");
                    }

                }
            } catch (NumberFormatException e) {
                System.err.println("Lỗi: " + idString + " không phải là một số nguyên hợp lệ.");
            }
        }

        if (anyChanges) {
            printlnSuccess("Thay đổi trạng thái thành công!");
            // Lưu trạng thái của danh mục sau khi thay đổi

        }
    }
    private void sortByName() {
        List<Category> categoryList = categoryService.sortByName();
        print(BLUE);
        System.out.println("\n                    DANH SÁCH DANH MỤC THEO TÊN               ");
        System.out.println("|-------------------------------------------------------------|");
        System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STATUS " + " |");
        System.out.println("|-------------------------------------------------------------|");

        for (Category category : categoryList) {
            System.out.printf("|%-5d | %-17s | %-20s | %-9s |%n",
                    category.getId(), category.getCategoryName(), category.getCategoryDes(), (category.isCategoryStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
        }
        System.out.println("|-------------------------------------------------------------|");
        printFinish();
    }
}

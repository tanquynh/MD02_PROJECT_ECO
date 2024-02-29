package ra.presentation.view.admin;

import ra.business.config.InputMethods;
import ra.business.model.Cart;
import ra.business.model.Category;
import ra.business.model.User;
import ra.business.design.CategoryDesign;
import ra.business.design.ProductDesign;
import ra.business.design.UserDesign;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ra.business.config.ConsoleColor.*;
import static ra.business.config.InputMethods.scanner;
import static ra.business.constant.Constant.CategoryStatus.ACTIVE;
import static ra.business.constant.Constant.Status.INACTIVE;

public class CategoryView {
    private CategoryDesign categoryDesign;
    private UserDesign userDesign;
    private ProductDesign productDesign;


    public CategoryView() {

        this.userDesign = new UserDesign();
        this.categoryDesign = new CategoryDesign();
        this.productDesign = new ProductDesign();
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
                    new UserDesign().logout();
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
                List<Category> categories = categoryDesign.findAll();
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
            category.setId(categoryDesign.autoId());
            categoryDesign.save(category);
        }
    }


    private void displayAllCategory() {
        List<Category> categories = categoryDesign.findAll();
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
        List<Category> categoryList = categoryDesign.findAll();
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
        List<Category> categoryList = categoryDesign.findAll();
        displayAllCategory();
        if (categoryList.isEmpty()) {
            return;
        }
        System.out.println("Nhập ID của danh mục cần chỉnh sửa:");
        int choice = InputMethods.getInteger();
        Category categoryToEdit = categoryDesign.findById(choice);
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
            categoryDesign.save(categoryToEdit);
        }
    }

    private void hideCategory() {
        List<User> users = userDesign.findAll();
        boolean isChange = false;
        displayAllCategory();
        System.out.println("Hãy nhập id Category bạn muốn thay đổi trạng thái:");
        int id = InputMethods.getInteger();
        Category category = categoryDesign.findById(id);
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
            categoryDesign.save(category);
        }
    }
    private void hideAllCategory() {
        List<Category> categories = categoryDesign.findAll();
        List<User> users = userDesign.findAll();
        System.out.println("Nhập danh sách mã danh mục cần ẩn/hiện (cách nhau bằng dấu phẩy):");
        String inputIds = scanner().nextLine();

        // Tách danh sách mã danh mục thành mảng các ID
        String[] idStrings = inputIds.split(",");
        boolean anyChanges = false;

        for (String idString : idStrings) {
            try {
                int idCategory = Integer.parseInt(idString);
                Category category = categoryDesign.findById(idCategory);

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
                        categoryDesign.save(category);
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
        List<Category> categoryList = categoryDesign.sortByName();
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

package ra.business.design;

import ra.business.constant.Constant;
import ra.business.model.Order;
import ra.business.repository.FileRepo;

import java.util.ArrayList;
import java.util.List;

public class OrderDesign implements IShop<Order> {
    FileRepo<Order, Integer> orderFileRepo;
    private List<Order> orderList;

    public OrderDesign() {
        this.orderFileRepo = new FileRepo<>(Constant.FilePath.ORDER_FILE);
    }
    @Override
    public void save(Order order) {
        orderFileRepo.save(order);
    }

    @Override
    public List<Order> findAll() {
        return orderFileRepo.findAll();
    }

    @Override
    public Order findById(int id) {
        return orderFileRepo.findById(id);
    }

    @Override
    public int autoId() {
        return orderFileRepo.autoId();
    }
    public List<Order> getOrdersByStatus(byte statusCode) {
        List<Order> orderList1 = findAll();
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orderList1) {
            if (order.getStatus() == statusCode) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }
}


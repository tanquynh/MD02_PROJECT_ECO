package ra.business.design;

import java.util.List;

public interface IShop <T>{
    List<T> findAll();
    void save(T t);
    T findById(int id);
    int autoId();

}

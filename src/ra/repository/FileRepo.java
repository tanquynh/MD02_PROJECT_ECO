package ra.repository;

import ra.model.Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import static ra.config.ConsoleColor.printlnError;
import static ra.constant.Constant.FilePath.COMMON_PATH;

public class FileRepo<T extends Entity, ID extends Number> {
    private File file;

    public FileRepo() {
    }

    public FileRepo(String filePath) {
        File dataDir = new File(COMMON_PATH);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        this.file = new File(COMMON_PATH + filePath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            printlnError("Lỗi khi tạo file");
        }
    }

    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(this.file);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            list = (List<T>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
        return list;
    }

    public void saveToFile(List<T> t) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(t);
            objectOutputStream.close();
        } catch (IOException e) {
            printlnError("Có lỗi xảy ra khi ghi file");
        }
    }

    public void save(T t) {
//        List<T> list = findAll();
//        int index = -1;
//        for (int i = 0; i < list.size(); i++) {
//            if(list.get(i).getId().equals(t.getId())) {
//                index = i;
//                break;
//            }
//        }
//        if(index != -1) {
//            list.set(index, t);
//        } else {
//            list.add(t);
//        }
//        saveToFile(list);

        List<T> list = findAll();
        OptionalInt index = IntStream.range(0, list.size())
                .filter(i -> list.get(i).getId().equals(t.getId()))
                .findFirst();
        if (index.isPresent()) {
            list.set(index.getAsInt(), t);
        } else {
            list.add(t);
        }
        saveToFile(list);
    }

    public T findById(ID id) {
        List<T> list = findAll();
//        for (T t : list) {
//            if (t.getId().equals(id)) {
//                return t;
//            }
//        }
//        return  null;

         Optional<T> e = list.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
        return e.orElse(null);

    }

    public int autoId() {
        List<T> list = findAll();
//        int maxId = list.isEmpty() ? 0 : list.get(list.size() - 1).getId().intValue();
//        return maxId + 1;
        OptionalInt maxId = list.stream().mapToInt(t -> t.getId().intValue()).max();
        return maxId.orElse(0) + 1;

    }
}

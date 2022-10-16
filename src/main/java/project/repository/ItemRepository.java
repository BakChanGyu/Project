package project.repository;

import org.springframework.stereotype.Repository;
import project.file.Item;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepository {

    private Map<Integer, Item> store = new HashMap<>();
    private int sequence = 0;

    public Item save(Item item) {
        item.setItemId(++sequence);
        store.put(item.getItemId(), item);
        return item;
    }
}

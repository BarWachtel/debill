package redis;

import database.entity.Bill;
import database.entity.Item;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisClient {
    private static String redisHost = "localhost";
    private static int redisPort = 6379;
    private static Jedis jedis = new Jedis(redisHost, redisPort);

    private static final String USER_KEY = "user:";
    private static final String BILL_KEY = "bill:";
    private static final String ITEM_KEY = "item:";

    private RedisClient() {
    }

    private static final RedisClient instance = new RedisClient();

    public static RedisClient getInstance() {
        return instance;
    }

    /**
     * This method gets a user ID, and use it as the key for a bill.
     * The value returns from Redis is a LIST with all the bill's items' IDs.
     * For each item id, we use it as a key to Redis and get a Hashmap value of the item.
     *
     * @param userId - The user who opened the bill
     * @return Bill object with manager and items
     */
    public Bill getBillByUserId(int userId) {
        String userIdKey = USER_KEY + userId;
        int billId = Integer.parseInt(jedis.get(userIdKey));
        String key = userIdKey + BILL_KEY + billId;
        Bill bill = new Bill();
        Long listLen = jedis.llen(key);
        List<String> itemsIDs = jedis.lrange(key, 0, listLen - 1);
        for (String itemID : itemsIDs) {
            int itemIdAsInt = Integer.parseInt(itemID);
            Item item = getItem(billId, itemIdAsInt);
            bill.addItem(item);
        }
        return bill;
    }

    public void setBillByUserId(Bill bill) {
        String key = USER_KEY + bill.getManager().getID();
        jedis.set(key, String.valueOf(bill.getID()));
    }

    public Item getItem(int billId, int itemId) {
        String itemKey = BILL_KEY + billId + ITEM_KEY + itemId;
        Map<String, String> itemProperties = jedis.hgetAll(itemKey);
        Item item = new Item();
        item.setID(itemId);
        item.setBillId(billId);
        item.setName(itemProperties.get("name"));
        item.setPrice(Float.parseFloat(itemProperties.get("price")));
        item.setQuantity(Integer.parseInt(itemProperties.get("quantity")));
        return item;
    }

    public void setItem(Item item) {
        String itemKey = BILL_KEY + item.getBillId() + ITEM_KEY + item.getID();
        Map<String, String> itemProperties = new HashMap<>();
        itemProperties.put("name", item.getName());
        itemProperties.put("price", Float.toString(item.getPrice()));
        itemProperties.put("quantity", Integer.toString(item.getQuantity()));
        jedis.hmset(itemKey, itemProperties);
    }

    public Long removeItem(Item item) {
        String itemKey = BILL_KEY + item.getBillId() + ITEM_KEY + item.getID();
        return jedis.hdel(itemKey);
    }

    public void setItemsOfBill(Bill bill) {
        setListOfItemsIDs(bill);
        setEachItemInHashMap(bill);
    }

    private void setListOfItemsIDs(Bill bill) {
        String key = BILL_KEY + bill.getID();
        for (Item item : bill.getItems()) {
            jedis.lpush(key, Integer.toString(item.getID()));
        }
    }

    private void setEachItemInHashMap(Bill bill) {
        for (Item item : bill.getItems()) {
			this.setItem(item);
		}
    }

    public void removeAllItems(Bill bill) {
		for (Item item : bill.getItems()) {
			this.removeItem(item);
		}
    }
}

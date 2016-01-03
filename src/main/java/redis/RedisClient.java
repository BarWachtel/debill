package redis;

import database.entity.Bill;
import database.entity.User;
import org.apache.commons.lang3.SerializationUtils;
import redis.clients.jedis.Jedis;

public class RedisClient {
	private static String redisHost = "localhost";
	private static int redisPort = 6379;

	private static Jedis redisClient = new Jedis(redisHost, redisPort);

	// Lets hide complexity of Jedis by only exposing the methods we use ;]
	public static void setItemHashmapByBillId(int billId, Bill bill) {
		// userId == key
		// bill == value

		redisClient.set(SerializationUtils.serialize(billIdAsKey(billId)), SerializationUtils.serialize(bill));
	}

	public static Bill getBillById(int billId) {
		byte[] billBytes = redisClient.get(SerializationUtils.serialize(billIdAsKey(billId)));
		Bill bill = (Bill) SerializationUtils.deserialize(billBytes);
		return bill;
	}

	public static void setBillByUserId(int userId, int billId) {
		redisClient.set(getUserIdAsKey(userId), String.valueOf(billId));
	}

	public static int getBillByUserId(int userId) {
		return Integer.valueOf(redisClient.get(getUserIdAsKey(userId)));
	}

	private static String getUserIdAsKey(int userId) {
		return "USERID:" + userId;
	}

	private static String billIdAsKey(int billId) {
		return "BILLID:" + billId;
	}

	public static void main(String[] args) {
		redisClient.set("test", "ya bababa");
		System.out.println(redisClient.get("test"));

		User user = new User(5, "baz", "wachtel");
		Bill bill = new Bill(7, user, true, true, null);
		RedisClient.setItemHashmapByBillId(user.getId(), bill);

		Bill aNewBill = RedisClient.getBillById(user.getId());

		System.out.println(aNewBill.toString());
	}
}

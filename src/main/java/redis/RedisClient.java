package redis;

import database.entity.Bill;
import database.entity.User;
import org.apache.commons.lang3.SerializationUtils;
import redis.clients.jedis.Jedis;

public class RedisClient {
	private static String redisHost = "localhost";
	private static int redisPort = 6379;

	private static Jedis redisClient = new Jedis(redisHost, redisPort);

	public static void setItemHashmapByBillId(int billId, Bill bill) {
		redisClient.set(SerializationUtils.serialize(generateBillIdAsKey(billId)),
				SerializationUtils.serialize(bill));
	}

	public static Bill getBillById(int billId) {
		byte[] billBytes = redisClient.get(SerializationUtils.serialize(generateBillIdAsKey(billId)));
		return SerializationUtils.deserialize(billBytes);
	}

	public static void setBillByUserId(int userId, int billId) {
		redisClient.set(generateUserIdAsKey(userId), String.valueOf(billId));
	}

	public static int getBillByUserId(int userId) {
		return Integer.valueOf(redisClient.get(generateUserIdAsKey(userId)));
	}

	private static String generateUserIdAsKey(int userId) {
		return "USERID:" + userId;
	}

	private static String generateBillIdAsKey(int billId) {
		return "BILLID:" + billId;
	}

	public static void main(String[] args) {
		redisClient.set("test", "ya bababa");
		System.out.println(redisClient.get("test"));

		User user = new User(5, "baz", "wachtel");
		Bill bill = new Bill(7, user, true, true, null);
		RedisClient.setItemHashmapByBillId(user.getID(), bill);

		Bill aNewBill = RedisClient.getBillById(user.getID());

		System.out.println(aNewBill.toString());
	}
}

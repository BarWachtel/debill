package redis;

import database.entity.Bill;
import database.entity.Item;
import database.entity.User;
import org.apache.commons.lang3.SerializationUtils;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RedisClient {
	private static String redisHost = "localhost";
	private static int redisPort = 6379;

	private static Jedis redisClient = new Jedis(redisHost, redisPort);

	// Lets hide complexity of Jedis by only exposing the methods we use ;]
	public static void setBill(int userId, Bill bill) {
		// userId == key
		// bill == value

		//redisClient.set(SerializationUtils.serialize(userIdAsKey(userId)), SerializationUtils.serialize(bill));
	}

	public static Bill getBill(int userId) {
		byte[] billBytes = redisClient.get(SerializationUtils.serialize(userIdAsKey(userId)));
		Bill bill = (Bill) SerializationUtils.deserialize(billBytes);
		return bill;
	}

	private static String userIdAsKey(int userId) {
		return "USERID:" + userId;
	}
//	public static void main(String[] args) {
//		Jedis redisClient = new Jedis("localhost", 6379);
//		try {
//			DriverManager.getConnection("jdbc:mysql://localhost", "root", "dmitri1928351991");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	public static String get(String key) {
		return redisClient.get(key);
	}

	public static String set(String key, String value) {
		return redisClient.set(key, value);
	}

	public static void main(String[] args) {
		redisClient.set("test", "ya bababa");
		System.out.println(redisClient.get("test"));

		User user = new User(5, "baz", "wachtel");
		Bill bill = new Bill(7, user, true, null);
		RedisClient.setBill(user.getId(), bill);

		Bill aNewBill = RedisClient.getBill(user.getId());

		System.out.println(aNewBill.toString());
	}
}

import redis.clients.jedis.Jedis;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by user on 29/11/2015.
 */
public class RedisClient {
	public static void main(String[] args) {
		Jedis redisClient = new Jedis("localhost", 6379);
		try {
			DriverManager.getConnection("jdbc:mysql://localhost", "bazza", "bazza");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		redisClient.set("test", "ya bababa");
		System.out.println(redisClient.get("test"));
	}
}

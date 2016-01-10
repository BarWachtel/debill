package api.route.pojo.request;

/**
 * Created by user on 10/01/2016.
 */
public class UserLoginDetails {
	String username;
	String password;

	public UserLoginDetails() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

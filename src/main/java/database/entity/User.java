package database.entity;

import java.sql.Date;

public class User extends Entity {

    private String username;
	private String password;

	public User() {

    }

    public User(int id, String username, String password) {
        this.id = id;
		this.username = username;
		this.password = password;
    }

    @Override
    public int getID() {
        return id;
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

	public boolean comparePassword(String password) {
		return this.password.equals(password);
	}
}


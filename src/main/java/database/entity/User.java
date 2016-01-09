package database.entity;

import java.sql.Date;

public class User extends Entity {

    private int id = -1;
    private String firstName;
    private String lastName;
	private String facebookId;
	private Date creationTime;

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public User() {

    }

    public User(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public User(int id, String firstName, String lastName,String facebookId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.facebookId = facebookId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int getID() {
        return id;
    }
}

/**
 CREATE TABLE `debill`.`user` (
 `firstname` VARCHAR(35) NOT NULL,
 `lastname` VARCHAR(35) NULL,
 `create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
 `facebookid` VARCHAR(65) NULL,
 `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
 PRIMARY KEY (`id`),
 UNIQUE INDEX `id_UNIQUE` (`id` ASC));
 */

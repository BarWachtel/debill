package database.entity;

import java.sql.Date;

public class User extends Entity {

    private String firstName;
    private String lastName;
    private String password;

	public User() {

    }

    public User(int id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
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

    public boolean comparePassword(String password) {
        return this.password.equals(password);
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

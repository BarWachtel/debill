package database.entity;

import java.io.Serializable;

public abstract class Entity implements Serializable {

    protected int id;

    public int getID() {return id; }
    public void setID(int id) {this.id = id;}
}

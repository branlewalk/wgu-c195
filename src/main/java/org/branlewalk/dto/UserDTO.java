package org.branlewalk.dto;

public class UserDTO {

    private int id;
    private String name;
    private String password;
    private boolean active;

    public UserDTO(int id, String name, String password, boolean active) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }
}

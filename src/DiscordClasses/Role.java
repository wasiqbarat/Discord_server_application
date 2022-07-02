package DiscordClasses;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    private String name;
    private ArrayList<Permission> permissions;
    private ArrayList<String> members;

    public Role(String name, ArrayList<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
        members = new ArrayList<>();
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public String getName() {
        return name;
    }

    public boolean isMember(String userName) {
        return members.contains(userName);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMember(String userName) {
        members.add(userName);
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", permissions=" + permissions +
                ", members=" + members +
                '}';
    }
}
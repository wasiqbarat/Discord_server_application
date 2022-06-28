package DiscordClasses;

import java.util.ArrayList;

public class Role {
    private String name;
    private final ArrayList<Permission> permissions;

    public Role(String name) {
        this.name = name;
        permissions = new ArrayList<>();
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
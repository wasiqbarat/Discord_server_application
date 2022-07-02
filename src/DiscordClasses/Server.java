package DiscordClasses;

import File.ServerDataBase;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * note that The Server class(this class) is different from Server in Server package
 * This class is Discord server existent
 *
 * @author wasiq
 */
public class Server implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;

    private final String owner;
    private String name;
    private final ArrayList<Role> roles;
    private final HashMap<String , Role> usersRole;//this hashmap maps each user to its role

    public Server(String name, String owner) {
        this.owner = owner;
        this.name = name;
        roles = new ArrayList<>();
        //
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.USERS_ADD);
        Role role = new Role("defaultRole", permissions);//every channel has a default role
        roles.add(role);
        usersRole = new HashMap<>();
    }

    public Role getUserRoles(String userName) {
        if (usersRole.get(userName) == null) {
            ArrayList<Permission> permissions = new ArrayList<>();
            permissions.add(Permission.USERS_ADD);
            usersRole.put(userName, new Role("defaultRole", permissions));
        }
        return usersRole.get(userName);
    }

    public void updateUserRoles(String userName, Role role) {
        if (usersRole.get(userName) == null) {
            ArrayList<Permission> permissions = new ArrayList<>();
            permissions.add(Permission.USERS_ADD);
            usersRole.put(userName, new Role("defaultRole", permissions));
        }
        usersRole.put(userName, role);
    }

    public void setRoleForUser(String userName, String roleName) {
        Role role = null;
        for (Role role1 : roles) {
            if (role1.getName().equals(roleName) ) {
                role1.addMember(userName);
                role = role1;
                System.out.println("roles is true");
                break;
            }
        }

        System.out.println(role);
        usersRole.put(userName, role);

        ServerDataBase.getInstance().updateServers(this);
        ServerDataBase.getInstance().reloadServers();
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isEqual(Server server) {
        return server.name.equals(name) && server.owner.equals(owner);
    }

}

package RespondingCommand;

import Classes.DiscordFriend;
import DiscordClasses.*;
import Exceptions.InvalidChannelName;
import Exceptions.InvalidServerName;
import Exceptions.PermissionException;
import File.DataBase;
import File.ServerDataBase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Server.Data;

import static DiscordClasses.Permission.getPermission;

public class ServerResponder extends Respond {
    private ServerDataBase serverDataBase;
    private DataBase dataBase;
    private Data data = Data.getInstance();

    public ServerResponder(Socket socket, JSONObject info) {
        super(socket, info);
        serverDataBase = ServerDataBase.getInstance();
        dataBase = DataBase.getInstance();
    }

    @Override
    public void handle() throws Exception {
        try {
            switch (info.getString("process")) {
                case "newServer" -> newServer();
                case "addMember" -> friendsToAdd();
                case "removeMemberFromServer" -> removeUserFromServer();
                case "authentication" -> authentication();
                case "applyMemberAdd" -> applyMemberAdd();
                case "createRole" -> createRole();
                case "myServers" -> myServers();
                case "membersActiveStatus" -> membersActiveStatus();
                case "serverPanel" -> serverPanel();
                case "applyCreatingChannel" -> applyCreatingChannel();
                case "serverChannels" -> getServerChannels();
            }
            info.put("method", "loggedIn");

            parseMessageToJsonAndSendToClient(info);
        } catch (Exception e) {
            e.printStackTrace();
            parseErrorToJsonAndSendToClient(e);
        }
    }

    private void getServerChannels() {
        String serverName = info.getString("serverName");
        ArrayList<Channel> channels = serverDataBase.getServerChannels().get(serverName);

        JSONArray jsonArray = new JSONArray();
        for (Channel channel : channels) {
            jsonArray.put(channel.getName());
        }

        info.put("channels", jsonArray);
        info.put("method", "loggedIn");
        info.put("process", "serverChannelsPanel");
    }

    private void applyCreatingChannel() throws InvalidChannelName {
        String serverName = info.getString("serverName");
        String channelName = info.getString("channelName");

        ArrayList<Channel> serverChannels = serverDataBase.getServerChannels().get(serverName);
        if (serverChannels == null) {
            serverChannels = new ArrayList<>();
            serverDataBase.updateServerChannels(serverName, new ArrayList<>());
        }

        for (Channel channel : serverChannels) {
            if (channel.getName().equals(channelName)) {
                throw new InvalidChannelName();
            }
        }

        String channelType = info.getString("channelType");

        Channel channel = null;
        switch (channelType) {
            case "text" -> channel = new TextChannel(channelName);
            case "voice" -> channel = new VoiceChannel(channelName);
        }
        ArrayList<Channel> channels = new ArrayList<>();
        channels.add(channel);
        serverDataBase.updateServerChannels(serverName, channels);

        ArrayList<String> channelUsers = new ArrayList<>();
        channelUsers.add(info.getString("userName"));
        serverDataBase.updateChannelUsers(channelName, channelUsers);


        info.put("method", "loggedIn");
        info.put("process", "channelPanel");

    }

    private void newServer() throws InvalidServerName {
        String serverName = info.getString("serverName");
        ArrayList<Server> servers = serverDataBase.getServers();

        for (Server server : servers) {
            if (server.getName().equals(serverName)) {
                throw new InvalidServerName();
            }
        }

        Server server = new Server(serverName, info.getString("userName"));
        String userName = info.getString("userName");


        servers.add(server);
        serverDataBase.updateServers(server);
        //
        ArrayList<String> serverUsers = new ArrayList<>();
        serverUsers.add(info.getString("userName"));
        serverDataBase.updateServerUsers(serverName, serverUsers);

        Server server1 = new Server(serverName, userName);
        ArrayList<Server> userServers1 = new ArrayList<>();
        userServers1.add(server1);

        serverDataBase.updateUserServers(userName, userServers1);
        serverDataBase.updateServerChannels(serverName, new ArrayList<>());

        //update server


        info.put("process", "serverPanel");
    }

    private void authentication() throws PermissionException {
        Server server = serverDataBase.getServer(info.getString("serverName"));
        String userName = info.getString("userName");

        String action = info.getString("action");
        Permission permission = Permission.getPermission(action);

        Role userRole = server.getUserRoles(userName);

        if (userRole.hasPermission(permission) || server.getOwner().equals(userName)) {
            switch (action) {
                case "removeMember" -> {
                    getServerUsers(action);
                }
                case "createChannel" -> {
                    info.put("method", "loggedIn");
                    info.put("process", "panelForCreatingChannel");
                }
                case "removeChannel" -> {
                    removeUserFromServer();
                }
                case "userLimit" -> {
                    System.out.println();
                }
                case "banUser" -> {
                    System.out.println(1);
                }
                case "renameServer" -> {
                    System.out.println(2);
                }
                case "chatHistoryAccess" -> {
                    System.out.println(3);
                }
                case "messagePin" -> {
                    System.out.println(4);
                }
            }
        } else {
            throw new PermissionException();
        }
    }

    private void getServerUsers(String action) {
        ArrayList<String> members = serverDataBase.getServerUsers().get(info.getString("serverName"));
        JSONArray serverMembers = new JSONArray();

        if (members == null) {
            members = new ArrayList<>();
            serverDataBase.updateServerUsers(info.getString("serverName"), members);
        }

        for (String user : members) {
            if (!user.equals(info.getString("userName"))) {
                serverMembers.put(user);
            }
        }

        info.put("serverUsers", serverMembers);
        switch (action) {
            case "removeMember" -> info.put("process", "selectUserToRemoveFromServer");
            case "userLimit" -> info.put("process", "selectUserToLimit");
            case "banUser" -> info.put("process", "selectUserToBan");
        }

    }

    private void removeUserFromServer() {
        String memberToRemove = info.getString("memberToRemove");
        String serverName = info.getString("serverName");

        HashMap<String, ArrayList<String>> channelUsers = serverDataBase.getChannelUsers();
        ////
        channelUsers.forEach((key, value) -> {
            if (value == null) {
                value = new ArrayList<>();
                serverDataBase.updateChannelUsers(key, value);
            } else {
                value.remove(memberToRemove);
            }
        });
        ////
        HashMap<String, ArrayList<String>> serverUsers = serverDataBase.getServerUsers();
        ArrayList<String> users = serverUsers.get(serverName);
        users.remove(memberToRemove);
        serverDataBase.updateServerUsers(serverName, users);
        ////
        ArrayList<Server> userServers = serverDataBase.getUserServers().get(memberToRemove);
        Iterator<Server> iterator = userServers.iterator();
        while (iterator.hasNext()) {
            Server server = (Server) iterator.next();
            if (server.getName().equals(serverName)) {
                iterator.remove();
                break;
            }
        }
        serverDataBase.updateUserServers(memberToRemove, userServers);

        info.put("process", "serverPanel");
        info.put("operation", "Member removed successfully.");
    }

    private void membersActiveStatus() {
        ArrayList<String> members = serverDataBase.getServerUsers().get(info.getString("serverName"));
        JSONArray activeStatus = new JSONArray();

        if (members == null) {
            members = new ArrayList<>();
            serverDataBase.updateServerUsers(info.getString("serverName"), members);
        }

        for (String user : members) {
            if (!user.equals(info.getString("userName"))) {
                if (data.isOnline(user)) {
                    activeStatus.put(user + "      (Online)");
                } else if (!data.isOnline(user)) {
                    activeStatus.put(user + "      (Offline)");
                }
            }
        }

        info.put("activeStatus", activeStatus);
    }

    private void myServers() {
        String userName = info.getString("userName");
        JSONArray servers = new JSONArray();
        ArrayList<Server> servers1 = serverDataBase.getUserServers().get(userName);

        if (servers1 == null) {
            servers1 = new ArrayList<>();
            serverDataBase.updateUserServers(userName, servers1);
        }

        for (Server server : servers1) {
            servers.put(server.getName());
        }

        info.put("process", "chooseServer");
        info.put("servers", servers);
    }

    private void createRole() {
        Server server = serverDataBase.getServer(info.getString("serverName"));

        String roleName = info.getString("roleName");
        JSONArray permissions = info.getJSONArray("permissions");
        ArrayList<Permission> permissionArrayList = new ArrayList<>();

        Permission permission1;

        for (Object o : permissions) {
            assert false;
            permission1 = getPermission((String) o);
            permissionArrayList.add(permission1);
        }

        info.remove("roleName");
        info.remove("permissions");

        Role role = new Role(roleName, permissionArrayList);
        server.addRole(role);

        serverDataBase.updateServers(server);

        info.put("process", "serverPanel");
        info.put("operation", "Role Added successfully.");
    }

    private void serverPanel() {
        Role role = serverDataBase.getServer(info.getString("serverName")).getUserRoles(info.getString("userName"));

        info.put("role", role.getName());
        info.put("method", "loggedIn");
        info.put("process", "serverPanel");
    }

    private void applyMemberAdd() {
        String friendToAdd = info.getString("friendToAdd");
        String serverName = info.getString("serverName");
        String role = info.getString("role");

        HashMap<String, ArrayList<String>> serverUsers = serverDataBase.getServerUsers();
        HashMap<String, ArrayList<Server>> userServers = serverDataBase.getUserServers();

        //if hashmaps are null
        serverUsers.computeIfAbsent(serverName, k -> new ArrayList<>());
        userServers.computeIfAbsent(friendToAdd, k -> new ArrayList<>());

        ArrayList<String> serverMembers = serverDataBase.getServerUsers().get(serverName);
        ArrayList<Server> myFriendsServers = serverDataBase.loadUserServers().get(friendToAdd);

        if (serverMembers == null) {
            serverMembers = new ArrayList<>();
        }
        if (myFriendsServers == null) {
            myFriendsServers = new ArrayList<>();
        }

        serverMembers.add(friendToAdd);

        Server server = serverDataBase.getServer(serverName);
        server.setDefaultRole(friendToAdd);
        myFriendsServers.add(server);

        serverDataBase.updateServers(server);
        serverDataBase.updateServerUsers(serverName, serverMembers);
        serverDataBase.updateUserServers(friendToAdd, myFriendsServers);

        info.remove("friendToAdd");
        info.put("process", "serverPanel");
    }

    private void friendsToAdd() {
        ArrayList<String> serverUsers = serverDataBase.getServerUsers().get(info.getString("serverName"));
        ArrayList<DiscordFriend> friends = dataBase.getFriendsMap().get(info.getString("userName"));
        JSONArray friendsToAdd = new JSONArray();

        Server server = serverDataBase.getServer(info.getString("serverName"));
        ArrayList<Role> roles = server.getRoles();
        JSONArray rolesJsonArray = new JSONArray();

        for (Role role : roles) {
            rolesJsonArray.put(role.getName());
        }

        if (serverUsers == null) {
            serverUsers = new ArrayList<>();
        }

        for (DiscordFriend discordFriend : friends) {
            if (!serverUsers.contains(discordFriend.getUserName())) {
                friendsToAdd.put(discordFriend.getUserName());
            }
        }

        info.put("roles", rolesJsonArray);
        info.put("process", "selectFriendToAddServer");
        info.put("friends", friendsToAdd);
    }

}

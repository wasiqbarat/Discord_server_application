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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                case "channelChat" -> channelChat();
                case "showPinnedMessage" -> getPinnedMessage();
                case "reactToMessage" -> reactToMessage();
                case "applyReaction" -> applyReaction();
                case "applyPinMessage" -> applyPinMessage();
            }
            info.put("method", "loggedIn");

            parseMessageToJsonAndSendToClient(info);
        } catch (Exception e) {
            e.printStackTrace();
            parseErrorToJsonAndSendToClient(e);
        }
    }

    private void applyPinMessage() {
        String messageForPin = info.getString("messageForPin");
        String channelName = info.getString("channelName");
        String serverName = info.getString("serverName");

        ArrayList<Channel> channels = serverDataBase.getServerChannels().get(serverName);
        if (channels == null) {
            channels = new ArrayList<>();
        }

        for (Channel channel : channels) {
            if (channel.getName().equals(channelName)) {
                channel.setPinnedMessage(messageForPin);
                break;
            }
        }
        serverDataBase.updateServerChannels(serverName, channels);

        info.remove("messageForPin");

        info.put("method", "loggedIn");
        info.put("process", "channelPanel");
    }

    private void applyReaction() {
        String message = info.getString("messageForReaction");
        String channelName = info.getString("channelName");

        String content = message.split(" ")[1];

        HashMap<String, ArrayList<Message>> channelMessagesMap = serverDataBase.getChannelMessages();

        ArrayList<Message> messages = channelMessagesMap.get(channelName);

        String jsonReaction = info.getString("reaction");

        Reaction reaction = Reaction.getReaction(jsonReaction);

        for (Message message1 : messages) {
            if (message1.getContent().equals(content)) {
                message1.addReaction(reaction);
                break;
            }
        }

        channelMessagesMap.put(channelName, messages);
        serverDataBase.updateChannelMessages(channelMessagesMap);

        info.put("method", "loggedIn");
        info.put("process", "channelPanel");
    }

    private void reactToMessage() {
        chatHistoryAccess("reactToMessage");
    }

    /**
     * authenticate if a member is admin or not
     * @throws PermissionException if user is not admin
     */
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

                }
                case "renameServer" -> {
                    //not completed
                }
                case "chatHistoryAccess", "pinMessage" -> {
                    chatHistoryAccess(action);
                }

            }
        } else {
            throw new PermissionException();
        }
    }

    private void chatHistoryAccess(String action) {
        String channelName = info.getString("channelName");

        ArrayList<Message> messages = serverDataBase.getChannelMessages().get(channelName);
        if ( messages == null) {
            messages = new ArrayList<>();
        }

        JSONArray messagesJson = new JSONArray();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");

        for (Message message : messages) {
            messagesJson.put(message.getSender() + ": " + message.getContent() + "    (" + message.getDateTime().format(formatter) + ")  " + message.reactionsToString());
        }

        info.put("messages" , messagesJson);
        switch (action) {
            case "chatHistoryAccess" -> info.put("process", "displayChannelMessages");
            case "reactToMessage" -> info.put("process", "reactToMessage");
            case "pinMessage" -> info.put("process", "pinMessage");
        }

    }

    private void getPinnedMessage() {
        String channelName = info.getString("channelName");
        String serverName = info.getString("serverName");
        ArrayList<Channel> channels = serverDataBase.getServerChannels().get(serverName);

        String pinnedMessage = "empty";
        for (Channel channel : channels) {
            if (channel.getName().equals(channelName)) {
                pinnedMessage = channel.getPinnedMessage();
                if (pinnedMessage == null || pinnedMessage.isEmpty()) {
                    channel.setPinnedMessage("empty");
                    pinnedMessage = "empty";
                }
                break;
            }
        }

        info.put("pinnedMessage", pinnedMessage);
        info.put("process", "displayPinnedMessage");
    }

    private void channelChat() throws IOException {
        String userName = info.getString("userName");
        String channelName = info.getString("channelName");
        String serverName = info.getString("serverName");
        String messageContent = info.getString("message");

        Message message = new Message(messageContent, userName, channelName, LocalDateTime.now());
        HashMap<String, ArrayList<Message>> channelMessages = serverDataBase.getChannelMessages();

        if (channelMessages.get(channelName) == null) {
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(message);
            channelMessages.put(channelName, messages);
        }else {
            ArrayList<Message> messages = channelMessages.get(channelName);
            messages.add(message);
            channelMessages.put(channelName, messages);
        }
        serverDataBase.updateChannelMessages(channelMessages);

        ArrayList<String> serverUsers = serverDataBase.getServerUsers().get(serverName);

        if ( serverUsers == null) {
            serverUsers = new ArrayList<>();
            serverDataBase.updateServerUsers(serverName, serverUsers);
        }


        for (String user : serverUsers) {
            if (data.isOnline(user) && !user.equals(userName)) {
                DataOutputStream dataOutputStream = new DataOutputStream(data.getSocket(user).getOutputStream());

                JSONObject dataToFriend = new JSONObject();

                dataToFriend.put("exception", false);
                dataToFriend.put("method", "channelMessage");
                dataToFriend.put("destination", user);

                dataToFriend.put("message", info.getString("message"));

                dataToFriend.put("sender", userName);
                dataToFriend.put("channelName", channelName);

                dataOutputStream.writeUTF(dataToFriend.toString());
            }
        }
        info.remove("message");

        info.put("method", "loggedIn");
        info.put("process", "channelPanel");
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

        ArrayList<Server> userServers = serverDataBase.getUserServers().get(userName);
        if (userServers == null) {
            userServers = new ArrayList<>();
        }

        userServers.add(server);

        serverDataBase.updateUserServers(userName, userServers);

        serverDataBase.updateServerChannels(serverName, new ArrayList<>());

        info.put("process", "serverPanel");

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
        serverDataBase.reloadServers();

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
        String userName = info.getString("userName");

        ArrayList<String> serverMembers = serverDataBase.getServerUsers().get(serverName);
        ArrayList<Server> myFriendsServers = serverDataBase.loadUserServers().get(friendToAdd);

        if (serverMembers == null) {
            System.err.println("is null");
            serverMembers = new ArrayList<>();
        }
        if (myFriendsServers == null) {
            System.err.println("is null");
            myFriendsServers = new ArrayList<>();
        }

        serverMembers.add(friendToAdd);


        Server server = serverDataBase.getServer(serverName);

        server.setRoleForUser(friendToAdd, role);

        myFriendsServers.add(server);

        serverDataBase.updateServers(server);
        serverDataBase.updateServerUsers(serverName, serverMembers);
        serverDataBase.updateUserServers(friendToAdd, myFriendsServers);

        info.remove("friendToAdd");
        info.put("process", "serverPanel");
    }

    private void friendsToAdd() {
        String serverName = info.getString("serverName");
        String userName = info.getString("userName");

        ArrayList<String> serverUsers = serverDataBase.getServerUsers().get(serverName);
        ArrayList<DiscordFriend> friends = dataBase.getFriendsMap().get(userName);

        JSONArray friendsToAdd = new JSONArray();

        Server server = serverDataBase.getServer(serverName);
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

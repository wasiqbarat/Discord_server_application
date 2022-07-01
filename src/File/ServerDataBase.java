package File;

import DiscordClasses.Channel;
import DiscordClasses.Message;
import DiscordClasses.Server;
import Exceptions.SeverNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * ServerDataBase stores and manages all Discord Servers data.
 *
 * @author wasiq
 * @see Server
 */
public class ServerDataBase {
    private static ServerDataBase serverDataBase = null;

    private ArrayList<Server> servers;
    private HashMap<String, ArrayList<Channel>> serverChannels;//this hashmap maps each server to its channels
    private HashMap<String, ArrayList<String>> channelUsers;//this hashmap maps each channel to its users
    private HashMap<String, ArrayList<String>> serverUsers;//this hashmap maps each server to its users
    private HashMap<String, ArrayList<Message>> channelMessages;//this hashmap maps each channel to its messages
    private HashMap<String, ArrayList<Server>> userServers;// this hashmap maps each user to its servers

    public ServerDataBase() {
        serverChannels = loadServerChannels();
        servers = loadServers();
        channelMessages = loadChannelMessages();
        userServers = loadUserServers();

        channelUsers = loadUsers("channel");
        serverUsers = loadUsers("server");
    }

    public void updateChannelMessages(String channelName, Message message) { //most be completed

    }

    public void updateChannelUsers(String channelName, ArrayList<String> users) {
        channelUsers.put(channelName, users);

        File file = new File("DataBase/channelUsers.bin");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(channelUsers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateServerChannels(String serverName, ArrayList<Channel> channels) {
        serverChannels.put(serverName, channels);

        File file = new File("DataBase/serverChannels.bin");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(serverChannels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server getServer(String serverName) {
        if (servers == null) {
            servers = loadServers();
        }

        for (Server server : servers) {
            if (server.getName().equals(serverName)) {
                return server;
            }
        }

        return null;
    }

    public HashMap<String, ArrayList<Server>> loadUserServers() {
        File file = new File("DataBase/userServers.bin");
        HashMap<String, ArrayList<Server>> userServers = null;

        if (!file.exists()) {
            userServers = new HashMap<>();
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(userServers);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return userServers;
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            userServers = (HashMap<String, ArrayList<Server>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userServers;
    }

    public void updateUserServers(String user, ArrayList<Server> servers) {
        userServers.computeIfAbsent(user, k -> new ArrayList<>());
        userServers.computeIfAbsent(user, k -> new ArrayList<>());
        userServers.put(user, servers);

        File file = new File("DataBase/userServers.bin");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(userServers);
        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

    public void updateServerUsers(String serverName, ArrayList<String> users) {
        serverUsers.put(serverName, users);

        File file = new File("DataBase/serverUsers.bin");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(channelUsers);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public HashMap<String, ArrayList<String>> loadUsers(String channelOrServer) {
        File file = new File("DataBase/" + channelOrServer + "Users.bin");
        HashMap<String, ArrayList<String>> users = null;

        if (!file.exists()) {
            users = new HashMap<>();
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(users);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return users;
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            users = (HashMap<String, ArrayList<String>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    public HashMap<String, ArrayList<Message>> loadChannelMessages() {
        File file = new File("DataBase/channelMessages.bin");
        HashMap<String, ArrayList<Message>> messages = null;

        if (!file.exists()) {
            messages = new HashMap<>();
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(messages);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return messages;
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            messages = (HashMap<String, ArrayList<Message>>) inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public ArrayList<Server> loadServers() {
        File file = new File("DataBase/servers.bin");

        ArrayList<Server> servers = null;
        if (!file.exists()) {
            servers = new ArrayList<>();
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(servers);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return servers;
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            servers = (ArrayList<Server>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return servers;
    }

    public ArrayList<Server> getServers() {
        if (servers == null) {
            servers = loadServers();
        }
        return servers;
    }

    public HashMap<String, ArrayList<Channel>> getServerChannels() {
        if (serverChannels == null) {
            serverChannels = loadServerChannels();
        }
        return serverChannels;
    }

    public HashMap<String, ArrayList<Message>> getChannelMessages() {
        if (channelMessages == null) {
            channelMessages = loadChannelMessages();
        }
        return channelMessages;
    }

    public HashMap<String, ArrayList<String>> getChannelUsers() {
        if (channelUsers == null) {
            channelUsers = loadUsers("channel");
        }
        return channelUsers;
    }

    public HashMap<String, ArrayList<String>> getServerUsers() {
        if (serverUsers == null) {
            serverUsers = loadUsers("server");
        }
        return serverUsers;
    }

    public HashMap<String, ArrayList<Server>> getUserServers() {
        if (userServers == null) {
            userServers = loadUserServers();
        }
        return userServers;
    }

    public void updateServers(Server server) {
        Iterator<Server> iterator = servers.iterator();
        while (iterator.hasNext()) {
            Server server1 = (Server) iterator.next();
            if (server.isEqual(server1)) {
                iterator.remove();
                servers.add(server);
                break;
            }
        }

        File file = new File("DataBase/servers.bin");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(servers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, ArrayList<Channel>> loadServerChannels() {
        File file = new File("DataBase/serverChannels.bin");

        HashMap<String, ArrayList<Channel>> serverChannels = null;

        if (!file.exists()) {
            serverChannels = new HashMap<>();
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(serverChannels);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return serverChannels;
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            serverChannels = (HashMap<String, ArrayList<Channel>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return serverChannels;
    }

    public static ServerDataBase getInstance() {   //singleton class structure
        if (serverDataBase == null) {
            serverDataBase = new ServerDataBase();
        }
        return serverDataBase;
    }

}

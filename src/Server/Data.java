package Server;

import java.net.Socket;
import java.util.HashMap;

/**
 * this method stores data about server.
 * like online users
 */
public class Data {
    private static Data data;

    private static HashMap<String, ClientHandler> onlineClients;

    private Data() {
        onlineClients = new HashMap<>();
    }

    //singleton class structure
    public static Data getInstance() {
        if (data == null)
            data = new Data();
        return data;
    }

    public void addOnlineUser(String user, ClientHandler client) {
        onlineClients.put(user, client);
    }

    public void deleteOfflineUser(ClientHandler clientHandler) {
        onlineClients.remove(clientHandler.getUserName());
    }

    public boolean isOnline(String userName) {
        return onlineClients.containsKey(userName);
    }

    public Socket getSocket(String userName) {
        return onlineClients.get(userName).getSocket();
    }


}
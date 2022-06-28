package Server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class Data {
    private static Data data;

    private static HashMap<String, ClientHandler> onlineClients;

    private Data() {
        onlineClients = new HashMap<>();
    }

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
        if (onlineClients.containsKey(userName)) {
            return true;
        } else return false;
    }

    public Socket getSocket(String userName) {
        return onlineClients.get(userName).getSocket();
    }


}
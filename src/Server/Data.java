package Server;


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
        System.out.println("User added: " + client.getUserName());
    }

    public void deleteOfflineUser(ClientHandler clientHandler) {
        onlineClients.remove(clientHandler.getUserName());
        System.out.println("User deleted: " + clientHandler.getUserName());
    }

    public boolean isOnline(String userName) {
        if (onlineClients.containsKey(userName)) {
            return true;
        } else return false;
    }
}

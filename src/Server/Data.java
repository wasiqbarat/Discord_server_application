package Server;


import java.util.HashMap;

public class Data {
    private static Data data;
    private static HashMap<String, ClientHandler> clients = new HashMap<>();

    private Data() {
        data = new Data();
    }

    public static Data getInstance() {
        if (data == null)
            data = new Data();
        return data;
    }

    public void addUser(String user, ClientHandler client) {
        clients.put(user, client);
    }

    public void deleteUser(String userName) {
        clients.remove(userName);
    }
}

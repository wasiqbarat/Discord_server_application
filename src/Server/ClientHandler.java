package Server;

import RespondingCommand.Respond;
import RespondingCommand.RespondFactory;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

public class ClientHandler implements Runnable {
    private String userName;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private final Data serverData = Data.getInstance();

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

        } catch (Exception e) {
            closeEveryThing(socket, dataOutputStream, dataInputStream);
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String dataFromClient = dataInputStream.readUTF();

                System.out.println("_____________from client_________________");
                System.out.println(dataFromClient);
                System.out.println("_________________________________________");

                JSONObject json = new JSONObject(dataFromClient);
                userName = json.getString("userName");
                serverData.addOnlineUser(userName, this);

                RespondFactory factory = RespondFactory.getInstance();
                Respond respond = factory.getRespond(json, socket);
                respond.handle();

            } catch (Exception e) {
                serverData.deleteOfflineUser(this);
                System.out.println("Nafar rasft");
                System.out.println(e.getMessage());
                closeEveryThing(socket, dataOutputStream, dataInputStream);
                e.printStackTrace();
                break;
            }
        }
    }


    public String getUserName() {
        return userName;
    }

    public void closeEveryThing(Socket socket, DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
        try {
            if (socket != null) {
                socket.close();
                serverData.deleteOfflineUser(this);
            }
            if (dataInputStream != null) {
                dataInputStream.close();
            }

            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

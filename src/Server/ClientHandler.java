package Server;

import RespondingCommand.Respond;
import RespondingCommand.RespondFactory;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

public class ClientHandler implements Runnable {
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private final Data data = Data.getInstance();
    private String userName;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

        } catch (Exception e) {
            //this method is for closing streams and socket
            closeEveryThing(socket, dataOutputStream, dataInputStream);
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String dataFromClient = dataInputStream.readUTF();
                System.out.println(dataFromClient);

                JSONObject json = new JSONObject(dataFromClient);
                userName = json.getString("userName");
                data.addUser(userName, this);

                RespondFactory factory = RespondFactory.getInstance();
                Respond respond = factory.getRespond(json, socket);
                respond.handle();
            } catch (IOException e) {
                data.deleteUser(this);
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
                data.deleteUser(this);
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

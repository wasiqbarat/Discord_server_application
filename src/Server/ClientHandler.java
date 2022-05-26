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
    private Data data = Data.getInstance();
    private String userName;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            userName = dataInputStream.readUTF();
            data.addUser(userName, this);

        } catch (Exception e) {
            //this method is for closing streams and socket
            closeEveryThing(socket, dataOutputStream, dataInputStream);
        }
    }


    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                JSONObject json = new JSONObject(dataInputStream.readUTF());
                RespondFactory factory = RespondFactory.getInstance();
                Respond respond = factory.getRespond(json, socket);
                respond.handle();
            } catch (IOException e) {
                data.deleteUser(this.userName);
                closeEveryThing(socket, dataOutputStream, dataInputStream);
                e.printStackTrace();
                break;
            }
        }
    }


    public void closeEveryThing(Socket socket, DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
        try {
            if (socket != null) {
                socket.close();
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

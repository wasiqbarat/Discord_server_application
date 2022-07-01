package RespondingCommand;

import Classes.DiscordFriend;
import DiscordClasses.Message;
import Exceptions.BlockedUserException;
import File.DataBase;
import Server.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PrivateChatRespond extends Respond {
    private final DataBase dataBase;
    private final Data data;

    public PrivateChatRespond(Socket socket, JSONObject info) {
        super(socket, info);
        dataBase = DataBase.getInstance();
        data = Data.getInstance();
    }

    @Override
    public void handle() throws Exception {
        switch (info.getString("process")) {
            case "newChat" -> newChat();
            case "myChats" -> myChats();
            case "chatAuthentication" -> chatAuthentication();
            case "chatting" -> chatting();
            case "getMessagesWith" -> getMessagesWith();
        }

    }


    private void getMessagesWith() {
        ArrayList<Message> messagesDataBase = dataBase.getMessagesMap().get(info.getString("userName"));

        String userName = info.getString("userName");
        String messagesWith = info.getString("messagesWith");

        if (messagesDataBase == null) {
            messagesDataBase = new ArrayList<>();
        }

        JSONArray messages = new JSONArray();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");

        for (Message message : messagesDataBase) {
            if (message.getSender().equals(messagesWith) || message.getReceiver().equals(messagesWith) ||
                    message.getSender().equals(userName) || message.getReceiver().equals(userName)        )
            {

                messages.put(message.getSender() + ": " + message.getContent() + "       " +
                        "(" + message.getDateTime().format(formatter)+ ")");
            }

        }

        info.put("messages", messages);
        info.put("method", "loggedIn");
        info.put("process", "messagesDisplay");
        info.put("exception", false);

        parseMessageToJsonAndSendToClient(info);
    }

    private void chatting() throws IOException {
        String friendToChat = info.getString("friendToChat").split(" ")[0];
        String senderUserName = info.getString("userName");

        ArrayList<Message> senderMessages = dataBase.getMessagesMap().get(senderUserName);
        ArrayList<Message> receiverMessages = dataBase.getMessagesMap().get(friendToChat);

        if (senderMessages == null) {
            senderMessages = new ArrayList<>();
        }
        if (receiverMessages == null) {
            receiverMessages = new ArrayList<>();
        }

        Message message = new Message(info.getString("message"), senderUserName, friendToChat, LocalDateTime.now());

        senderMessages.add(message);
        receiverMessages.add(message);


        //see if friend is online or not
        if (data.isOnline(friendToChat)) {
            DataOutputStream dataOutputStream = new DataOutputStream(data.getSocket(friendToChat).getOutputStream());

            JSONObject dataToFriend = new JSONObject();

            dataToFriend.put("exception", false);
            dataToFriend.put("method", "newMessage");
            dataToFriend.put("friendToChat", info.getString("userName").split(" ")[0]);
            dataToFriend.put("message", info.getString("message"));

            dataToFriend.put("sender", info.getString("userName"));

            dataToFriend.put("userName", info.getString("friendToChat").split(" ")[0]);

            dataOutputStream.writeUTF(dataToFriend.toString());

        }


        try {
            info.getBoolean("reply");
        } catch (JSONException e) {
            info.put("reply", false);
        }

        //is message was a reply there is no need to any action
        if (!info.getBoolean("reply")) {
            info.remove("message");
            info.put("method", "loggedIn");
            info.put("process", "chatting");
            info.put("isFirstMessage", false);

            dataBase.updateMessagesMap(senderUserName, senderMessages);
            dataBase.updateMessagesMap(friendToChat, receiverMessages);

            parseMessageToJsonAndSendToClient(info);
        }

    }

    private void myChats() {
        ArrayList<String> myMessages = new ArrayList<>();

        dataBase.getMessagesMap().forEach((key, value) -> {
            myMessages.add(key);
        });

        JSONArray messagesJsonArray = new JSONArray();

        for (String userName : myMessages) {
            if (!info.getString("userName").equals(userName)) {
                messagesJsonArray.put(userName);
            }
        }

        info.put("method", "loggedIn");
        info.put("myChats", messagesJsonArray);

        parseMessageToJsonAndSendToClient(info);
    }


    private void newChat() {
        ArrayList<DiscordFriend> friends = dataBase.getFriendsMap().get(info.getString("userName"));
        JSONArray friendsJsonArray = new JSONArray();

        if (friends == null) {
            friends = new ArrayList<>();
        }

        for (DiscordFriend discordFriend : friends) {
            if (data.isOnline(discordFriend.getUserName())) {
                friendsJsonArray.put(discordFriend.getUserName() + "    " + "(Online)");
            } else {
                friendsJsonArray.put(discordFriend.getUserName() + "    " + "(Offline)");
            }
        }

        if (friendsJsonArray.isEmpty()) {
            info.put("method", "loggedIn");
            info.put("process", "action");
            parseMessageToJsonAndSendToClient(info);
        } else {
            info.put("friends", friendsJsonArray);
            info.put("method", "loggedIn");
            info.put("process", "newChat");
            parseMessageToJsonAndSendToClient(info);
        }
    }


    /**
     * chatAuthentication method authenticate that
     * user is blocked by its friend or not
     * if blocked, throws exception
     */
    public void chatAuthentication() {
        String friendToChat = info.getString("friendToChat").split(" ")[0];

        ArrayList<DiscordFriend> friendsToChatFriends = dataBase.getFriendsMap().get(friendToChat);

        if (friendsToChatFriends == null) {
            friendsToChatFriends = new ArrayList<>();
        }


        try {
            for (DiscordFriend discordFriend : friendsToChatFriends) {
                if (discordFriend.getUserName().equals(info.getString("userName"))) {
                    if (discordFriend.getBlockStatus()) {
                        throw new BlockedUserException();
                    }
                }
            }

            info.put("method", "loggedIn");
            info.put("process", "chatting");
            info.put("isFirstMessage", true);

            parseMessageToJsonAndSendToClient(info);

        } catch (Exception e) {
            parseErrorToJsonAndSendToClient(e);
        }

    }

}

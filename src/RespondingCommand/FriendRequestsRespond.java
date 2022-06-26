package RespondingCommand;

import Classes.DiscordFriend;
import Classes.Person;
import DiscordClasses.FriendRequest;
import File.DataBase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FriendRequestsRespond extends Respond {
    private final DataBase dataBase;

    public FriendRequestsRespond(Socket socket, JSONObject info) {
        super(socket, info);
        dataBase = DataBase.getInstance();
    }

    @Override
    public void handle() {
        switch (info.getString("process")) {
            case "myFriendRequests" -> myFriendRequests();
            case "sendFriendRequest" -> sendFriendRequest();
            case "applyRequests" -> applyRequests();
            case "confirmOrApproveFriendRequests" -> confirmOrApproveFriendRequests();
        }
    }

    private void confirmOrApproveFriendRequests() {
        JSONArray confirmed = info.getJSONArray("confirmedRequests");
        JSONArray approved = info.getJSONArray("approvedRequests");

        Iterator iterator1 = confirmed.iterator();

        ArrayList<DiscordFriend> discordFriends = dataBase.getFriendsMap().get(info.getString("userName"));
        ArrayList<FriendRequest> friendRequestsArray = dataBase.getFriendRequestMap().get(info.getString("userName"));
        Iterator iterator = friendRequestsArray.iterator();

        if (discordFriends == null) {
            discordFriends = new ArrayList<>();
        }

        Person user;
        while (iterator1.hasNext()) {
            String userToAdd = (String) iterator1.next();

            String userName = userToAdd.split(" ")[0];
            File userFile = new File("Files/Users/" + userName + ".bin");

            try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(userFile))) {
                user = (Person) inputStream.readObject();

                discordFriends.add(new DiscordFriend(user.getUserName(), user.getEmail(), user.getPhoneNumber()));

                //remove friend request from friend requests
                while (iterator.hasNext()) {
                    FriendRequest friendRequest = (FriendRequest) iterator.next();

                    if (friendRequest.getSender().equals(user.getUserName())) {
                        iterator.remove();
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        dataBase.getFriendsMap().put(info.getString("userName") , discordFriends);
        dataBase.updateFriendsMap(info.getString("userName"), discordFriends);

        info.put("process", "loggedIn");
        info.put("method", "loggedIn");
        info.remove("confirmedRequests");
        info.remove("approvedRequests");

        parseMessageToJsonAndSendToClient(info);
    }


    private void applyRequests() {
        JSONArray friends = info.getJSONArray("friendsToAdd");

        HashMap<String, ArrayList<FriendRequest>> friendRequests = dataBase.getFriendRequestMap();

        for (Object friend : friends) {
            String tmp = (String) friend;

            ArrayList<FriendRequest> friendRequestsArray = friendRequests.get(tmp);

            if (friendRequestsArray == null) {
                friendRequestsArray = new ArrayList<>();
            }

            try {
                friendRequestsArray.add(new FriendRequest(info.getString("userName"), tmp, LocalDateTime.now()));
                friendRequests.put(tmp, friendRequestsArray);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Friend request respond exception line 44");
            }
        }

        dataBase.updateFriendRequestMap(friendRequests);

        info.put("process", "loggedIn");
        info.put("method", "loggedIn");
        info.remove("friendsToAdd");

        parseMessageToJsonAndSendToClient(info);
    }


    private void sendFriendRequest() {
        ArrayList<String> usersList = dataBase.getUsersList();
        JSONArray users = new JSONArray();

        for (String userName : usersList) {
            if (! (info.getString("userName")).equals(userName) ) {
                users.put(userName);
            }
        }

        info.put("users", users);
        info.put("method", "loggedIn");
        parseMessageToJsonAndSendToClient(info);
    }

    private void myFriendRequests() {
        HashMap<String, ArrayList<FriendRequest>> friendRequestsMap = dataBase.getFriendRequestMap();

        friendRequestsMap.forEach((key, value) -> {
            System.out.println(key);
        });

        String userName = info.getString("userName");

        ArrayList<FriendRequest> friendRequests = friendRequestsMap.get(userName);

        JSONArray friendRequestsJsonArray = new JSONArray();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");

        for (FriendRequest friendRequest : friendRequests) {
            friendRequestsJsonArray.put(friendRequest.getSender() + "  " + friendRequest.getDateTime().format(formatter));
        }

        info.put("method", "loggedIn");
        info.put("friendRequests", friendRequestsJsonArray);

        parseMessageToJsonAndSendToClient(info);
    }

}

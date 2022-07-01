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

        //getting my friends list from database and manipulate it
        ArrayList<DiscordFriend> discordFriends = dataBase.getFriendsMap().get(info.getString("userName"));
        ArrayList<FriendRequest> friendRequestsArray = dataBase.getFriendRequestMap().get(info.getString("userName"));

        if (friendRequestsArray == null) {
            friendRequestsArray = new ArrayList<>();
        }
        if (discordFriends == null) {
            discordFriends = new ArrayList<>();
        }


        Iterator iterator = friendRequestsArray.iterator();
        Person user;
        Person receiver = null;

        ArrayList<DiscordFriend> senderFriends = null;
        String senderUserName = null;
        while (iterator1.hasNext()) {

            String senderUserNameWithData = (String) iterator1.next();

            senderUserName = senderUserNameWithData.split(" ")[0];

            File userFile = new File("Files/Users/" + senderUserName + ".bin");//we need to sender's email and phone

            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(userFile))) {
                user = (Person) inputStream.readObject();

                //add sender to my friends list
                discordFriends.add(new DiscordFriend(user.getUserName(), user.getEmail(), user.getPhoneNumber()));

                //remove friend request from friend requests
                while (iterator.hasNext()) {
                    FriendRequest friendRequest = (FriendRequest) iterator.next();

                    if (friendRequest.getSender().equals(user.getUserName())) {
                        iterator.remove();
                    }

                }


                //Add myself as friend to someone who has sent a friend The request
                //So I need my email and phone number.
                File friendRequestReceiver = new File("Files/users/" + info.getString("userName") + ".bin");

                try (ObjectInputStream inputStream1 = new ObjectInputStream(new FileInputStream(friendRequestReceiver))) {
                    receiver = (Person) inputStream1.readObject();

                    senderFriends = dataBase.getFriendsMap().get(senderUserName);
                    if (senderFriends == null) {
                        senderFriends = new ArrayList<>();
                    }

                    senderFriends.add(new DiscordFriend(receiver.getUserName(), receiver.getEmail(), receiver.getPhoneNumber()));
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        //update database
        dataBase.getFriendsMap().put(info.getString("userName"), discordFriends);
        dataBase.updateFriendsMap(info.getString("userName"), discordFriends);

        dataBase.getFriendsMap().put(senderUserName, senderFriends);
        dataBase.updateFriendsMap(senderUserName, senderFriends);

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

        ArrayList<DiscordFriend> friends = dataBase.getFriendsMap().get(info.getString("userName"));
        ArrayList<String> friendsArray = new ArrayList<>();

        if (friends == null) {
            friends = new ArrayList<>();
        }

        for (DiscordFriend discordFriend : friends) {
            friendsArray.add(discordFriend.getUserName());
        }


        for (String userName : usersList) {
            //checks if userName is friend or not, if not; you can send friend request
            if (!(info.getString("userName")).equals(userName) && !friendsArray.contains(userName) ) {
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

        if (friendRequests == null) {
            friendRequests = new ArrayList<>();
        }

        for (FriendRequest friendRequest : friendRequests) {
            friendRequestsJsonArray.put(friendRequest.getSender() + "  " + friendRequest.getDateTime().format(formatter));
        }

        info.put("method", "loggedIn");
        info.put("friendRequests", friendRequestsJsonArray);

        parseMessageToJsonAndSendToClient(info);
    }
}

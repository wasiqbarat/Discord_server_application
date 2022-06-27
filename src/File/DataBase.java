package File;

import Classes.DiscordFriend;
import DiscordClasses.FriendRequest;
import DiscordClasses.Message;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBase {
    private static DataBase dataBase = null;

    private final DiscordFile fileManager;
    private ArrayList<String> usersList;
    private HashMap<String, ArrayList<DiscordFriend>> friendsMap;

    private final HashMap<String, ArrayList<DiscordFriend>> blockedFriendsMap;
    private final HashMap<String, ArrayList<Message>> messagesMap;
    private HashMap<String, ArrayList<FriendRequest>> friendRequestMap;

    public void setFriendRequestMap(HashMap<String, ArrayList<FriendRequest>> friendRequestMap) {
        this.friendRequestMap = friendRequestMap;
    }

    private DataBase() {
        fileManager = DiscordFile.getInstance();
        usersList = fileManager.loadUsersList();
        friendsMap = fileManager.loadFriends();
        blockedFriendsMap = fileManager.loadBlockedFriends();
        messagesMap = fileManager.loadMessagesMap();
        friendRequestMap = fileManager.loadFriendRequestMap();

    }


    //singleton structure
    public static DataBase getInstance() {
        if (dataBase == null) {
            dataBase = new DataBase();
        }
        return dataBase;
    }


    public HashMap<String, ArrayList<DiscordFriend>> getFriendsMap() {
        if (friendsMap == null) {
            friendsMap =  fileManager.loadFriends();
        }

        friendRequestMap.forEach((key, value) -> {
            if (value == null) {
                value = new ArrayList<>();
            }
        });
        return friendsMap;
    }

    public ArrayList<String> getUsersList() {
        if (usersList == null) {
            usersList =  fileManager.loadUsersList();
        }
        return usersList;
    }

    public HashMap<String, ArrayList<DiscordFriend>> getBlockedFriendsMap() {
        return blockedFriendsMap;
    }

    public HashMap<String, ArrayList<Message>> getMessagesMap() {
        return messagesMap;
    }

    public HashMap<String, ArrayList<FriendRequest>> getFriendRequestMap() {

        if (friendRequestMap == null) {
            friendRequestMap = fileManager.loadFriendRequestMap();
        }

        friendRequestMap.forEach((key, value) -> {
            if (value == null) {
                value = new ArrayList<>();
            }
        });

        return friendRequestMap;
    }


    public void updateFriendsMap(String userName, ArrayList<DiscordFriend> discordFriends) {
        friendsMap.put(userName, discordFriends);
        fileManager.updateFriendsMap(friendsMap);
    }

    public void updateFriendRequestMap(HashMap<String, ArrayList<FriendRequest>> friendRequests) {
        setFriendRequestMap(friendRequests);
        fileManager.updateFriendRequestMap(friendRequestMap);
    }

}

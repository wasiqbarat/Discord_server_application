package File;

import Classes.DiscordFriend;
import Classes.Person;
import DiscordClasses.FriendRequest;
import DiscordClasses.Message;
import Exceptions.UserInvalid;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * DataBase is a simulation of database.
 * this class stores discord datas
 *
 * @see DiscordFile
 */
public class DataBase {
    private static DataBase dataBase = null;

    private final DiscordFile fileManager;
    private ArrayList<String> usersList;
    private HashMap<String, ArrayList<DiscordFriend>> friendsMap;
    private HashMap<String, ArrayList<FriendRequest>> friendRequestMap;
    private HashMap<String, ArrayList<Message>> messagesMap;

    private DataBase() {
        fileManager = DiscordFile.getInstance();
        usersList = fileManager.loadUsersList();
        friendsMap = fileManager.loadFriends();
        friendRequestMap = fileManager.loadFriendRequestMap();
        messagesMap = fileManager.loadMessagesMap();
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
            friendsMap = fileManager.loadFriends();
        }
        return friendsMap;
    }

    public ArrayList<String> getUsersList() {
        return usersList;
    }

    public HashMap<String, ArrayList<Message>> getMessagesMap() {
        if (messagesMap == null) {
            messagesMap = fileManager.loadMessagesMap();
        }
        return messagesMap;
    }

    public HashMap<String, ArrayList<FriendRequest>> getFriendRequestMap() {
        if (friendRequestMap == null) {
            friendRequestMap = fileManager.loadFriendRequestMap();
        }
        return friendRequestMap;
    }

    public void setFriendRequestMap(HashMap<String, ArrayList<FriendRequest>> friendRequestMap) {
        this.friendRequestMap = friendRequestMap;
    }


    public void updateFriendsMap(String userName, ArrayList<DiscordFriend> discordFriends) {
        friendsMap.put(userName, discordFriends);
        fileManager.updateFriendsMap(friendsMap);
    }

    public void updateFriendRequestMap(HashMap<String, ArrayList<FriendRequest>> friendRequests) {
        setFriendRequestMap(friendRequests);
        fileManager.updateFriendRequestMap(friendRequestMap);
    }

    public void updateMessagesMap(String userName, ArrayList<Message> messages) {
        messagesMap.put(userName, messages);
        fileManager.updateMessagesMap(messagesMap);
    }

    public Person getUser(String userName) throws UserInvalid {
        return fileManager.getUser(userName);
    }

    public void updateUser(Person person) throws Exception {
        fileManager.updateUser(person);
    }

}

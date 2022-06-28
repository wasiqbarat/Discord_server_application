package File;

import Classes.DiscordFriend;
import Classes.DiscordUser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import DiscordClasses.FriendRequest;
import DiscordClasses.Message;
import Exceptions.*;

public class DiscordFile {
    private static DiscordFile discordFile = null;

    //singleton class pattern
    private DiscordFile() {
    }

    public static DiscordFile getInstance() {
        if (discordFile == null) {
            discordFile = new DiscordFile();
        }
        return discordFile;
    }

    public void signUpUser(DiscordUser discordUser) throws Exception {
        File usersFolder = new File("Files/Users/");
        String[] usersList = usersFolder.list();

        if (usersList != null) {
            for (String user : usersList) {
                if (user.equals(discordUser.getUserName() + ".bin")) {
                    throw new userExistException();
                }
            }
        }

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Files/Users/" + discordUser.getUserName() + ".bin"))) {
            objectOutputStream.writeObject(discordUser);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void signIn(String userName, String password) throws Exception {
        File usersFolder = new File("Files/Users/");
        String[] usersList = usersFolder.list();

        try {
            DiscordUser discordUser = null;
            if (usersList != null) {
                for (String user : usersList) {
                    if (user.equals(userName + ".bin")) {
                        FileInputStream fileInputStream = new FileInputStream("Files/Users/" + user);
                        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                        discordUser = (DiscordUser) objectInputStream.readObject();

                        fileInputStream.close();
                        objectInputStream.close();
                    }
                }
            }

            if (discordUser == null || !discordUser.getPassword().equals(password)) {
                throw new userOrPasswordInvalidException();
            }


        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }


    public HashMap<String, ArrayList<DiscordFriend>> loadFriends() {
        File file = new File("DataBase/friendsListHashmap.bin");

        HashMap<String, ArrayList<DiscordFriend>> friendsListHashmap = null;

        if(!file.exists()) {
            friendsListHashmap = new HashMap<>();
            ArrayList<String> userList = loadUsersList();
            for (String user : userList) {
                ArrayList<DiscordFriend> arrayList = new ArrayList<>();
                friendsListHashmap.put(user, arrayList);
            }

            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                objectOutputStream.writeObject(friendsListHashmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return friendsListHashmap;
        }

        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))){
            friendsListHashmap = (HashMap<String, ArrayList<DiscordFriend>>) inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return friendsListHashmap;
    }


    public void updateFriendsMap(HashMap<String, ArrayList<DiscordFriend>> friendsMap) {
        File file = new File("DataBase/friendsListHashmap.bin");

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file) )){
            output.writeObject(friendsMap);
        } catch (IOException e) {
            System.err.println("error in update friends map");
            e.printStackTrace();
        }
    }

    //------------------------------------------
    public HashMap<String, ArrayList<FriendRequest>> loadFriendRequestMap() {
        File file = new File("DataBase/friendRequestMap.bin");

        HashMap<String, ArrayList<FriendRequest>> friendRequestMap = null;

        if( !file.exists() ) {
            friendRequestMap = new HashMap<>();
            ArrayList<String> usersList = loadUsersList();

            for (String user : usersList) {
                ArrayList<FriendRequest> arrayList = new ArrayList<>();
                friendRequestMap.put(user, arrayList);
            }

            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                objectOutputStream.writeObject(friendRequestMap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return friendRequestMap;
        }

        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))){
            friendRequestMap = (HashMap<String, ArrayList<FriendRequest>>) inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return friendRequestMap;
    }

    public void updateFriendRequestMap(HashMap<String, ArrayList<FriendRequest>> friendRequestMap) {
        File file = new File("DataBase/friendRequestMap.bin");
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(friendRequestMap);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public ArrayList<String> loadUsersList() {
        File usersFolder = new File("Files/Users/");
        String[] usersList1 = usersFolder.list();

        ArrayList<String> usersList = new ArrayList<>();
        for (String user : usersList1 ){
            usersList.add(user.split("\\.")[0]);
        }

        return usersList;
    }

    ///
    public HashMap<String, ArrayList<DiscordFriend>> loadBlockedFriends() {
        File file = new File("DataBase/blockedFriendsMap.bin");

        if(!file.exists()) {
            return new HashMap<>();
        }

        HashMap<String, ArrayList<DiscordFriend>> blockedFriendsMap = null;

        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))){
            blockedFriendsMap = (HashMap<String, ArrayList<DiscordFriend>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return blockedFriendsMap;
    }

    public void updateMessagesMap(HashMap<String,ArrayList<Message>> messagesMap) {
        File file = new File("DataBase/messagesMap.bin");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))){
            outputStream.writeObject(messagesMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, ArrayList<Message>> loadMessagesMap() {
        File file = new File("DataBase/messagesMap.bin");

        HashMap<String, ArrayList<Message>> messagesMap = null;

        if( !file.exists() ) {
            messagesMap = new HashMap<>();
            ArrayList<String> usersList = loadUsersList();

            for (String user : usersList) {
                ArrayList<Message> arrayList = new ArrayList<>();
                messagesMap.put(user, arrayList);
            }

            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                objectOutputStream.writeObject(messagesMap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return messagesMap;
        }

        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))){
            messagesMap = (HashMap<String, ArrayList<Message>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return messagesMap;
    }
}
package File;

import Classes.DiscordUser;

import java.io.*;

import Exceptions.*;

public class DiscordFile {
    private static DiscordFile discordFile = null;

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
                    System.out.println("unable to make users list -> DiscordFile class");
                    throw new userExistException();
                }
            }
        }

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Files/Users/" + discordUser.getUserName() + ".bin"))) {
            objectOutputStream.writeObject(discordUser);
            System.out.println("yes");
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

            if (discordUser == null) {
                throw new userOrPasswordInvalidException();
            }

            if ( !discordUser.getPassword().equals(password)) {
                throw new userOrPasswordInvalidException();
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

    }

}
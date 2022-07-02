package DiscordClasses;

/**
 * Admins has various permissions
 * these permissions are constant
 *
 * @author wasiq
 */
public enum Permission {
    CHANNEL_CREATE,
    CHANNEL_REMOVE,
    USERS_ADD,
    USERS_REMOVE,
    USERS_SET_LIMIT,
    USERS_BAN,
    SERVER_RENAME,
    CHAT_HISTORY_ACCESS,
    MESSAGE_PIN;

    public static Permission getPermission(String permission) {
        switch (permission) {
            case "createChannel" -> {
                return CHANNEL_CREATE;
            }
            case "removeChannel" -> {
                return CHANNEL_REMOVE;
            }
            case "addMember" -> {
                return USERS_ADD;
            }
            case "removeUser" -> {
                return USERS_REMOVE;
            }
            case "userLimit" -> {
                return USERS_SET_LIMIT;
            }
            case "banUser" -> {
                return USERS_BAN;
            }
            case "renameServer" -> {
                return SERVER_RENAME;
            }
            case "chatHistoryAccess" -> {
                return CHAT_HISTORY_ACCESS;
            }
            case "messagePin" -> {
                return MESSAGE_PIN;
            }
        }

        return null;
    }
}

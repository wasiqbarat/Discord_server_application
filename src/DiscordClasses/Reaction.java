package DiscordClasses;

public enum Reaction {
    LIKE,
    DISLIKE,
    HAHA;

    public static Reaction getReaction(String reaction) {
        switch (reaction) {
            case "like" -> {
                return LIKE;
            }
            case "disLike" -> {
                return DISLIKE;
            }
            case "haha" -> {
                return HAHA;
            }
        }

        return null;
    }

}

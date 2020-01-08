package linebot.linelogin.entity;

public class UserProfile {

    public final String userId;
    public final String displayName;
    public final String pictureUrl;

    public UserProfile(String userId, String displayName, String pictureUrl) {
        this.userId = userId;
        this.displayName = displayName;
        this.pictureUrl = pictureUrl;
    }

}

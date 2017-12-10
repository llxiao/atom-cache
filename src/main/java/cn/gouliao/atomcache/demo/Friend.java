package cn.gouliao.atomcache.demo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * 类说明
 *
 * @author shawn
 * @since 2017/12/10
 */
@Data
public class Friend implements Serializable{
    @SerializedName("friendName")
    @Expose
    private String friendName;


    public static final class Builder {
        public String friendName;

        public Builder() {
        }

        public static Builder aFriend() {
            return new Builder();
        }

        public Builder withFriendName(String friendName) {
            this.friendName = friendName;
            return this;
        }

        public Friend build() {
            Friend friend = new Friend();
            friend.setFriendName(friendName);
            return friend;
        }
    }
}

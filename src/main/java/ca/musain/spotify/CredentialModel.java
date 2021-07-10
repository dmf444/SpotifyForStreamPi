package ca.musain.spotify;

import java.time.LocalDateTime;

public class CredentialModel {

    private String accessToken;
    private LocalDateTime expireTime;
    private String refreshToken;

    public CredentialModel(String accessToken, int secondsOffset, String refreshToken) {
        this.accessToken = accessToken;
        this.expireTime = LocalDateTime.now().plusSeconds(secondsOffset);
        this.refreshToken = refreshToken;
    }

    public boolean isExpired() {
        if(LocalDateTime.now().compareTo(this.expireTime) > 0) {
            return true;
        }
        return false;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getLocalDateTime() {
        return expireTime.toString();
    }

    public void setLocalDateTime(String localDateTime) {
        this.expireTime = LocalDateTime.parse(localDateTime);
    }

    public void setLocalDateTime(int time) {
        this.expireTime = LocalDateTime.now().plusSeconds(time);
    }
}

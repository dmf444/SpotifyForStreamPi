package ca.musain.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;


public class SpotifyRefreshToken {

    private SpotifyApi api;

    public SpotifyRefreshToken(SpotifyApi api) {
        this.api = api;

    }

    public AuthorizationCodeCredentials renewToken() throws Exception  {
        return api.authorizationCodePKCERefresh().build().execute();
    }


}

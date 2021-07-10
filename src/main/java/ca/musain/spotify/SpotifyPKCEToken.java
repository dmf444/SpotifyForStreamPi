package ca.musain.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;


public class SpotifyPKCEToken {

    private SpotifyApi api;
    private String verifier;
    private String spotifyResponse;

    public SpotifyPKCEToken(SpotifyApi api, String spotifyResponseCode, String codeVerifier) {
        this.api = api;
        this.verifier = codeVerifier;
        this.spotifyResponse = spotifyResponseCode;
    }

    public AuthorizationCodeCredentials requestToken() throws Exception {
        return api.authorizationCodePKCE(this.spotifyResponse, this.verifier).build().execute();
    }


}

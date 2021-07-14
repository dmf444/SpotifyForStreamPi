package ca.musain.spotify;

import ca.musain.SpotifyAction;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.requests.data.AbstractDataRequest;
import com.wrapper.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;
import java.net.URI;

public class SpotifyInstance {

    private static SpotifyInstance instance;
    private SpotifyApi api;
    private SpotifyPKCERequest initialRequest;
    private CredentialModel credentials;
    private CurrentlyPlayingContext context;

    private SpotifyInstance() {
        api = new SpotifyApi.Builder()
            .setClientId("5a0fec4baea14cf8899d9e63ead87471")
            .setRedirectUri(SpotifyHttpManager.makeUri("https://spoti-pi.musain.ca/"))
            .build();
    }


    public SpotifyApi getAPI() {
        return api;
    }

    public CredentialModel getCredentials() {
        return this.credentials;
    }

    public boolean isEnabled() { return !this.getAPI().getRefreshToken().equals(""); }

    public <T extends AbstractDataRequest> T createRequest(T request) throws Exception {
        if(this.getCredentials().isExpired()) {
            SpotifyRefreshToken spotifyRefreshToken = new SpotifyRefreshToken(this.getAPI());
            AuthorizationCodeCredentials creds = spotifyRefreshToken.renewToken();
            updateCredentials(creds);
        }
        return (T) request.execute();
    }

    public CurrentlyPlayingContext quickRequest() throws Exception {
        if(context == null) {
            instance.renewTokenIfNeeded();
            GetInformationAboutUsersCurrentPlaybackRequest infoReq = instance.getAPI().getInformationAboutUsersCurrentPlayback().build();
            this.context = infoReq.execute();
        }
        return context;
    }

    public void renewTokenIfNeeded() throws Exception {
        if(this.getCredentials().isExpired()) {
            SpotifyRefreshToken spotifyRefreshToken = new SpotifyRefreshToken(this.getAPI());
            AuthorizationCodeCredentials creds = spotifyRefreshToken.renewToken();
            updateCredentials(creds);
        }
    }

    public URI createInitialRequest() {
        this.initialRequest = new SpotifyPKCERequest(this.getAPI());
        return this.initialRequest.buildUri();
    }

    public boolean convertToToken(String spotifyToken) {
        if(this.initialRequest != null) {
            SpotifyPKCEToken tokenRequest = new SpotifyPKCEToken(this.getAPI(), spotifyToken, this.initialRequest.getCodeVerifier());
            try {
                AuthorizationCodeCredentials creds = tokenRequest.requestToken();
                updateCredentials(creds);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void updateCredentials(AuthorizationCodeCredentials creds) {
        if(this.credentials == null) {
            this.credentials = new CredentialModel(creds.getAccessToken(), creds.getExpiresIn(), creds.getRefreshToken());
        } else {
            this.credentials.setAccessToken(creds.getAccessToken());
            this.credentials.setLocalDateTime(creds.getExpiresIn());
            this.credentials.setRefreshToken(creds.getRefreshToken());
        }

        this.getAPI().setAccessToken(creds.getAccessToken());
        this.getAPI().setRefreshToken(creds.getRefreshToken());
        SpotifyAction.spotifyAction.credentialsUpdated();
    }

    public void initCredentialsConfig(CredentialModel model) {
        this.credentials = model;
        this.getAPI().setAccessToken(model.getAccessToken());
        this.getAPI().setRefreshToken(model.getRefreshToken());
    }




    public static SpotifyInstance getInstance() {
        if(instance == null) {
            instance = new SpotifyInstance();
        }
        return instance;
    }

}

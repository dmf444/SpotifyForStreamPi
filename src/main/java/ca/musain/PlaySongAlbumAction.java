package ca.musain;

import ca.musain.spotify.SpotifyInstance;
import com.stream_pi.action_api.actionproperty.property.Property;
import com.stream_pi.action_api.actionproperty.property.Type;
import com.stream_pi.action_api.externalplugin.NormalAction;
import com.stream_pi.util.exception.MinorException;
import com.stream_pi.util.version.Version;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.requests.data.player.StartResumeUsersPlaybackRequest;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.gson.JsonParser;

public class PlaySongAlbumAction extends NormalAction {

    public PlaySongAlbumAction() {
        setName("Play Song/Playlist");
        setCategory("Spotify");
        setAuthor("dmf444");
        setHelpLink("https://github.com/dmf444/SpotifyForStreamPi");
        setVisibilityInServerSettingsPane(false);
        setVersion(new Version(1,0,0));
    }

    @Override
    public void initProperties() throws MinorException {
        Property url = new Property("url", Type.STRING);
        url.setDisplayName("Spotify Song/Playlist URL");

        addClientProperties(url);
    }


    private String convertToUri(String url) {
        if(url != null && !url.equals("")) {
            if(url.startsWith("http")) {
                try {
                    URL properUrl = new URL(url);
                    String[] urlExplode = properUrl.getPath().split("/");
                    return "spotify:" + urlExplode[1] + ":" + urlExplode[2];
                } catch (MalformedURLException e) { }
            }
        }
        return null;
    }


    @Override
    public void onActionClicked() throws MinorException {
        String savedUrl = getClientProperties().getSingleProperty("url").getStringValue();
        String uri = convertToUri(savedUrl);
        if(SpotifyInstance.getInstance().isEnabled()) {
            if(uri != null){
                SpotifyApi api = SpotifyInstance.getInstance().getAPI();
                try {
                    System.out.println(uri);
                    StartResumeUsersPlaybackRequest.Builder request = api.startResumeUsersPlayback();
                    if(uri.contains("track")){
                        request.uris(JsonParser.parseString("[\""+ uri +"\"]").getAsJsonArray());
                    } else {
                        request.context_uri(uri);
                    }
                    SpotifyInstance.getInstance().createRequest(request.build());
                } catch (Exception e) {
                    throw new MinorException(e.getMessage());
                }
            } else {
                throw new MinorException("Invalid URL!");
            }
        } else {
            throw new MinorException("You are not logged into spotify!");
        }
    }
}

package ca.musain;

import ca.musain.spotify.SpotifyInstance;
import com.stream_pi.action_api.externalplugin.ToggleAction;
import com.stream_pi.util.exception.MinorException;
import com.stream_pi.util.version.Version;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;
import com.wrapper.spotify.requests.data.player.PauseUsersPlaybackRequest;
import com.wrapper.spotify.requests.data.player.StartResumeUsersPlaybackRequest;

public class PlayPauseAction extends ToggleAction {

    public PlayPauseAction() {
        setName("Play/Pause Current Track");
        setCategory("Spotify");
        setAuthor("dmf444");
        setHelpLink("https://github.com/spotify");
        setVisibilityInServerSettingsPane(false);
        setVersion(new Version(1,0,2));

    }

    @Override
    public void onClientConnected() throws MinorException {
        SpotifyInstance instance = SpotifyInstance.getInstance();
        if(instance.isEnabled()) {
            try {
                instance.renewTokenIfNeeded();
                CurrentlyPlayingContext context = instance.getAPI().getInformationAboutUsersCurrentPlayback().build().execute();
                setCurrentToggleStatus(context.getIs_playing());
            } catch (Exception e) {
                throw new MinorException(e.getMessage());
            }
        }
    }

    @Override
    public void onToggleOn() throws MinorException {
        if(SpotifyInstance.getInstance().isEnabled()) {
            SpotifyApi api = SpotifyInstance.getInstance().getAPI();
            try {
                StartResumeUsersPlaybackRequest request = api.startResumeUsersPlayback().build();
                SpotifyInstance.getInstance().createRequest(request);
            } catch (Exception e) {
                throw new MinorException(e.getMessage());
            }
        } else {
            throw new MinorException("You are not logged into spotify!");
        }
    }

    @Override
    public void onToggleOff() throws MinorException {
        if(SpotifyInstance.getInstance().isEnabled()) {
            SpotifyApi api = SpotifyInstance.getInstance().getAPI();
            try {
                PauseUsersPlaybackRequest request = api.pauseUsersPlayback().build();
                SpotifyInstance.getInstance().createRequest(request);
            } catch (Exception e) {
                throw new MinorException(e.getMessage());
            }
        }else {
            throw new MinorException("You are not logged into spotify!");
        }
    }
}

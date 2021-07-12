package ca.musain;

import ca.musain.spotify.SpotifyInstance;
import com.stream_pi.util.exception.MinorException;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.requests.data.player.StartResumeUsersPlaybackRequest;
import com.wrapper.spotify.requests.data.player.ToggleShuffleForUsersPlaybackRequest;

public class SpotifyShuffleAction extends ToggleActionExtended {

    public SpotifyShuffleAction() {
        setName("Toggle Shuffle");
    }

    @Override
    public boolean getRuntimeToggleState() {
        SpotifyInstance instance = SpotifyInstance.getInstance();
        if(instance.isEnabled()) {
            try {
                instance.renewTokenIfNeeded();
                CurrentlyPlayingContext context = instance.getAPI().getInformationAboutUsersCurrentPlayback().build().execute();
                return context.getShuffle_state();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void onToggleOn() throws MinorException {
        if(SpotifyInstance.getInstance().isEnabled()) {
            if(this.getToggleState()) return;

            Boolean execState = this.buildExecuteToggle(true);
            if(execState != null) this.setToggleState(execState);
            if (execState == null) throw new MinorException("Unable to toggle shuffle.");

        }
    }

    @Override
    public void onToggleOff() throws MinorException {
        if(SpotifyInstance.getInstance().isEnabled()) {
            if(!this.getToggleState()) return;

            Boolean execState = this.buildExecuteToggle(false);
            if(execState != null) this.setToggleState(execState);
            if (execState == null) throw new MinorException("Unable to toggle shuffle.");

        }
    }

    private Boolean buildExecuteToggle(boolean newState) {
        SpotifyApi api = SpotifyInstance.getInstance().getAPI();
        try {
            ToggleShuffleForUsersPlaybackRequest request = api.toggleShuffleForUsersPlayback(newState).build();
            SpotifyInstance.getInstance().createRequest(request);
            return !newState;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

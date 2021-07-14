package ca.musain;

import ca.musain.spotify.SpotifyInstance;
import com.stream_pi.util.exception.MinorException;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.requests.data.player.SetRepeatModeOnUsersPlaybackRequest;

public class SpotifyLoopTrackAction extends ToggleActionExtended {

    public SpotifyLoopTrackAction() {
        setName("Loop Track");
    }

    @Override
    public boolean getRuntimeToggleState() {
        SpotifyInstance instance = SpotifyInstance.getInstance();
        if(instance.isEnabled()) {
            try {
                CurrentlyPlayingContext context = instance.quickRequest();
                return context.getRepeat_state().equals("track");
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

            boolean execState = this.buildExecuteToggle("track");
            if(execState) {
                this.setToggleState(true);
            } else {
                throw new MinorException("Unable to toggle shuffle.");
            }
        }
    }

    @Override
    public void onToggleOff() throws MinorException {
        if(SpotifyInstance.getInstance().isEnabled()) {
            if(!this.getToggleState()) return;

            boolean execState = this.buildExecuteToggle("off");
            if(execState) {
                this.setToggleState(false);
            } else {
                throw new MinorException("Unable to toggle shuffle.");
            }
        }
    }


    private boolean buildExecuteToggle(String newState) {
        SpotifyApi api = SpotifyInstance.getInstance().getAPI();
        try {
            SetRepeatModeOnUsersPlaybackRequest request = api.setRepeatModeOnUsersPlayback(newState).build();
            SpotifyInstance.getInstance().createRequest(request);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

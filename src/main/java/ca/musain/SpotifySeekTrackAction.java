package ca.musain;

import ca.musain.spotify.SpotifyInstance;
import com.stream_pi.action_api.actionproperty.property.Property;
import com.stream_pi.action_api.actionproperty.property.Type;
import com.stream_pi.action_api.externalplugin.NormalAction;
import com.stream_pi.util.exception.MinorException;
import com.stream_pi.util.version.Version;
import com.wrapper.spotify.requests.data.player.SkipUsersPlaybackToNextTrackRequest;
import com.wrapper.spotify.requests.data.player.SkipUsersPlaybackToPreviousTrackRequest;

public class SpotifySeekTrackAction extends NormalAction {

    public SpotifySeekTrackAction() {
        setName("Track Skip");
        setCategory("Spotify");
        setAuthor("dmf444");
        setHelpLink("https://github.com/dmf444/SpotifyForStreamPi");
        setVisibilityInServerSettingsPane(false);
        setVersion(new Version(1,1,0));
    }

    @Override
    public void initProperties() throws MinorException {
        Property reverseSeekDirection = new Property("seek_direction", Type.BOOLEAN);
        reverseSeekDirection.setDisplayName("Reverse Direction?");
        reverseSeekDirection.setDefaultValueBoolean(false);

        addClientProperties(reverseSeekDirection);
    }

    @Override
    public void onActionClicked() throws MinorException {
        SpotifyInstance instance = SpotifyInstance.getInstance();
        if(!instance.getAPI().getRefreshToken().equals("")) {

            boolean reverseDirection = getClientProperties().getSingleProperty("seek_direction").getBoolValue();

            if(reverseDirection) {

                SkipUsersPlaybackToPreviousTrackRequest request = instance.getAPI().skipUsersPlaybackToPreviousTrack().build();
                try {
                    instance.createRequest(request);
                } catch (Exception e) {
                    throw new MinorException(e.getMessage());
                }

            } else {

                SkipUsersPlaybackToNextTrackRequest request = instance.getAPI().skipUsersPlaybackToNextTrack().build();
                try {
                    instance.createRequest(request);
                } catch (Exception e) { throw new MinorException(e.getMessage()); }

            }
        }
        getLogger().warning("Action Completed.");
    }


}

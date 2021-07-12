package ca.musain;

import com.stream_pi.action_api.externalplugin.ToggleAction;
import com.stream_pi.util.exception.MinorException;
import com.stream_pi.util.version.Version;

public abstract class ToggleActionExtended extends ToggleAction {

    public ToggleActionExtended() {
        setCategory("Spotify");
        setAuthor("dmf444");
        setHelpLink("https://github.com/dmf444/SpotifyForStreamPi");
        setVisibilityInServerSettingsPane(false);
        setVersion(new Version(1,0,0));
    }

    private boolean toggleState = false;

    @Override
    public void onClientConnected() throws MinorException {
        this.toggleState = getRuntimeToggleState();
    }

    public abstract boolean getRuntimeToggleState();


    public boolean getToggleState() {
        return this.toggleState;
    }

    public void setToggleState(boolean toggleState) {
        this.toggleState = toggleState;
    }

}

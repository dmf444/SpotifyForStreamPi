package ca.musain;

import ca.musain.spotify.CredentialModel;
import ca.musain.spotify.SpotifyInstance;
import com.stream_pi.action_api.actionproperty.property.Property;
import com.stream_pi.action_api.actionproperty.property.Type;
import com.stream_pi.action_api.externalplugin.NormalAction;
import com.stream_pi.util.exception.MinorException;
import com.stream_pi.util.version.Version;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import javafx.scene.control.Button;


public class SpotifyAction extends NormalAction {

    public static SpotifyAction spotifyAction;
    private Button initializeBtn;
    private Button connectionStatus;

    public SpotifyAction() {
        setName("Spotify Playback Plugin");
        setCategory("Spotify");
        setAuthor("dmf444");
        setHelpLink("https://github.com/dmf444/SpotifyForStreamPi");
        setVisibilityInPluginsPane(false);
        setVersion(new Version(1,0,0));

        initializeBtn = new Button("Link Spotify Account");
        connectionStatus = new Button("Disconnected");
        connectionStatus.setDisable(true);
        connectionStatus.setStyle("-fx-text-fill: #ff0000");
        buttonActionSetup();
        setServerSettingsButtonBar(initializeBtn, connectionStatus);

        SpotifyAction.spotifyAction = this;
    }

    @Override
    public void onActionClicked() throws MinorException { }

    @Override
    public void initProperties() throws MinorException {
        
        Property token = new Property("token", Type.STRING);
        token.setDisplayName("Spotify Token");

        Property accessToken = new Property("access_token", Type.STRING);
        accessToken.setVisible(false);

        Property renewTime = new Property("expire_time", Type.STRING);
        renewTime.setVisible(false);

        Property refreshToken = new Property("refresh_token", Type.STRING);
        refreshToken.setVisible(false);

        addServerProperties(token, accessToken, renewTime, refreshToken);
    }

    @Override
    public void initAction() throws MinorException {
        String accessToken = getServerProperties().getSingleProperty("access_token").getStringValue();
        String tokenResponse = getServerProperties().getSingleProperty("token").getStringValue();
        if(accessToken != null && !accessToken.equals("")) {

            String expiryDate = getServerProperties().getSingleProperty("expire_time").getStringValue();
            String refreshToken = getServerProperties().getSingleProperty("refresh_token").getStringValue();
            CredentialModel model = new CredentialModel(accessToken, 0, refreshToken);
            model.setLocalDateTime(expiryDate);
            SpotifyInstance.getInstance().initCredentialsConfig(model);
            setConnectedStatus();

        } else if(tokenResponse != null && !tokenResponse.equals("")) {
            boolean success = SpotifyInstance.getInstance().convertToToken(tokenResponse);
            if (success) setConnectedStatus();
        }
    }

    public void credentialsUpdated() {
        CredentialModel credentials = SpotifyInstance.getInstance().getCredentials();
        if(credentials != null) {
            try {
                getServerProperties().getSingleProperty("access_token").setStringValue(credentials.getAccessToken());
                getServerProperties().getSingleProperty("expire_time").setStringValue(credentials.getLocalDateTime());
                getServerProperties().getSingleProperty("refresh_token").setStringValue(credentials.getRefreshToken());
                saveServerProperties();
            } catch (MinorException e) {
                //Do nothing...
            }
        }
    }

    private void buttonActionSetup() {
        initializeBtn.setOnAction(action -> {
            try {

                String refreshToken = getServerProperties().getSingleProperty("refresh_token").getStringValue();

                if(refreshToken == null || refreshToken.equals("")) {
                    String inputTokenValue = getServerProperties().getSingleProperty("token").getStringValue();

                    if(inputTokenValue.equals("")) {
                        SpotifyInstance instance = SpotifyInstance.getInstance();
                        URI uri = instance.createInitialRequest();
                        Desktop desktop = Desktop.getDesktop();
                        desktop.browse(uri);

                    } else {
                        boolean success = SpotifyInstance.getInstance().convertToToken(inputTokenValue);
                        if (success) setConnectedStatus();
                    }

                }

            } catch (MinorException | IOException e) {
                getLogger().warning("you've failed.");
            }

        });
    }

    private void setConnectedStatus() {
        initializeBtn.setDisable(true);
        connectionStatus.setText("Connected");
        connectionStatus.setStyle("-fx-text-fill: #00ff00");
    }

}

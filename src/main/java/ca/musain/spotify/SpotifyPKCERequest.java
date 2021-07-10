package ca.musain.spotify;

import com.wrapper.spotify.SpotifyApi;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SpotifyPKCERequest {

    private SpotifyApi api;
    private String codeVerifier;
    private String codeChallenge;

    public SpotifyPKCERequest(SpotifyApi api) {
        this.api = api;

        this.generateVerifier();
        this.generateChallenge();
    }

    private void generateVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        this.codeVerifier =  Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
    }

    private void generateChallenge() {
        try {
            byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bytes, 0, bytes.length);
            byte[] digest = messageDigest.digest();
            this.codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);

        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
    }

    public URI buildUri() {
        URI uri = api.authorizationCodePKCEUri(codeChallenge).scope("user-read-playback-state,user-modify-playback-state,user-read-currently-playing").build().execute();
        return uri;
    }

    public String getCodeVerifier() {
        return this.codeVerifier;
    }

}

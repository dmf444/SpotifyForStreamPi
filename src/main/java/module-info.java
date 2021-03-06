module ca.musain.spotify {

    requires com.stream_pi.action_api;
    requires com.stream_pi.util;

    requires spotify.web.api.java;
    requires java.desktop;
    requires com.google.gson;

    exports ca.musain.spotify;

    provides com.stream_pi.action_api.externalplugin.ExternalPlugin with ca.musain.SpotifyAction,
        ca.musain.SpotifySeekTrackAction, ca.musain.PlayPauseAction, ca.musain.SpotifyShuffleAction,
        ca.musain.SpotifyLoopTrackAction, ca.musain.PlaySongAlbumAction;
}

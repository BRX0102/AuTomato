package hashmaps.automatoandroid;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Sal on 4/29/2017.
 */

public class AutomatoAPI {
    private Socket mSocket;
    final String serverURL = "https://shielded-brushlands-57140.herokuapp.com/";
    public AutomatoAPI() {
    }

    /**
     * Sends latitude, longitude, and what type of blocked is it
     */
    public void sendBlockade(final Location aLocation, final int num){
        try {
            mSocket = IO.socket(serverURL);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    // Sending an object
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("latitude", aLocation.getLatitude());
                        obj.put("longitude", aLocation.getLongitude());
                        obj.put("blockType", num);
                        mSocket.emit("markEndPoint", obj);
                        mSocket.disconnect();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {}

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {}

            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

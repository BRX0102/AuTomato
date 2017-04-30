package hashmaps.automatoandroid;

import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Sal on 4/29/2017.
 */

public class AutomatoAPI {
    private Socket mSocket;

    final String TAG = "AutomatoAPI";
    final String serverURL = "https://shielded-brushlands-57140.herokuapp.com";
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
                        //getInitData();
                        //mSocket.disconnect();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //SET NEW POINT ON MAP WITH THIS
                }

            }).on("newPoint", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "got a response");
                    JSONObject data = (JSONObject) args[0];
                    Double newLatitude;
                    Double newLongitude;
                    int newBlockType;
                    try {
                        newLatitude= data.getDouble("latitude");
                        newLongitude= data.getDouble("longitude");
                        newBlockType = data.getInt("blockType");

                        Log.d(TAG,""+newLatitude);
                        Log.d(TAG,""+newLongitude);
                        Log.d(TAG,""+newBlockType);

                    } catch (JSONException e) {
                        Log.d(TAG,"ERROR");
                    }
                    mSocket.disconnect();
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d(TAG, "disconnected");
                }

            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initial values
     */
    public void initialValues(){
        try {
            mSocket = IO.socket(serverURL);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    mSocket.emit("readData");
                }

            }).on("coordinates", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "got a response");
                    JSONObject data = (JSONObject) args[0];
                    Double newLatitude;
                    Double newLongitude;
                    int newBlockType;
                    Iterator<String> iter = data.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        Log.d(TAG, key);
                        try {
                            JSONArray value = (JSONArray) data.get(key);
                            for (int i = 0; i < value.length(); i++) {
                                JSONObject objects = value.getJSONObject(i);
                                Iterator key_2 = objects.keys();
                                while (key_2.hasNext()) {
                                    String k = key_2.next().toString();
                                    System.out.println("Key : " + k + ", value : "
                                            + objects.getString(k));
                                }
                                // System.out.println(objects.toString());
                                System.out.println("-----------");
                            }
                            } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mSocket.disconnect();
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d(TAG, "disconnected");
                }

            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

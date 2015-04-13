package darrenretinambpcrystalwell.Game;

import android.util.Log;

import java.io.IOException;

import AndroidCallback.DotsAndroidCallback;
import Constants.DotsConstants;
import Sockets.DotsClient;
import Sockets.DotsServer;
import Sockets.DotsServerClientParent;

/**
 * Created by JiaHao on 8/4/15.
 */
public class DotsGameTask implements Runnable {

    private final String TAG = "DotsGameTask";
    private final DotsServerClientParent dotsServerClientParent;



    private DotsAndroidCallback dotsAndroidCallback;

    public DotsGameTask(int playerId, int port, String ipAddress) throws IOException {

        Log.d(TAG, "PlayerId: " + playerId + " port: " + port + " IP address: " + ipAddress);
        // if the player is 0 or the port indicates a single player, we force the player to be
        // the server
        if (playerId == 0 || port == DotsConstants.SINGLE_PLAYER_PORT) {
            this.dotsServerClientParent = new DotsServer(port);
        } else {
            this.dotsServerClientParent = new DotsClient(ipAddress, port);
        }

        this.dotsServerClientParent.setAndroidCallback(dotsAndroidCallback);


    }

    public DotsServerClientParent getDotsServerClientParent() {
        return dotsServerClientParent;
    }

    public void setDotsAndroidCallback(DotsAndroidCallback dotsAndroidCallback) {
        this.dotsAndroidCallback = dotsAndroidCallback;
    }

    @Override
    public void run() {

        this.dotsServerClientParent.setAndroidCallback(this.dotsAndroidCallback);


        try {
            dotsServerClientParent.start();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}

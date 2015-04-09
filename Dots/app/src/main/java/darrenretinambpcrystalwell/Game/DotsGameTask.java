package darrenretinambpcrystalwell.Game;

import java.io.IOException;

import AndroidCallback.DotsAndroidCallback;
import Sockets.DotsClient;
import Sockets.DotsServer;
import Sockets.DotsServerClientParent;

/**
 * Created by JiaHao on 8/4/15.
 */
public class DotsGameTask implements Runnable {


    private final DotsServerClientParent dotsServerClientParent;



    private DotsAndroidCallback dotsAndroidCallback;

    public DotsGameTask(int playerId, int port, String ipAddress) throws IOException {


        if (playerId == 0) {
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

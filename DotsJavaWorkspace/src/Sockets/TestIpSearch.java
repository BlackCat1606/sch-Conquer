package Sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by JiaHao on 12/4/15.
 */
public class TestIpSearch {

    public static void main(String[] args) {


//        try {
//            checkHosts("192.168.1");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        HashMap<Integer, Integer> affectedMap = new HashMap<Integer, Integer>();
        affectedMap.get(1);

    }

    public static void checkHosts(String subnet) throws IOException {
        int timeout=100;
        for (int i=1;i<255;i++){
            String host=subnet + "." + i;
            if (InetAddress.getByName(host).isReachable(timeout)){
                System.out.println(host + " is reachable");
            }
        }
    }
}

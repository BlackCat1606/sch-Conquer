package Sockets;

import Constants.DotsConstants;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

/**
 * Created by JiaHao on 12/4/15.
 */
public class TestIpSearch {

    public static void main(String[] args) throws IOException {


        Socket client = new Socket();
        client.connect(new InetSocketAddress("10.12.17.172", DotsConstants.CLIENT_PORT), 100);

//        try {
//            checkHosts("10.12");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



    }

    public static void checkHosts(String subnet) throws IOException {
        int timeout=10;

        for (int j = 17; j < 18; j++) {

            String subSubnet = subnet + "." + j;

            for (int i=1;i<255;i++){
                String host=subSubnet + "." + i;
                System.out.println(host);
                try {
                    Socket client = new Socket();
                    client.connect(new InetSocketAddress(host, DotsConstants.CLIENT_PORT), timeout);
                } catch (SocketException e) {
                    System.out.println("Cannot connect");
                } catch (SocketTimeoutException e) {
                    System.out.println("Timed out");
                }

//            if (InetAddress.getByName(host).isReachable(timeout)){
//                System.out.println(host + " is reachable");
//            }
            }
        }



        System.out.println("DOne");
    }
}

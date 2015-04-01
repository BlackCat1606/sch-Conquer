package Sockets;

import AwesomeSockets.AwesomeClientSocket;
import AwesomeSockets.AwesomeServerSocket;
import Dots.Point;
import Model.DotsInteraction;
import Model.DotsInteractionStates;
import Model.DotsMessage;
import Model.MessageLocks;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Helper methods to make reading and writing DotsMessage objects from AwesomeSockets easier
 * Created by JiaHao on 25/2/15.
 */
public class DotsSocketHelper {

    // Initialise a lock object so that threads do not try to send message simultaneously
    public static MessageLocks locks = new MessageLocks();

    // Serialization functions

    /**
     * A server will call this method to send a message to the client
     * @param server  server to send through
     * @param message object to send
     * @throws IOException
     */
    public static void sendMessageToClient(AwesomeServerSocket server, DotsMessage message) throws IOException {
        synchronized (locks.getLock(0)) {

            System.out.println("Sending message: " + message.toString());
            ObjectOutputStream serverObjectOutputStream = new ObjectOutputStream(server.getServerOutputStreamForClient(0));
            serverObjectOutputStream.writeObject(message);
            System.out.println(message.toString() + " sent!");

        }
    }

    /**
     * A server will call this to read a message from the client
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static DotsMessage readMessageFromClient(AwesomeServerSocket server) throws IOException, ClassNotFoundException {

        synchronized (locks.getLock(1)) {
            System.out.println("Reading message!");
            ObjectInputStream serverObjectInputStream = new ObjectInputStream(server.getServerInputStreamForClient(0));
            System.out.println("message received");
            return (DotsMessage)serverObjectInputStream.readObject();
        }

    }

    /**
     * A client calls this to send a message to the server
     */
    public static void sendMessageToServer(AwesomeClientSocket client, DotsMessage message) throws IOException {

        synchronized (locks.getLock(2)) {
            System.out.println("Sending message: " + message.toString());
            ObjectOutputStream clientObjectOutputStream = new ObjectOutputStream(client.getClientOutputStream());
            clientObjectOutputStream.writeObject(message);
            System.out.println(message.toString() + " sent!");
        }

    }

    /**
     * A client calls this to read a message from the server
     */
    public static DotsMessage readMessageFromServer(AwesomeClientSocket client) throws IOException, ClassNotFoundException {
        synchronized (locks.getLock(3)) {
            System.out.println("Reading message!");
            ObjectInputStream clientObjectInputStream = new ObjectInputStream(client.getClientInputStream());
            System.out.println("message received");
            return (DotsMessage)clientObjectInputStream.readObject();
        }

    }

}

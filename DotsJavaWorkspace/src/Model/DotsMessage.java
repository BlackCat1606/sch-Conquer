package Model;

import Dots.Dot;

/**
 * Created by JiaHao on 24/3/15.
 */
public class DotsMessage {

    private final DotsMessageType messageType;

    private Dot[][] boardMessage;

    private boolean response;


    public DotsMessage(Dot[][] boardMessage) {
        this.boardMessage = boardMessage;
        this.messageType = DotsMessageType.BOARD;
    }

    public DotsMessage(boolean response) {
        this.response = response;
        this.messageType = DotsMessageType.RESPONSE;
    }
}


enum DotsMessageType {

    RESPONSE, BOARD,

}
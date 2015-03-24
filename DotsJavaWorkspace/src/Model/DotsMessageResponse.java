package Model;

/**
 * Created by JiaHao on 24/3/15.
 */
public class DotsMessageResponse implements DotsMessage {

    private final boolean response;

    public DotsMessageResponse(boolean response) {
        this.response = response;
    }

    public boolean getResponse() {
        return response;
    }
}

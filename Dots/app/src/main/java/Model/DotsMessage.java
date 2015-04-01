package Model;

import java.io.Serializable;

/**
 * Empty interface that extends serializable to serialise implementing objects
 *
 * All messages sent over sockets will extend this interface, so that when we read the message, we simply have to do
 * e.g if (message instanceOf DotsMessageResponse) {
 *
 * },
 *
 * to deal with the message accordingly
 *
 *
 *
 * Created by JiaHao on 24/3/15.
 */
public interface DotsMessage extends Serializable {

}

